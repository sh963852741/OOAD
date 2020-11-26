package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Shop;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ShopVo {
    @NotBlank(message = "商铺名不能为空")
    private String name;
    private long id;
    private byte state;
//    public Shop createShop() {
//        Shop shop = new Shop();
//        shop.setId(this.id);
//        shop.setName(this.name);
//        shop.setState(this.state);
//        return shop;
//    }
}
