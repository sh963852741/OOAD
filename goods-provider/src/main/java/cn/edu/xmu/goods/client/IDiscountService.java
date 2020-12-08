package cn.edu.xmu.goods.client;



import cn.edu.xmu.goods.client.dubbo.OrderItemDTO;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yifei Wang
 * @Date: 2020/12/6 10:49
 */
public interface IDiscountService {

	/**
	 * 计算折扣
	 * @param orderItemDTOS
	 * @return skuId到discount的映射
	 */
	Map<Long, Long> calcDiscount(List<OrderItemDTO> orderItemDTOS);

}
