package cn.edu.xmu.goods.service.dubbo.implement;

import cn.edu.xmu.goods.service.GoodsService;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.goods.client.IInventoryService;
import cn.edu.xmu.goods.client.dubbo.OrderItemDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@DubboService(version = "0.0.1-SNAPSHOT")
public class InventoryServiceImpl implements IInventoryService {

    @Autowired
    private GoodsService goodsService;

    @Override
    public List<OrderItemDTO> modifyInventory(List<OrderItemDTO> orderItemDTOS) {
        List<OrderItemDTO> retData = new ArrayList<>();
        if(orderItemDTOS == null || orderItemDTOS.size() <= 0){
            return  retData;
        }
        for(OrderItemDTO orderItemDTO : orderItemDTOS){
            ReturnObject ret=goodsService.changSkuInventory(orderItemDTO.getSkuId(), orderItemDTO.getQuantity());
            if(ret.getCode() == ResponseCode.OK){
                retData.add(orderItemDTO);
            }
        }
        log.debug("retData:" + retData);
        return retData;
    }
}
