package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.po.CouponPo;
import lombok.Data;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;

@Data
public class CouponVo {
    private Long id;
    private Long customerId;
    private String name;
    private String couponSn;
    private Byte state;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private ActivityInCouponVo activity;

    public CouponVo(CouponPo po, ActivityInCouponVo a){
        id = po.getId();
        customerId = po.getCustomerId();
        name = po.getName();
        couponSn = po.getCouponSn();
        state = po.getState();
        beginTime = po.getBeginTime();
        endTime = po.getEndTime();
        gmtCreate = po.getGmtCreate();
        gmtModified = po.getGmtModified();
        activity = a;
    }
}
