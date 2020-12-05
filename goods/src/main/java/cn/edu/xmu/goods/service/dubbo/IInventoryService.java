package cn.edu.xmu.goods.service.dubbo;

import cn.edu.xmu.goods.model.bo.dubbo.OrderItem;

import java.util.List;

/**
 * @author xincong yao
 * @date 2020-11-17
 */
public interface IInventoryService {

	/**
	 * 修改一批订单项对应的sku库存
	 * 部分sku库存不足不影响其他sku
	 * 返回不能为null，全部失败返回空列表
	 * @param orderItems
	 * @return 扣库存成功的orderItem列表
	 */
	List<OrderItem> modifyInventory(List<OrderItem> orderItems);
}
