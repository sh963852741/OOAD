package cn.edu.xmu.goods.service.dubbo.implement;

import cn.edu.xmu.goods.service.ShopService;
import cn.edu.xmu.goods.client.IShopService;
import cn.edu.xmu.goods.client.dubbo.ShopDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@DubboService(version = "0.0.1-SNAPSHOT")
public class ShopServiceImpl implements IShopService {

    @Autowired
    private ShopService shopService;

    @Override
    public ShopDTO getShopById(Long shopId) {
        log.debug("Method getShopById(IShopService) was invoked;");
        var shop = shopService.getShopByShopId(shopId).getData();
        if(shop == null){
            return null;
        } else {
            return shop.createDTO();
        }
    }
}
