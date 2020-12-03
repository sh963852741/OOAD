package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "浮动价格视图")
public class FloatPriceVo {

    @Min(0)
    @ApiModelProperty(value = "Sku生效价格")
    @NotNull
    Long activityPrice;

    @ApiModelProperty(value = "生效开始时间")
    @NotBlank
    String beginTime;

    @ApiModelProperty(value = "生效结束时间")
    @NotBlank
    String endTime;

    @Min(0)
    @ApiModelProperty(value = "对应数量")
    @NotNull
    Long quantity;

}
