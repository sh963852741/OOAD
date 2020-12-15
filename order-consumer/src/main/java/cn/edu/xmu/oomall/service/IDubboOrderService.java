package cn.edu.xmu.oomall.service;

import cn.edu.xmu.oomall.dto.AfterSaleDto;
import cn.edu.xmu.oomall.dto.EffectiveShareDto;
import cn.edu.xmu.oomall.dto.OrderItemDto;

import java.util.List;

/**
 * @author xincong yao
 * @date 2020-11-30
 */
public interface IDubboOrderService {

	/**
	 * 获取用于提交售后单的订单信息
	 * @param orderItemId
	 * @return
	 */
	AfterSaleDto getAfterSaleByOrderItemId(Long orderItemId);

	/**
	 * 商铺是否拥有某个order
	 * @param shopId
	 * @param orderId
	 * @return
	 */
	Boolean isShopOwnOrder(Long shopId, Long orderId);

	/**
	 * 用户是否拥有某个order
	 * @param customerId
	 * @param orderId
	 * @return
	 */
	Boolean isCustomerOwnOrder(Long customerId, Long orderId);

	/**
	 * 用户是否拥有某个orderItem
	 * @param customerId
	 * @param orderItemId
	 * @return
	 */
	Boolean isCustomerOwnOrderItem(Long customerId, Long orderItemId);

	/**
	 * 获取订单项信息
	 * @param orderItemId
	 * @return
	 */
	OrderItemDto getOrderItem(Long orderItemId);


	/**
	 * 查看订单是否可以进行支付
	 * @param id 订单id
	 * @return
	 */
	Boolean orderCanBePaid(Long id);

	/**
	 * 根据已完成的支付数额修改订单状态
	 * @param id 订单id
	 * @param amount 已支付金额
	 */
	void checkOrderPaid(Long id, Long amount);

	/**
	 * 获取订单的价格(可以被返款的部分)
	 * @param id
	 * @return
	 */
	Long getOrderCanBeRefundPrice(Long id);

	/**
	 * 返回确认收货后七天到八天且未退款的订单项信息
	 * @return
	 */
	List<EffectiveShareDto> getEffectiveShareRecord();
}
