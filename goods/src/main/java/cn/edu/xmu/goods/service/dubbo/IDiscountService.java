package cn.edu.xmu.goods.service.dubbo;

import cn.edu.xmu.goods.model.bo.dubbo.OrderItem;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author xincong yao
 * @date 2020-11-17
 */
public interface IDiscountService {

	/**
	 * 计算折扣
	 * @param orderItems
	 * @return skuId到discount的映射
	 */
	Map<Long, Long> calcDiscount(List<OrderItem> orderItems);

}
