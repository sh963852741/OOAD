package cn.edu.xmu.flashsale.model.bo;

import cn.edu.xmu.flashsale.model.vo.ProductRetVo;
import cn.edu.xmu.goods.client.dubbo.SkuDTO;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.order.discount.JsonSerializable;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Ming Qiu
 * @date Created in 2020/11/22 13:32
 **/
@Data
public class Product implements VoObject, Serializable{
    private Long id;
    private String name;

    private String skuSn;

    private String detail;

    private String imgUrl;

    private Long originalPrice;

    private Integer inventory;

    private Long weight;

    private String configuration;

    private Byte disabled;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public Product(SkuDTO skuDTO) {
        this.id = skuDTO.getId();
        this.name = skuDTO.getName();
        this.skuSn = skuDTO.getSkuSn();
        this.detail = skuDTO.getDetail();
        this.imgUrl = skuDTO.getImageUrl();
        this.originalPrice = skuDTO.getOriginalPrice();
        this.inventory = skuDTO.getInventory();
        this.weight = skuDTO.getWeight();
        this.configuration = skuDTO.getConfiguration();
        this.gmtCreate = skuDTO.getGmtCreate();
        this.gmtModified = skuDTO.getGmtModified();
        this.disabled = skuDTO.getDisable();
    }

    @Override
    public Object createVo() {
        ProductRetVo retVo = new ProductRetVo();
        retVo.setId(this.id);
        retVo.setDisable(this.disabled);
        retVo.setImageUrl(this.imgUrl);
        retVo.setInventory(this.inventory);
        retVo.setName(this.name);
        retVo.setOriginalPrice(this.originalPrice);
        retVo.setSkuSn(this.skuSn);
        return retVo;
    }

    public Product() {
        super();
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
