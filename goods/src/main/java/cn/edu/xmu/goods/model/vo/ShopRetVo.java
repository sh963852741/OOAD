package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Shop;
import lombok.Data;

@Data
public class ShopRetVo {
    private Long id;
    private String name;
    private byte state;
    public ShopRetVo(Shop shop) {
        this.id = shop.getId();
        this.name = shop.getName();
        this.state=shop.getState();
    }
}
