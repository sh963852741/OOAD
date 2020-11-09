package cn.edu.xmu.ooad.model.vo;

import lombok.Data;

@Data
public class CouponVo {

    String name;
    Long quantity;
    Long quantityType;
    Long validTerm;
    String couponTime;
    String beginTime;
    String endTime;
    String strategy;

}
