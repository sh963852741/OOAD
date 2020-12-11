package cn.edu.xmu.activity.service.dubbo;

import cn.edu.xmu.activity.dao.CouponActivityDao;
import cn.edu.xmu.activity.dao.CouponDao;
import cn.edu.xmu.activity.dao.GrouponActivityDao;
import cn.edu.xmu.activity.dao.PresaleActivityDao;
import cn.edu.xmu.activity.model.Timeline;
import cn.edu.xmu.activity.model.bo.Coupon;
import cn.edu.xmu.activity.model.po.CouponPo;
import cn.edu.xmu.activity.model.po.CouponSKUPo;
import cn.edu.xmu.goods.client.IGoodsService;
import cn.edu.xmu.goods.client.dubbo.PriceDTO;
import cn.edu.xmu.goods.client.dubbo.SkuDTO;
import cn.edu.xmu.goods.client.dubbo.SpuDTO;
import cn.edu.xmu.goods.client.dubbo.PriceDTO;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.goods.client.IActivityService;
import cn.edu.xmu.goods.client.dubbo.OrderItemDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.*;

@DubboService(version = "0.0.1-SNAPSHOT")
public class ActivityServiceImpl implements IActivityService {
    @Autowired
    GrouponActivityDao grouponActivityDao;
    @Autowired
    PresaleActivityDao presaleActivityDao;
    @Autowired
    CouponActivityDao couponActivityDao;
    @Autowired
    CouponDao couponDao;
    @Autowired
    IGoodsService goodsService;

    @Override
    public Map<Long, Long> validateActivity(List<OrderItemDTO> orderItemDTOS, Long couponId){
        // 考虑增加HashMap以进行优惠券和活动的对应
        CouponPo couponPo = couponDao.getCoupon(couponId);
        if(couponPo == null){
            return new HashMap<>();
        }
        Map<Long, Long> map = new HashMap<>();
        // 考虑增加缓存，缓存活动适用的SKU
        Long activityId = couponPo.getActivityId();
        List<CouponSKUPo> couponSPUPoList = couponActivityDao.getSKUsInActivity(activityId);
        Set<Long> spuSet = new HashSet<>();
        for(CouponSKUPo couponSPUPo:couponSPUPoList){
            spuSet.add(couponSPUPo.getSkuId());
        }

        for(OrderItemDTO o: orderItemDTOS){
            if(spuSet.contains(o.getSkuId())){
                map.put(o.getSkuId(), activityId);
            }else{
                map.put(o.getSkuId(), null);
            }

        }

        return map;
    }


    /**
     * 使用指定ID的优惠券，订单模块调用
     * @param couponId
     * @return
     */
    public Boolean useCoupon(Long couponId){
        CouponPo couponToUse = couponDao.getCoupon(couponId);
        if(couponToUse == null){
//            new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "您使用的优惠券不存在");
            return false;
        }
        if(couponToUse.getBeginTime().isAfter(LocalDateTime.now())
                ||couponToUse.getEndTime().isBefore(LocalDateTime.now())){
//            new ReturnObject<>(ResponseCode.COUPONACT_STATENOTALLOW, "优惠券过期或未到使用时间");
            return false;
        }
        if(!couponToUse.getState().equals(Coupon.CouponStatus.NORMAL.getCode())){
            new ReturnObject<>(ResponseCode.COUPONACT_STATENOTALLOW, "优惠券状态不可用");
            return false;
        }


        CouponPo po = new CouponPo();
        po.setId(couponToUse.getId());
        po.setState(Coupon.CouponStatus.USED.getCode());

        if (couponDao.modifyCoupon(po) == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Long getGrouponId(Long skuId) {
        SkuDTO skuDTO = goodsService.getSku(skuId);
        if(skuDTO == null){
            return null;
        }
        SpuDTO spuDTO = goodsService.getSimpleSpuById(skuDTO.getGoodsSpuId());
        long spuId = spuDTO.getId();
        var f = grouponActivityDao.getActivitiesBySPUId(1,1,spuId, (byte)Timeline.RUNNING.ordinal());
        if(f.getList().isEmpty()){
            return null;
        } else {
            return f.getList().get(0).getId();
        }
    }

    @Override
    public Long getPreSale(Long skuId) {
        var f = presaleActivityDao.getActivitiesBySKUId(1,1,skuId,(byte)Timeline.RUNNING.ordinal());
        if(f.getList().isEmpty()){
            return null;
        } else {
            return f.getList().get(0).getId();
        }
    }

    @Override
    public Map<String, Long> getPrePrice(Long skuId) {
        return null;
    }

    @Override
    public List<PriceDTO> getPriceAndName(List<OrderItemDTO> list, Integer type) {
        return null;
    }
}
