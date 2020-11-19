package cn.edu.xmu.ooad.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "优惠活动")
public class CouponVo {

    @ApiModelProperty(value = "优惠名称")
    String name;

    //暂时不懂是什么意思
    Long quantity;
    Long quantityType;
    Long validTerm;
    String couponTime;
    String beginTime;
    String endTime;

    @ApiModelProperty(value = "优惠规则")
    String strategy;

}
