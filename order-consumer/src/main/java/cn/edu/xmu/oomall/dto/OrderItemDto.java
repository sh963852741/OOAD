package cn.edu.xmu.oomall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xincong yao
 * @date 2020-11-27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto implements Serializable {

	private Long id;
	private Long skuId;
	private Long orderId;
	private String name;
	private Integer quantity;
	private Long price;
	private Long discount;
	private Long couponActivityId;
	private Long beShareId;

}
