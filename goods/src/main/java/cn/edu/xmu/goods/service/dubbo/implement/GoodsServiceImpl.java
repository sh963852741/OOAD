package cn.edu.xmu.goods.service.dubbo.implement;


import cn.edu.xmu.goods.dao.GoodsDao;
import cn.edu.xmu.goods.model.po.SKUPo;
import cn.edu.xmu.goods.model.vo.SkuRetVo;
import cn.edu.xmu.goods.service.GoodsService;
import cn.xmu.edu.goods.client.IGoodsService;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.xmu.edu.goods.client.dubbo.OrderItem;
import cn.xmu.edu.goods.client.dubbo.Shop;
import cn.xmu.edu.goods.client.dubbo.Sku;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@DubboService(version = "0.0.1-SNAPSHOT")
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsDao goodsDao;

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
    public Sku getSku(Long skuId) {
        ReturnObject<SKUPo> skuRet=goodsDao.getSkuById(skuId);
        if(skuRet.getCode()!=ResponseCode.OK){
            return new Sku();
        }
        Sku sku=new Sku();
        SKUPo skuPo=skuRet.getData();
        sku.setConfiguration(skuPo.getConfiguration());
        sku.setDisable(skuPo.getDisabled());
        sku.setId(skuPo.getId());
        sku.setImageUrl(skuPo.getImageUrl());
        sku.setInventory(skuPo.getInventory());
        sku.setName(skuPo.getName());
        sku.setOriginalPrice(skuPo.getOriginalPrice());
        sku.setSkuSn(skuPo.getSkuSn());
        sku.setWeight(skuPo.getWeight());
        sku.setGmtCreate(skuPo.getGmtCreate());
        sku.setGmtModified(skuPo.getGmtModified());
        sku.setDetail(skuPo.getDetail());
        sku.setGoodsSpuId(skuPo.getGoodsSpuId());
        return sku;
    }


}
