package cn.edu.xmu.activity.service.dubbo;

import cn.edu.xmu.activity.model.vo.CouponActivityVo;
import cn.edu.xmu.activity.service.ActivityService;
import cn.edu.xmu.ooad.order.bo.OrderItem;
import cn.edu.xmu.ooad.order.discount.BaseCouponDiscount;
import cn.xmu.edu.goods.client.dubbo.*;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.xmu.edu.goods.client.IDiscountService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DubboService(version = "0.0.1-SNAPSHOT")
public class DiscountServiceImpl implements IDiscountService {

    @Autowired
    private ActivityService activityService;

    private List<OrderItem> adapter(List<OrderItemDTO> items){
        List<OrderItem> data=new ArrayList<>();
        for(OrderItemDTO item: items){
            OrderItem temp = new OrderItem();
            temp.setId(item.getId());
            temp.setBeShareId(item.getBeShareId());
            temp.setCouponActivityId(item.getCouponActivityId());
            temp.setDiscount(item.getDiscount());
            temp.setName(item.getName());
            temp.setOrderId(item.getOrderId());
            temp.setPrice(item.getPrice());
            temp.setQuantity(item.getQuantity());
            temp.setSkuId(item.getSkuId());
        }
        return data;
    }


    @Override
    public Map<Long, Long> calcDiscount(List<OrderItemDTO> orderItemDTOS) {
        Map<Long, List<OrderItemDTO>> classifyData = new HashMap<>();
        //将orderItem根据优惠活动id分类
        for(OrderItemDTO orderItemDTO : orderItemDTOS){
            if(classifyData.containsKey(orderItemDTO.getCouponActivityId())){
                classifyData.get(orderItemDTO.getCouponActivityId()).add(orderItemDTO);
            }else{
                List<OrderItemDTO> list = new ArrayList<>();
                list.add(orderItemDTO);
                classifyData.put(orderItemDTO.getCouponActivityId(), list);
            }
        }
        Map<Long, Long> retData=new HashMap<>();
        for(Map.Entry<Long, List<OrderItemDTO>> entry : classifyData.entrySet()){
            if(entry.getValue().size() == 0){
                continue;
            }
            ReturnObject<CouponActivityVo> ret=activityService.getCouponActivity(entry.getValue().get(0).getCouponActivityId(),0);
            if(ret.getCode() != ResponseCode.OK){
                for(OrderItemDTO item : entry.getValue()){
                    retData.put(item.getSkuId(),item.getPrice());
                }
            }
            try {
                BaseCouponDiscount baseCouponDiscount = BaseCouponDiscount.getInstance(ret.getData().getStrategy());
                List<OrderItem> computedLists=baseCouponDiscount.compute(adapter(entry.getValue()));
                for(OrderItem item : computedLists){
                    retData.put(item.getSkuId(),item.getDiscount());
                }
            }catch (Exception e){
                for(OrderItemDTO item : entry.getValue()){
                    retData.put(item.getSkuId(),item.getPrice());
                }
            }
        }
        return retData;
    }
}
