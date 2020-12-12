package cn.edu.xmu.oomall.impl;

import cn.edu.xmu.oomall.dto.CustomerDTO;

/**
 * @author xincong yao
 * @date 2020-11-16
 */
public interface ICustomerService {

	/**
	 * 获取customer业务对象
	 * @param customerId
	 * @return
	 */
	CustomerDTO getCustomer(Long customerId);
}
