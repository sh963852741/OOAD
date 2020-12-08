package cn.edu.xmu.goods.client;



import cn.edu.xmu.goods.client.dubbo.OrderItemDTO;

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
	 * @param orderItemDTOS
	 * @return 扣库存成功的orderItem列表
	 */
	List<OrderItemDTO> modifyInventory(List<OrderItemDTO> orderItemDTOS);
}
