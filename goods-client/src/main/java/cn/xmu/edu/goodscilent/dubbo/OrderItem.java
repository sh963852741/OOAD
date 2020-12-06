package cn.xmu.edu.goodscilent.dubbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Yifei Wang
 * @Date: 2020/12/6 10:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

	private Long id;
	private Long skuId;
	private Long orderId;
	private String name;
	private Integer quantity;
	private Long price;
	private Long discount;
	private Long couponActivityId;
	private Long beShareId;

//	public static OrderItem toOrderItem(OrderItemPo po) {
//		if (po == null) {
//			return null;
//		}
//		OrderItem oi = new OrderItem();
//		oi.setId(po.getId());
//		oi.setSkuId(po.getGoodsSkuId());
//		oi.setOrderId(po.getOrderId());
//		oi.setName(po.getName());
//		oi.setQuantity(po.getQuantity());
//		oi.setPrice(po.getPrice());
//		oi.setDiscount(po.getDiscount());
//		oi.setCouponActivityId(po.getCouponActivityId());
//		oi.setBeShareId(po.getBeShareId());
//		return oi;
//	}

}
