package cn.edu.xmu.flashsale.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "秒杀活动")
public class FlashsaleVo {

    @ApiModelProperty(value = "秒杀的skuId")
    private Long skuId;

    @ApiModelProperty(value = "sku秒杀价格")
    private Long price;

    @ApiModelProperty(value = "sku秒杀数量")
    private Long quantity;


}
