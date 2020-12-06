package cn.xmu.edu.goodscilent;



import cn.xmu.edu.goodscilent.dubbo.OrderItem;
import cn.xmu.edu.goodscilent.dubbo.Shop;
import cn.xmu.edu.goodscilent.dubbo.Sku;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yifei Wang
 * @Date: 2020/12/6 10:49
 */
public interface IGoodsService {

	/**
	 * 获取价格
	 * @param skuId
	 * @return
	 */
	Long getPrice(Long skuId);

	/**
	 * 根据sku所属商铺划分orderItem
	 * 返回的OrderItem需要skuId, name, quantity, price
	 * @param orderItems
	 * @return
	 */
	Map<Shop, List<OrderItem>> classifySku(List<OrderItem> orderItems);

	/**
	 * 功能描述: 根据skuId获取sku  没有查询到则返回一个sku 里面全部为空
	 * @Param:
	 * @Return:
	 * @Author: Yifei Wang
	 * @Date: 2020/12/6 14:46
	 */
	Sku getSku(Long skuId);
}
