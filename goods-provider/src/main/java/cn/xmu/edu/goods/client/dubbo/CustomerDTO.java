package cn.xmu.edu.goods.client.dubbo;

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
public class CustomerDTO {
	private Long id;
	private String userName;
	private String realName;

	public CustomerDTO(Long id) {
		this.id = id;
	}
}
