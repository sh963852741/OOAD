package cn.edu.xmu.goods.service.dubbo.implement;

import cn.edu.xmu.goods.dao.CouponActivityDao;
import cn.edu.xmu.goods.dao.CouponDao;
import cn.edu.xmu.goods.model.bo.Coupon;
import cn.edu.xmu.goods.model.po.CouponPo;
import cn.edu.xmu.goods.model.po.CouponSPUPo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.xmu.edu.goods.client.IActivityService;
import cn.xmu.edu.goods.client.dubbo.OrderItem;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.*;

@DubboService(version = "0.0.1-SNAPSHOT")
public class ActivityServiceImpl implements IActivityService {

    @Autowired
    CouponActivityDao couponActivityDao;
    @Autowired
    CouponDao couponDao;

    @Override
    public Map<Long, Long> validateActivity(List<OrderItem> orderItems, Long couponId){
        // 考虑增加HashMap以进行优惠券和活动的对应
        CouponPo couponPo = couponDao.getCoupon(couponId);
        if(couponPo == null){
            return new HashMap<>();
        }
        Map<Long, Long> map = new HashMap<>();
        // 考虑增加缓存，缓存活动适用的SPU
        Long activityId = couponPo.getActivityId();
        List<CouponSPUPo> couponSPUPoList = couponActivityDao.getSPUsInActivity(activityId);
        Set<Long> spuSet = new HashSet<>();
        for(CouponSPUPo couponSPUPo:couponSPUPoList){
            spuSet.add(couponSPUPo.getSpuId());
        }

        for(OrderItem o:orderItems){
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
        if(couponToUse.getBeginTime().isAfter(LocalDateTime.now())
                ||couponToUse.getEndTime().isBefore(LocalDateTime.now())){
            new ReturnObject(ResponseCode.COUPONACT_STATENOTALLOW, "优惠券过期或未到使用时间");
        }
        if(couponToUse.getState() != Coupon.CouponStatus.NORMAL.getCode()){
            new ReturnObject(ResponseCode.COUPONACT_STATENOTALLOW, "优惠券状态不可用");
        }
        if(couponToUse == null){
            new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "您使用的优惠券不存在");
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
}
