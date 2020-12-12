package cn.edu.xmu.flashsale.model.bo;

import cn.edu.xmu.flashsale.model.po.FlashSaleItemPo;
import cn.edu.xmu.flashsale.model.po.FlashSalePo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisFlash {
    private Long id;
    private Long skuId;
    private Long activityId;
    private Long price;
    private Integer quantity;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private LocalTime beginTime;
    private LocalTime endTime;

    public RedisFlash(FlashSaleItemPo flashSaleItemPo, LocalTime beginTime, LocalTime endTime, FlashSalePo flashSalePo){
        id = flashSaleItemPo.getId();
        skuId = flashSaleItemPo.getGoodsSkuId();
        price = flashSaleItemPo.getPrice();
        quantity = flashSaleItemPo.getQuantity();
        gmtCreate = flashSaleItemPo.getGmtCreate();
        gmtModified =flashSaleItemPo.getGmtModified();
        activityId = flashSalePo.getId();
        this.beginTime = beginTime;
        this.endTime = endTime;
    }
}
