package cn.edu.xmu.goods.service.dubbo.implement;

import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.service.GoodsService;
import cn.edu.xmu.goods.service.ShopService;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.xmu.edu.goods.client.IShopService;
import cn.xmu.edu.goods.client.dubbo.ShopDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService(version = "0.0.1-SNAPSHOT")
public class ShopServiceImpl implements IShopService {

    @Autowired
    private ShopService shopService;

    @Override
    public ShopDTO getShopById(Long shopId) {
        var shop = shopService.getShopByShopId(shopId).getData();
        if(shop == null){
            return null;
        } else {
            return shop.createDTO();
        }
    }
}
