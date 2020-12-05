package cn.edu.xmu.goods.service.dubbo;

import cn.edu.xmu.goods.model.bo.dubbo.OrderItem;
import cn.edu.xmu.goods.model.bo.dubbo.Shop;

import java.util.List;
import java.util.Map;

/**
 * @author xincong yao
 * @date 2020-11-28
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
	 * 获取商铺消息
	 * @param skuId
	 * @return
	 */
	Shop getShop(Long skuId);
}
