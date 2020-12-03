package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.FlashSaleItemPo;
import cn.edu.xmu.goods.model.po.SKUPo;
import cn.edu.xmu.goods.model.vo.FlashSaleItemRetVo;
import cn.edu.xmu.goods.model.vo.ProductRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品规格
 * @author Ming Qiu
 **/
@Data
public  class FlashSaleItem implements VoObject, Serializable  {

    private Long id;

    private Long saleId;

    private Long price;

    private Integer quantity;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private Product product;

    public FlashSaleItem() {
        super();
    }

    public FlashSaleItem(FlashSaleItemPo itemPo, SKUPo skuPo) {
        this.id = skuPo.getId();
        this.product = new Product(skuPo);
        this.price = itemPo.getPrice();
        this.saleId = itemPo.getSaleId();
        this.quantity = itemPo.getQuantity();
        this.gmtCreate = itemPo.getGmtCreate();
        this.gmtModified = itemPo.getGmtModified();
    }

    @Override
    public Object createVo() {
        FlashSaleItemRetVo retVo = new FlashSaleItemRetVo();
        retVo.setId(this.id);
        ProductRetVo productRetVo = (ProductRetVo) product.createVo();
        productRetVo.setPrice(this.price);
        productRetVo.setInventory(this.quantity);
        retVo.setGoodsSku(productRetVo);
        retVo.setPrice(this.price);
        retVo.setQuantity(this.quantity);
        retVo.setGmtCreate(this.gmtCreate);
        retVo.setGmtModified(this.gmtModified);
        return retVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
