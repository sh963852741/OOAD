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
public class Shop {
	private Long id;
	private String name;
	private String gmtCreateTime;
	private String gmtModiTime;

	public Shop(Long shopId) {
		this.id = shopId;
	}
}
