package cn.edu.xmu.other.impl;

import cn.edu.xmu.other.dto.CustomerDto;

/**
 * @author ChengYang Li
 */
public interface ICustomerService
{
    public CustomerDto getCustomerById(long Id);
}
