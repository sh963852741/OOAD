package cn.edu.xmu.flashsale.model.vo;

import cn.edu.xmu.flashsale.model.po.FlashSaleItemPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "秒杀活动")
public class FlashSaleItemVo {

    @ApiModelProperty(value = "秒杀的skuId")
    private Long skuId;

    @ApiModelProperty(value = "sku秒杀价格")
    private Long price;

    @ApiModelProperty(value = "sku秒杀数量")
    private Integer quantity;

    public FlashSaleItemPo createPo(){
        FlashSaleItemPo po = new FlashSaleItemPo();
        po.setGoodsSkuId(skuId);
        po.setPrice(price);
        po.setQuantity(quantity);

        return po;
    }
}
