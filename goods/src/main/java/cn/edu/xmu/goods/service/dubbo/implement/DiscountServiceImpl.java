package cn.edu.xmu.goods.service.dubbo.imp;

import cn.edu.xmu.goods.model.vo.CouponActivityVo;
import cn.edu.xmu.goods.service.ActivityService;
import cn.edu.xmu.ooad.order.discount.BaseCouponDiscount;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.xmu.edu.goods.client.IDiscountService;
import cn.xmu.edu.goods.client.dubbo.OrderItem;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DubboService
public class DiscountServiceImpl implements IDiscountService {

    @Autowired
    private ActivityService activityService;

    private List<cn.edu.xmu.ooad.order.bo.OrderItem> adapter(List<OrderItem> items){
        List<cn.edu.xmu.ooad.order.bo.OrderItem> data=new ArrayList<>();
        for(OrderItem item: items){
            cn.edu.xmu.ooad.order.bo.OrderItem temp=new cn.edu.xmu.ooad.order.bo.OrderItem();
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
    public Map<Long, Long> calcDiscount(List<OrderItem> orderItems) {
        Map<Long, List<OrderItem>> classifyData = new HashMap<>();
        //将orderItem根据优惠活动id分类
        for(OrderItem orderItem : orderItems){
            if(classifyData.containsKey(orderItem.getCouponActivityId())){
                classifyData.get(orderItem.getCouponActivityId()).add(orderItem);
            }else{
                List<OrderItem> list = new ArrayList<>();
                list.add(orderItem);
                classifyData.put(orderItem.getCouponActivityId(), list);
            }
        }
        Map<Long, Long> retData=new HashMap<>();
        for(Map.Entry<Long, List<OrderItem>> entry : classifyData.entrySet()){
            if(entry.getValue().size() == 0){
                continue;
            }
            ReturnObject<CouponActivityVo> ret=activityService.getCouponActivity(entry.getValue().get(0).getCouponActivityId(),0);
            if(ret.getCode() != ResponseCode.OK){
                for(OrderItem item : entry.getValue()){
                    retData.put(item.getSkuId(),item.getPrice());
                }
            }
            try {
                BaseCouponDiscount baseCouponDiscount = BaseCouponDiscount.getInstance(ret.getData().getStrategy());
                List<cn.edu.xmu.ooad.order.bo.OrderItem> computedLists=baseCouponDiscount.compute(adapter(entry.getValue()));
                for(cn.edu.xmu.ooad.order.bo.OrderItem item : computedLists){
                    retData.put(item.getSkuId(),item.getDiscount());
                }
            }catch (Exception e){
                for(OrderItem item : entry.getValue()){
                    retData.put(item.getSkuId(),item.getPrice());
                }
            }
        }
        return retData;
    }
}
