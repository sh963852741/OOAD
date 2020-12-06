package cn.xmu.edu.goodscilent;



import cn.xmu.edu.goodscilent.dubbo.OrderItem;

import java.util.List;

/**
 * @Author: Yifei Wang
 * @Date: 2020/12/6 10:48
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
