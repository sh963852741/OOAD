package cn.edu.xmu.flashsale.service;

import cn.edu.xmu.flashsale.dao.FlashSaleDao;
import cn.edu.xmu.flashsale.model.bo.FlashSaleItem;
import cn.edu.xmu.flashsale.model.bo.RedisFlash;
import cn.edu.xmu.flashsale.model.bo.TimeSegment;
import cn.edu.xmu.flashsale.model.po.FlashSaleItemPo;
import cn.edu.xmu.flashsale.model.po.FlashSalePo;
import cn.edu.xmu.flashsale.model.vo.FlashSaleItemVo;
import cn.edu.xmu.flashsale.model.vo.FlashSaleRetVo;
import cn.edu.xmu.flashsale.service.dubbo.ITimeSegmentService;
import cn.edu.xmu.goods.client.dubbo.SkuDTO;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
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
import java.util.Map;
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

    @DubboReference(version = "0.0.3-SNAPSHOT")
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
        if(flashDate.minusHours(24).isBefore(LocalDateTime.now())){
            return new ReturnObject(ResponseCode.FIELD_NOTVALID, "只能新增明天之后的活动");
        }

        FlashSalePo flashSalePo = new FlashSalePo();
        flashSalePo.setGmtCreate(LocalDateTime.now());
        flashSalePo.setFlashDate(flashDate);
        flashSalePo.setTimeSegId(timeSegId);

        if(flashSaleDao.addFlashSale(flashSalePo) == 1){
            return new ReturnObject(new FlashSaleRetVo(flashSalePo, time));
        }else{
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    @Transactional
    public ReturnObject delFlashSale(long id){
        FlashSalePo flashSalePo = flashSaleDao.getFlashSale(id);
        if (flashSalePo == null){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST,"秒杀活动不存在");
        }

        redisTemplate.delete("FlashSale" + flashSalePo.getFlashDate().toLocalDate().toString() + flashSalePo.getTimeSegId());

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

    public ReturnObject addSkuToFlashSale(Long flashSaleId, FlashSaleItemVo vo){
        FlashSalePo flashSalePo = flashSaleDao.getFlashSale(flashSaleId);
        if(flashSalePo == null){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "秒杀活动不存在");
        }
        if(flashSalePo.getFlashDate().minusHours(24).isBefore(LocalDateTime.now())){
            // 需要更新缓存
            return new ReturnObject(ResponseCode.FIELD_NOTVALID, "不能增加24小时内的秒杀商品");
        }

        FlashSaleItemPo po = vo.createPo();
        po.setGmtCreate(LocalDateTime.now());
        po.setSaleId(flashSaleId);
        if(flashSaleDao.addFlashSaleItem(po) == 1){
            SkuDTO skuDTO = goodsService.getSku(po.getGoodsSkuId());
            FlashSaleItem flashSaleItem = new FlashSaleItem(po, skuDTO);
            return new ReturnObject(flashSaleItem);
        }else{
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    public ReturnObject removeSkuFromFlashSale(Long flashSaleId, Long flashSaleItemId){
        FlashSalePo flashSalePo = flashSaleDao.getFlashSale(flashSaleId);
        if(flashSalePo == null){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "秒杀活动不存在");
        }
        if(flashSalePo.getFlashDate().minusHours(24).isBefore(LocalDateTime.now())){
            // 需要更新缓存
            redisTemplate.opsForSet().remove(
                    "FlashSale" + flashSalePo.getFlashDate().toLocalDate().toString() + flashSalePo.getTimeSegId(),
                    flashSalePo);
        }

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
                    var dto = goodsService.getSku(((FlashSaleItemPo)x).getGoodsSkuId());
                    return new FlashSaleItem((FlashSaleItemPo) x,dto);
                });
    }

    /* 加载今日秒杀，不允许同时加载 */
    @Override
    public synchronized void afterPropertiesSet() throws Exception {
        List<TimeSegment> timeSegments = timeSegmentService.getFlashSaleSegments();
        Map<Long, TimeSegment> segmentMap = timeSegments.stream().collect(Collectors.toMap(TimeSegment::getId, x -> x));

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
            String setKey ="FlashSale" + flashSalePo.getFlashDate().toLocalDate().toString() + flashSalePo.getTimeSegId();
            String hashKey ="FlashSaleSKU" + flashSalePo.getFlashDate().toLocalDate().toString();
            for(FlashSaleItemPo po:flashSaleItemPos){
                redisTemplate.boundSetOps(setKey).add(po); // 时段与SKU ID对应
                TimeSegment time =segmentMap.get(flashSalePo.getTimeSegId());
                RedisFlash redisFlash = new RedisFlash(po, time.getStartTime(),time.getEndTime());
//                redisTemplate.boundHashOps(hashKey).put(po.getGoodsSkuId(), redisFlash); // SKU ID与秒杀Items对应
            }
        }

        String key ="FlashSale" + LocalDate.now().toString();
        redisTemplate.expire(key,24, TimeUnit.HOURS);
    }
}

