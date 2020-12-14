package cn.edu.xmu.oomall.other.impl;

import cn.edu.xmu.oomall.other.dto.CustomerDTO;

/**
 * @author XQChen
 * @version 创建时间：2020/12/14 下午8:36
 */
public interface ICustomerService {

    /**
     * 获取customer业务对象
     * @param customerId
     * @return
     */
    CustomerDTO getCustomer(Long customerId);
}
