package cn.edu.xmu.goods.service.dubbo;

import cn.edu.xmu.goods.model.bo.dubbo.Customer;

public interface IUserService {
    /**
     *
     * @param id 用户ID
     * @return 用户
     */
    public Customer getUserById (long id);
}
