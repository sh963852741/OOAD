package cn.edu.xmu.flashsale.service;

import cn.edu.xmu.flashsale.dao.FlashSaleDao;
import cn.edu.xmu.flashsale.model.bo.FlashSaleItem;
import cn.edu.xmu.flashsale.model.bo.TimeSegment;
import cn.edu.xmu.flashsale.model.po.FlashSaleItemPo;
import cn.edu.xmu.flashsale.model.po.FlashSalePo;
import cn.edu.xmu.flashsale.model.po.SKUPo;
import cn.edu.xmu.flashsale.model.vo.FlashSaleItemRetVo;
import cn.edu.xmu.flashsale.model.vo.FlashsaleVo;
import cn.edu.xmu.flashsale.service.dubbo.ITimeSegmentService;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import cn.edu.xmu.goods.client.IGoodsService;

/**
 * @author Ming Qiu
 * @date Created in 2020/11/21 15:00
 **/
@Service
@Slf4j
public class FlashSaleService implements InitializingBean {

    @Value("${flashsale.loadtime}")
    private Integer loadTime;

    @Resource
    private ReactiveRedisTemplate<String, Serializable> reactiveRedisTemplate;
    @Resource
    private RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    IGoodsService goodsService;
    @Autowired
    ITimeSegmentService timeSegmentService;
    @Autowired
    FlashSaleDao flashSaleDao;

    public ReturnObject addFlashSale(long timeSegId, LocalDateTime flashDate){
        var time = timeSegmentService.getTimeSegmentById(timeSegId);
        if(time == null){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "时间段不存在");
        }

        FlashSalePo flashSalePo = new FlashSalePo();
        flashSalePo.setGmtCreate(LocalDateTime.now());
        flashSalePo.setFlashDate(flashDate);
        flashSalePo.setTimeSegId(timeSegId);

        if(flashSaleDao.addFlashSale(flashSalePo) == 1){
            return new ReturnObject(flashSalePo);
        }else{
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    @Transactional
    public ReturnObject delFlashSale(long id){
        flashSaleDao.delFlashSaleItemByFlashSaleId(id);
        if(flashSaleDao.deleteFlashSale(id)==1){
            return new ReturnObject();
        }else{
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "秒杀活动不存在");
        }
    }

    public ReturnObject modifyFlashSale(long id, LocalDateTime date){
        if(flashSaleDao.setFlashSaleDate(id, date) == 1){
            return new ReturnObject();
        }else{
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "秒杀活动不存在");
        }
    }

    public ReturnObject addSkuToFlashSale(Long id, FlashsaleVo vo){
        FlashSaleItemPo po = vo.createPo();
        po.setGmtCreate(LocalDateTime.now());
        po.setSaleId(id);
        if(flashSaleDao.addFlashSaleItem(po) == 1){
            FlashSaleItemRetVo flashSaleItemRetVo = new FlashSaleItemRetVo();
            return new ReturnObject(flashSaleItemRetVo);
        }else{
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    public ReturnObject removeSkuToFlashSale(Long flashSaleId, Long flashSaleItemId){
        if(flashSaleDao.deleteFlashSaleItem(flashSaleItemId) == 1){
            return new ReturnObject();
        }else{
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    public Flux<FlashSaleItem> getFlashSale(Long timeSegId) {
        return reactiveRedisTemplate.opsForSet()
                .members("FlashSale" + LocalDate.now().toString() + timeSegId.toString())
                .map(x -> {
                    SKUPo skuPo =new SKUPo();
                    return new FlashSaleItem((FlashSaleItemPo) x,skuPo);
                });
    }

    /* 加载今日秒杀，不允许同时加载 */
    @Override
    public synchronized void afterPropertiesSet() throws Exception {
        List<TimeSegment> timeSegments = timeSegmentService.getFlashSaleSegments();

        List<TimeSegment> timeSegmentsForToday = new ArrayList<>();
        List<TimeSegment> timeSegmentsForTomorrow = new ArrayList<>();
        for (TimeSegment timeSegment : timeSegments) {
            if (timeSegment.getStartTime().getHour() < loadTime) {
                /* 这一部分的应该是明天的秒杀 */
                timeSegmentsForTomorrow.add(timeSegment);
            } else {
                /* 这一部分的应该是今天的秒杀 */
                timeSegmentsForToday.add(timeSegment);
            }
        }
        log.debug(timeSegments.toString());
        /* 获取了所有的秒杀活动 */
        List<FlashSalePo> effectFlashSaleToLoad = new ArrayList<>();
        effectFlashSaleToLoad.addAll(flashSaleDao.selectFlashSaleByTimeAndDate(
                LocalDate.now(),
                timeSegmentsForToday.stream().map(TimeSegment::getId).collect(Collectors.toList())
        ));
        effectFlashSaleToLoad.addAll(flashSaleDao.selectFlashSaleByTimeAndDate(
                LocalDate.now().plusDays(1),
                timeSegmentsForTomorrow.stream().map(TimeSegment::getId).collect(Collectors.toList())
        ));

        for (FlashSalePo flashSalePo : effectFlashSaleToLoad) {
            List<FlashSaleItemPo> flashSaleItemPos = flashSaleDao.getFlashSaleItemByFlashSaleId(flashSalePo.getId());
            String key ="FlashSale" + flashSalePo.getFlashDate().toLocalDate().toString() + flashSalePo.getTimeSegId();
            for(FlashSaleItemPo po:flashSaleItemPos){
                redisTemplate.boundSetOps(key).add(po);
            }
        }

        String key ="FlashSale" + LocalDate.now().toString();
        redisTemplate.expire(key,24, TimeUnit.HOURS);
    }
}

