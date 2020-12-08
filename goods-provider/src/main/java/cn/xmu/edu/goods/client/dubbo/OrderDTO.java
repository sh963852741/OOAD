package cn.xmu.edu.goods.client.dubbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Yifei Wang
 * @Date: 2020/12/6 10:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO implements Serializable {

	private Long id;
	private CustomerDTO customerDTO;
	private ShopDTO shopDTO;
	private String orderSn;
	private List<OrderDTO> subOrderDTOS;
	private Long pid;
	private String consignee;
	private Long regionId;
	private String address;
	private String mobile;
	private String message;
	private Integer orderType;
	private Long freightPrice;
	private Long couponId;
	private Long couponActivityId;
	private Long discountPrice;
	private Long originPrice;
	private Long presaleId;
	private Long grouponId;
	private Long grouponDiscount;
	private Integer rebateNum;
	private LocalDateTime confirmTime;
	private String shipmentSn;
	private Integer state;
	private Integer subState;
	private Integer beDeleted;
	private List<OrderItemDTO> orderItemDTOS;
	private LocalDateTime gmtCreated;
	private LocalDateTime gmtModified;


	public OrderDTO createAndAddSubOrder(ShopDTO shopDTO, List<OrderItemDTO> orderItemDTOS) {
		OrderDTO subOrderDTO = new OrderDTO();

		subOrderDTO.customerDTO = this.customerDTO;

		subOrderDTO.shopDTO = shopDTO;

		subOrderDTO.orderItemDTOS = new ArrayList<>();
		subOrderDTO.orderItemDTOS.addAll(orderItemDTOS);

		subOrderDTO.consignee = this.consignee;
		subOrderDTO.regionId = this.regionId;
		subOrderDTO.address = this.address;
		subOrderDTO.mobile = this.mobile;
		subOrderDTO.message = this.message;

		this.subOrderDTOS.add(subOrderDTO);

		return subOrderDTO;
	}

	public void calcAndSetParentOrderOriginPrice() {
		Long price = 0L;
		for (OrderDTO o : getSubOrderDTOS()) {
			price += o.getOriginPrice();
		}
		originPrice = price;
	}

	public void calcAndSetSubOrdersOriginPrice() {
		for (OrderDTO subOrderDTO : getSubOrderDTOS()) {
			long price = 0L;
			for (OrderItemDTO oi : subOrderDTO.getOrderItemDTOS()) {
				price += oi.getQuantity() * oi.getPrice();
			}
			subOrderDTO.setOriginPrice(price);
		}
	}

	public Integer calcAndGetRebate() {
		if (originPrice == null) {
			return 0;
		}
		return Math.toIntExact(originPrice / 25L);
	}

//	public void setOrderStatus(OrderStatus status, boolean withSubOrder) {
//		state = status.value();
//		if (withSubOrder) {
//			for (Order o : getSubOrders()) {
//				o.setState(state);
//			}
//		}
//	}
//
//	public String createAndGetOrderSn() {
//		orderSn = UUID.randomUUID().toString();
//		return orderSn;
//	}
//
//	public static Order toOrder(OrderPo orderPo) {
//		if (orderPo == null) {
//			return null;
//		}
//		Order o = new Order();
//		o.id = orderPo.getId();
//		o.customer = new Customer(orderPo.getCustomerId());
//		o.shop = new Shop(orderPo.getShopId());
//		o.orderSn = orderPo.getOrderSn();
//		o.pid = orderPo.getPid();
//		o.couponActivityId = orderPo.getCouponActivityId();
//		o.consignee = orderPo.getConsignee();
//		o.regionId = orderPo.getRegionId();
//		o.address = orderPo.getAddress();
//		o.mobile = orderPo.getMobile();
//		o.message = orderPo.getMessage();
//		o.orderType = orderPo.getOrderType();
//		o.freightPrice = orderPo.getFreightPrice();
//		o.couponId = orderPo.getCouponId();
//		o.discountPrice = orderPo.getDiscountPrice();
//		o.originPrice = orderPo.getOriginPrice();
//		o.presaleId = orderPo.getPresaleId();
//		o.grouponId = orderPo.getGrouponId();
//		o.grouponDiscount = orderPo.getGrouponDiscount();
//		o.rebateNum = orderPo.getRebateNum();
//		o.confirmTime = orderPo.getConfirmTime();
//		o.shipmentSn = orderPo.getShipmentSn();
//		o.state = orderPo.getState();
//		o.subState = orderPo.getSubState();
//		o.beDeleted = orderPo.getBeDeleted();
//		o.gmtCreated = orderPo.getGmtCreated();
//		o.gmtModified = orderPo.getGmtModified();
//		return o;
//	}
//
//	public static Order toOrder(OrderPutRequest vo) {
//		Order o = new Order();
//		o.setConsignee(vo.getConsignee());
//		o.setAddress(vo.getAddress());
//		o.setMobile(vo.getMobile());
//		o.setRegionId(vo.getRegionId());
//		return o;
//	}
//
//	public static Order toOrder(OrderPostRequest request) {
//		Order o = new Order();
//		o.customer = new Customer();
//		o.shop = new Shop();
//		o.subOrders = new ArrayList<>();
//		o.consignee = request.getConsignee();
//		o.regionId = request.getRegionId();
//		o.address = request.getAddress();
//		o.mobile = request.getMobile();
//		o.message = request.getMessage();
//		o.couponId = request.getCouponId();
//		o.presaleId = request.getPresaleId();
//		o.grouponId = request.getGrouponId();
//
//		o.orderItems = new ArrayList<>();
//		for (OrderPostRequest.OrderItem oi : request.getOrderItems()) {
//			OrderItem noi = new OrderItem();
//			noi.setSkuId(oi.getSkuId());
//			noi.setQuantity(oi.getQuantity());
//			o.orderItems.add(noi);
//		}
//
//		return o;
//	}
}
