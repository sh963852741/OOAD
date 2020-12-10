package cn.edu.xmu.flashsale.model.bo;

import cn.edu.xmu.flashsale.model.po.FlashSaleItemPo;
import cn.edu.xmu.flashsale.model.po.FlashSalePo;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class RedisFlash {
    private Long id;
    private Long price;
    private Integer quantity;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private LocalTime beginTime;
    private LocalTime endTime;

    public RedisFlash(FlashSaleItemPo flashSaleItemPo, LocalTime beginTime, LocalTime endTime){
        id = flashSaleItemPo.getId();
        price = flashSaleItemPo.getPrice();
        quantity = flashSaleItemPo.getQuantity();
        gmtCreate = flashSaleItemPo.getGmtCreate();
        gmtModified =flashSaleItemPo.getGmtModified();
        this.beginTime = beginTime;
        this.endTime = endTime;
    }
}
