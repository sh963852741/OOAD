package cn.edu.xmu.goods.service.dubbo.imp;

import cn.edu.xmu.goods.model.bo.dubbo.OrderItem;
import cn.edu.xmu.goods.model.bo.dubbo.Shop;
import cn.edu.xmu.goods.model.vo.SkuRetVo;
import cn.edu.xmu.goods.service.GoodsService;
import cn.edu.xmu.goods.service.ShopService;
import cn.edu.xmu.goods.service.dubbo.IGoodsService;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.apache.dubbo.config.annotation.DubboService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@DubboService(version = "0.0.1-SNAPSHOT")
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    private ShopService shopService;

    @Autowired
    private GoodsService goodsService;

    @Override
    public Long getPrice(Long skuId) {
        if(skuId == null){
            return null;
        }
        ReturnObject<SkuRetVo> ret = goodsService.getSkuDetails(skuId);
        if(ret.getCode()!= ResponseCode.OK){
            return null;
        }
        return ret.getData().getOriginalPrice();
    }

    @Override
    public Map<Shop, List<OrderItem>> classifySku(List<OrderItem> orderItems) {
        Map<Long,List<OrderItem>> temp = new HashMap<Long,List<OrderItem>>();
        for(OrderItem orderItem : orderItems){
            ReturnObject<Long> ret = goodsService.getShopIdBySkuId(orderItem.getId());
            if(ret.getCode() != ResponseCode.OK){
                return null;
            }
            Long shopId = ret.getData();
            if(temp.containsKey(ret.getData())){
                temp.get(shopId).add(orderItem);
            }else{
                List<OrderItem> orderItemList=new ArrayList<>();
                orderItemList.add(orderItem);
                temp.put(shopId,orderItemList);
            }
        }
        Map<Shop,List<OrderItem>> ret = new HashMap<>();
        for (Map.Entry<Long, List<OrderItem>> entry : temp.entrySet()) {
            Shop shop = new Shop(entry.getKey());
            ret.put(shop,entry.getValue());
        }
        return ret;
    }

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
