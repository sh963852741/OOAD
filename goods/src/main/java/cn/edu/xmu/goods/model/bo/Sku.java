package cn.edu.xmu.goods.model.bo;


import cn.edu.xmu.goods.model.po.SKUPo;
import cn.edu.xmu.goods.model.vo.SkuSimpleRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;
import java.util.List;
import java.time.LocalDateTime;
import java.util.ListIterator;

@Data
public class Sku implements VoObject {

    private Long id;

    private String name;

    private String detail;

    private String skuSn;

    private String imageUrl;

    private Long inventory;

    private Long originalPrice;

    private Long price;

    private String configuration;

    private Long weight;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private SimpleSpu spu;

    private Byte disable;

    @Override
    public Object createVo() {
        return null;
    }

    @Override
    public SkuSimpleRetVo createSimpleVo() {
        SkuSimpleRetVo ret=new SkuSimpleRetVo();
        ret.setId(this.getId());
        ret.setImageUrl(this.getImageUrl());
        ret.setDisable(this.getDisable());
        ret.setInventory(this.getInventory());
        ret.setName(this.getName());
        ret.setOriginalPrice(this.getOriginalPrice());
        ret.setPrice(this.getPrice());
        ret.setSkuSn(this.getSkuSn());
        return ret;
    }

    public Sku(SKUPo skuPo){
        this.setDisable(skuPo.getDisabled());
        this.setId(skuPo.getId());
        this.setImageUrl(skuPo.getImageUrl());
        this.setInventory((long)skuPo.getInventory());
        this.setName(skuPo.getName());
        this.setOriginalPrice(skuPo.getOriginalPrice());
        this.setSkuSn(skuPo.getSkuSn());
    }
}

class SimpleSpu{

    private Long id;
    private String name;

}


