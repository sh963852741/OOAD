package cn.xmu.edu.goodscilent;

import cn.xmu.edu.goodscilent.dubbo.OrderItem;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yifei Wang
 * @Date: 2020/12/6 10:49
 */
public interface IActivityService {

	/**
	 * 校验sku是否适用与给定的优惠活动
	 * 以及优惠卷是否可用
	 * @param orderItems
	 * @param couponId
	 * @return skuId到couponActivityId的映射
	 */
	Map<Long, Long> validateActivity(List<OrderItem> orderItems, Long couponId);

	/**
	 * 使用优惠卷
	 * @param couponId
	 * @return
	 */
	Boolean useCoupon(Long couponId);
}
