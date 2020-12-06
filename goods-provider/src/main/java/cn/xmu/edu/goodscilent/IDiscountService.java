package cn.xmu.edu.goodscilent;



import cn.xmu.edu.goodscilent.dubbo.OrderItem;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yifei Wang
 * @Date: 2020/12/6 10:49
 */
public interface IDiscountService {

	/**
	 * 计算折扣
	 * @param orderItems
	 * @return skuId到discount的映射
	 */
	Map<Long, Long> calcDiscount(List<OrderItem> orderItems);

}
