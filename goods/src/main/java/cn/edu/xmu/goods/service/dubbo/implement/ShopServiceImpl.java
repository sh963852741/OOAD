package cn.edu.xmu.goods.service.dubbo.implement;

import cn.edu.xmu.goods.service.GoodsService;
import cn.edu.xmu.goods.service.ShopService;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.xmu.edu.goodscilent.IShopService;
import cn.xmu.edu.goodscilent.dubbo.Shop;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class ShopServiceImpl implements IShopService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ShopService shopService;

    @Override
    public Shop getShop(Long skuId) {
        if(skuId == null){
            return new Shop();
        }
        ReturnObject shopIdRet=goodsService.getShopIdBySkuId(skuId);
        if(shopIdRet.getCode() != ResponseCode.OK){
            return new Shop();
        }
        Long shopId=(Long)shopIdRet.getData();
        ReturnObject shopRet=shopService.getShopByShopId(shopId);
        if(shopRet.getCode() != ResponseCode.OK){
            return new Shop();
        }
        cn.edu.xmu.goods.model.bo.Shop shop=(cn.edu.xmu.goods.model.bo.Shop) shopRet.getData();
        Shop shopData=new Shop(shop.getId(),shop.getName(),shop.getGmtCreated().toString(),shop.getGmtModified().toString());
        return shopData;
    }
}
