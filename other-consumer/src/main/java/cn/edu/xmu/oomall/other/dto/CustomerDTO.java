package cn.edu.xmu.oomall.other.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xincong yao
 * @date 2020-11-16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO implements Serializable {
	private Long id;
	private String userName;
	private String realName;
}
