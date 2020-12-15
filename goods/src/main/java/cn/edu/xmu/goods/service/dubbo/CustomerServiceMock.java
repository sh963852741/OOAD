package cn.edu.xmu.goods.service.dubbo;


import cn.edu.xmu.oomall.other.dto.CustomerDTO;
import cn.edu.xmu.oomall.other.impl.ICustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceMock implements ICustomerService {


    @Override
    public CustomerDTO getCustomer(Long customerId) {
        CustomerDTO customerDto=new CustomerDTO();
        customerDto.setId(customerId);
        customerDto.setUserName("小沈");
        customerDto.setRealName("小小沈");
        return customerDto;
    }
}
