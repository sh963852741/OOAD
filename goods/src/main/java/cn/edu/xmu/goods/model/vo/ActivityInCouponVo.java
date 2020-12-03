package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.po.CouponActivityPo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ActivityInCouponVo {
    private Long id;
    private String name;
    private String imageUrl;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private LocalDateTime couponTime;
    private Integer quantity;

    public ActivityInCouponVo(CouponActivityPo po){
        id = po.getId();
        name = po.getName();
        imageUrl =po.getImageUrl();
        beginTime =po.getBeginTime();
        endTime =po.getEndTime();
        couponTime =po.getCouponTime();
        quantity = po.getQuantity();
    }
}
