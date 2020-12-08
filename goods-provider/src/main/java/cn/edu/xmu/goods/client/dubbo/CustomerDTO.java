package cn.edu.xmu.goods.client.dubbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Yifei Wang
 * @Date: 2020/12/6 10:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO implements Serializable {
	private Long id;
	private String userName;
	private String realName;

	public CustomerDTO(Long id) {
		this.id = id;
	}
}
