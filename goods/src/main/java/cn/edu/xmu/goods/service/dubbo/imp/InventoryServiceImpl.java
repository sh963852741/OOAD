package cn.edu.xmu.goods.service.dubbo.imp;

import cn.edu.xmu.goods.model.bo.dubbo.OrderItem;
import cn.edu.xmu.goods.service.GoodsService;
import cn.edu.xmu.goods.service.dubbo.IInventoryService;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.apache.dubbo.config.annotation.DubboService;
import org.aspectj.weaver.ast.Or;
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
