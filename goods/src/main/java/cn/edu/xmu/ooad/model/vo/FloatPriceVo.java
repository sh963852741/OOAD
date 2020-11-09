package cn.edu.xmu.ooad.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@ApiModel(value = "浮动价格视图")
public class FloatPriceVo {

    @Min(0)
    @ApiModelProperty(value = "Sku生效价格")
    Long activityPrice;

    @ApiModelProperty(value = "生效开始时间")
    String beginTime;

    @ApiModelProperty(value = "生效结束时间")
    String endTime;

    @Min(0)
    @ApiModelProperty(value = "对应数量")
    Long quantity;

}
