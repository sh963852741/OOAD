package cn.edu.xmu.flashsale.service;

import cn.edu.xmu.flashsale.dao.FlashSaleDao;
import cn.edu.xmu.flashsale.model.FlashState;
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

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import cn.edu.xmu.goods.client.IGoodsService;
import reactor.core.publisher.Mono;

/**
 * @author Ming Qiu
 * @date Created in 2020/11/21 15:00
 **/
@Service
@Slf4j
public class FlashSaleService implements InitializingBean {

    @Value("${flashsale.loadtime}")
    private Integer loadTime;

    @Autowired
    private ReactiveRedisTemplate<String, Serializable> reactiveRedisTemplate;
    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @DubboReference(version = "0.0.1-SNAPSHOT",check = false)
    IGoodsService goodsService;
    @Autowired
    ITimeSegmentService timeSegmentService;
    @Autowired
    FlashSaleDao flashSaleDao;

    public ReturnObject onlineFlashSale(Long id){
        FlashSalePo flashSalePo = flashSaleDao.getFlashSale(id);
        if (flashSalePo == null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,"秒杀活动不存在");
        }
        if(!flashSalePo.getState().equals(FlashState.FalshStatus.OFFLINE.getCode())){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "仅可上线下线的秒杀活动");
        }

        if(flashSaleDao.setFlashSaleStatus(id, FlashState.FalshStatus.ONLINE.getCode())==1){
            return new ReturnObject<>();
        } else {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    public ReturnObject offlineFlashSale(Long id){
        FlashSalePo flashSalePo = flashSaleDao.getFlashSale(id);
        if (flashSalePo == null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,"秒杀活动不存在");
        }
        if(!flashSalePo.getState().equals(FlashState.FalshStatus.ONLINE.getCode())){
            return new ReturnObject<>(ResponseCode.DELETE_CHANGAE_NOTALLOW, "仅可上线下线的秒杀活动");
        }

        redisTemplate.delete("FlashSale" + flashSalePo.getFlashDate().toLocalDate().toString() + flashSalePo.getTimeSegId());

        if(flashSaleDao.setFlashSaleStatus(id, FlashState.FalshStatus.OFFLINE.getCode())==1){
            return new ReturnObject<>();
        } else {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    public ReturnObject addFlashSale(long timeSegId, LocalDateTime flashDate){
        var time = timeSegmentService.getTimeSegmentById(timeSegId);
        if(time == null){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "时间段不存在");
        }
        if(flashDate.minusHours(24).isBefore(LocalDateTime.now())){
            return new ReturnObject(ResponseCode.FIELD_NOTVALID, "只能新增明天之后的活动");
        }

        if(flashSaleDao.hasSameFlashSale(timeSegId, flashDate)){
            return new ReturnObject(ResponseCode.TIMESEG_CONFLICT);
        }

        FlashSalePo flashSalePo = new FlashSalePo();
        flashSalePo.setGmtCreate(LocalDateTime.now());
        flashSalePo.setFlashDate(flashDate);
        flashSalePo.setTimeSegId(timeSegId);
        flashSalePo.setState(FlashState.FalshStatus.OFFLINE.getCode());

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
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if (flashSalePo.getState().equals(FlashState.FalshStatus.DELETE.getCode())){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if (!flashSalePo.getState().equals(FlashState.FalshStatus.OFFLINE.getCode())){
            return new ReturnObject(ResponseCode.ACTIVITYALTER_INVALID);
        }

        redisTemplate.delete("FlashSale" + flashSalePo.getFlashDate().toLocalDate().toString() + flashSalePo.getTimeSegId());

//        flashSaleDao.delFlashSaleItemByFlashSaleId(id);
        if(flashSaleDao.setFlashSaleStatus(id, FlashState.FalshStatus.DELETE.getCode())==1){
            return new ReturnObject();
        }else{
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "秒杀活动不存在");
        }
    }

    public ReturnObject modifyFlashSale(long id, LocalDateTime date){
        var flashSale = flashSaleDao.getFlashSale(id);
        if (flashSale == null || !flashSale.getState().equals(FlashState.FalshStatus.OFFLINE.getCode())){
            return new ReturnObject(ResponseCode.ACTIVITYALTER_INVALID, "状态错误");
        }
        if(flashSale.getFlashDate().isBefore(LocalDateTime.now())){
            return new ReturnObject(ResponseCode.ACTIVITYALTER_INVALID);
        }
        if(date.minusHours(24).isBefore(LocalDateTime.now())){
            return new ReturnObject(ResponseCode.FIELD_NOTVALID, "不能修改24小时内开始的活动");
        }
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
        po.setGoodsSkuId(vo.getSkuId());
        SkuDTO skuDTO = goodsService.getSku(po.getGoodsSkuId());
        if(skuDTO == null){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(flashSaleDao.addFlashSaleItem(po) == 1){
            FlashSaleItem flashSaleItem = new FlashSaleItem(po, skuDTO);
            log.debug("flashSaleItem" + flashSaleItem.toString());
            return new ReturnObject(flashSaleItem);
        }else{
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    public ReturnObject removeSkuFromFlashSale(Long flashSaleId, Long flashSaleItemId){
        FlashSalePo flashSalePo = flashSaleDao.getFlashSale(flashSaleId);
        log.debug("删除缓存中……" + flashSaleItemId);
        FlashSaleItemPo flashSaleItemPo = flashSaleDao.getFlashSaleItemByPrimaryKey(flashSaleItemId);
        if(flashSalePo == null){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "秒杀活动不存在");
        }
        if(flashSalePo.getFlashDate().minusHours(24).isBefore(LocalDateTime.now())){
            // 需要更新缓存
            log.debug("删除缓存中……" + flashSaleItemPo.getGoodsSkuId().toString());
            redisTemplate.opsForSet().remove(
                    "FlashSale" + flashSalePo.getFlashDate().toLocalDate().toString() + flashSalePo.getTimeSegId(),
                    flashSaleItemPo.getGoodsSkuId().toString());
        }

        if(flashSaleDao.deleteFlashSaleItem(flashSaleItemId) == 1){
            return new ReturnObject();
        }else{
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    public Mono<Object> getFlashSale(Long timeSegId) {
        String setKey ="FlashSale" + LocalDate.now().toString() + timeSegId.toString();
        String hashKey ="FlashSaleSKU" + LocalDate.now().toString();
        log.debug("SetKey: " + setKey);
        log.debug("HashKey: " + hashKey);

        var keys = redisTemplate.opsForSet().members(setKey);
        if(keys == null || keys.isEmpty()){
            return Mono.just(new ArrayList<>());
        }
        Set<Object> skus = keys.stream().map(Object::toString).collect(Collectors.toSet());
        log.debug("skus:" + skus);
        if(skus.isEmpty()){
            return Mono.just(new ArrayList<>());
        }
        return reactiveRedisTemplate.opsForHash().multiGet(hashKey, skus).map(y ->{
            List<FlashSaleItem> ret = new ArrayList<>();
            log.debug("y:" + ((List)y).toString());
            for(Object r:y){
                var dto = goodsService.getSku(((RedisFlash)r).getSkuId());
                log.debug("SkuDTO:" + dto.toString());
                ret.add(new FlashSaleItem((RedisFlash)r,dto));
            }
            return ret;
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
        log.debug("TimeSegmentsForToday:" + timeSegmentsForToday.toString());
        log.debug("TimeSegmentsForTomorrow:" + timeSegmentsForTomorrow.toString());
        log.debug(timeSegments.toString());
        /* 获取了所有的秒杀活动 */
        List<FlashSalePo> effectFlashSaleToLoad = new ArrayList<>();
        effectFlashSaleToLoad.addAll(flashSaleDao.selectFlashSaleByTimeAndDate(
                LocalDate.now(),
                timeSegmentsForToday.stream().map(TimeSegment::getId).collect(Collectors.toList())
        ));
        if(!timeSegmentsForTomorrow.isEmpty()){
            effectFlashSaleToLoad.addAll(flashSaleDao.selectFlashSaleByTimeAndDate(
                    LocalDate.now().plusDays(1),
                    timeSegmentsForTomorrow.stream().map(TimeSegment::getId).collect(Collectors.toList())
            ));
        }
        log.debug("EffectFlashSaleToLoad:" + effectFlashSaleToLoad.toString());
        for (FlashSalePo flashSalePo : effectFlashSaleToLoad) {
            // 忽略不是上线状态的秒杀
            if(!flashSalePo.getState().equals(FlashState.FalshStatus.ONLINE.getCode()))continue;

            List<FlashSaleItemPo> flashSaleItemPos = flashSaleDao.getFlashSaleItemByFlashSaleId(flashSalePo.getId());
            String setKey ="FlashSale" + flashSalePo.getFlashDate().toLocalDate().toString() + flashSalePo.getTimeSegId();
            String hashKey ="FlashSaleSKU" + flashSalePo.getFlashDate().toLocalDate().toString();
            for(FlashSaleItemPo po:flashSaleItemPos){
                redisTemplate.boundSetOps(setKey).add(po.getGoodsSkuId().toString()); // 时段与SKU ID对应
                TimeSegment time =segmentMap.get(flashSalePo.getTimeSegId());
                RedisFlash redisFlash = new RedisFlash(po, time.getStartTime(),time.getEndTime(),flashSalePo);
                redisTemplate.boundHashOps(hashKey).put(po.getGoodsSkuId().toString(), redisFlash); // SKU ID与秒杀Items对应
            }
        }

        String key ="FlashSale" + LocalDate.now().toString();
        redisTemplate.expire(key,24, TimeUnit.HOURS);
    }

    private int updateQuantity(Long skuId){
        return 0;
    }
}

