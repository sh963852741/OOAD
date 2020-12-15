package cn.edu.xmu.goods.service.dubbo;

import cn.edu.xmu.oomall.order.dto.AfterSaleDto;
import cn.edu.xmu.oomall.order.dto.EffectiveShareDto;
import cn.edu.xmu.oomall.order.dto.OrderItemDto;
import cn.edu.xmu.oomall.order.service.IDubboOrderService;
import cn.edu.xmu.other.dto.CustomerDto;
import cn.edu.xmu.other.impl.ICustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceMock implements ICustomerService {
    @Override
    public CustomerDto getCustomerById(long Id){
        CustomerDto customerDto=new CustomerDto();
        customerDto.setId(Id);
        customerDto.setUserName("小沈");
        customerDto.setRealName("小小沈");
        return customerDto;

    }
}
