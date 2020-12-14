package cn.edu.xmu.activity.model.vo;

import cn.edu.xmu.activity.model.po.CouponActivityPo;
import cn.edu.xmu.activity.model.po.CouponPo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

@Data
public class SingleCouponVo implements VoObject {
    private Long id;
    private ActivityInCouponVo activity;
    private String name;
    private String couponSn;

    public SingleCouponVo(CouponActivityPo couponActivityPo, CouponPo couponPo){
        id = couponPo.getId();
        name = couponPo.getName();
        couponSn = couponPo.getCouponSn();
        activity = new ActivityInCouponVo(couponActivityPo);
    }

    @Override
    public Object createVo() {
        return this;
    }

    @Override
    public Object createSimpleVo() {
        return this;
    }
}
