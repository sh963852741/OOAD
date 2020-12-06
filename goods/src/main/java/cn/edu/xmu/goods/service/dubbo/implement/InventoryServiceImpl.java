package cn.edu.xmu.goods.service.dubbo.implement;

import cn.edu.xmu.goods.service.GoodsService;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.xmu.edu.goods.client.IInventoryService;
import cn.xmu.edu.goods.client.dubbo.OrderItem;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@DubboService(version = "0.0.1-SNAPSHOT")
public class InventoryServiceImpl implements IInventoryService {

    @Autowired
    private GoodsService goodsService;

    @Override
    public List<OrderItem> modifyInventory(List<OrderItem> orderItems) {
        List<OrderItem> retData = new ArrayList<>();
        if(orderItems == null || orderItems.size() <= 0){
            return  retData;
        }
        for(OrderItem orderItem : orderItems){
            ReturnObject ret=goodsService.changSkuInventory(orderItem.getSkuId(),orderItem.getQuantity());
            if(ret.getCode() == ResponseCode.OK){
                retData.add(orderItem);
            }
        }
        return retData;
    }
}
