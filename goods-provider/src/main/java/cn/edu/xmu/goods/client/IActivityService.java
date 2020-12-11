package cn.edu.xmu.goods.client;

import cn.edu.xmu.goods.client.dubbo.OrderItemDTO;
import cn.edu.xmu.goods.client.dubbo.PriceDTO;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yifei Wang
 * @Date: 2020/12/6 10:49
 */
public interface IActivityService {

	/**
	 * 校验sku是否适用与给定的优惠活动
	 * 以及优惠卷是否可用 如果可用则直接使用 不可用的orderItem 不加入map
	 * @param orderItemDTOS
	 * @param couponId
	 * @return skuId到couponActivityId的映射
	 */
	Map<Long, Long> validateActivity(List<OrderItemDTO> orderItemDTOS, Long couponId);


	/**
	 * 功能描述: 根据skuId获取团购活动id
	 * @Param: skuId
	 * @Return:不存在返回null
	 */
	Long getGrouponId(Long skuId);

	/**
	 * 功能描述: 根据skuId 获取预售活动id
	 * @Param: [skuId]
	 * @Return: 不存在返回null
	 */
	Long getPreSale(Long skuId);

	/**
	 * 功能描述: 获取预售活动的定金和尾款
	 * @Param: [preSaleId]
	 * @Return: Map<String, Long> key为prePrice 和 finalPrice
	 */
	Map<String, Long> getPrePrice(Long preSaleId);

	// type表示类型 团购或者预售 如果priceDto里预售款是null 则尾款表示是团购活动的价格
	List<PriceDTO> getPriceAndName(List<OrderItemDTO> list , Integer type);
}
