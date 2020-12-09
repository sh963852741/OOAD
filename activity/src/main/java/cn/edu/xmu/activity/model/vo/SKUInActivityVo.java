package cn.edu.xmu.activity.model.vo;

import cn.edu.xmu.goods.client.dubbo.SkuDTO;
import cn.edu.xmu.ooad.model.VoObject;
import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class SKUInActivityVo implements VoObject {
    private Long id;
    private String name;
    private String skuSn;
    private String imgUrl;
    private Integer inventory;
    private Long originalPrice;
    private Long price;
    private Boolean disable;

    public SKUInActivityVo(SkuDTO dto){
        id = dto.getId();
        name = dto.getName();
        skuSn = dto.getSkuSn();
        imgUrl = dto.getImageUrl();
        inventory = dto.getInventory();
        originalPrice = dto.getOriginalPrice();
        price = dto.getPrice();
        disable = getDisable();
    }

    @Override
    public Object createVo() {
        return this;
    }

    @Override
    public Object createSimpleVo() {
        return this;
    }
}
