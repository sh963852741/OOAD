package cn.edu.xmu.goods.client.dubbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: Yifei Wang
 * @Date: 2020/12/6 10:48
 */
@Data
@NoArgsConstructor
public class ShopDTO implements Serializable {
	private Long id;
	private String name;
	private LocalDateTime gmtCreateTime;
	private LocalDateTime gmtModiTime;
	private Byte state;
	public ShopDTO(Long id){
		this.id =id;
	}
}
