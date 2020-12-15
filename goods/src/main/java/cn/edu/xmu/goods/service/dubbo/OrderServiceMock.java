package cn.edu.xmu.goods.service.dubbo;

import cn.edu.xmu.ooad.order.bo.Order;
import cn.edu.xmu.oomall.order.dto.AfterSaleDto;
import cn.edu.xmu.oomall.order.dto.EffectiveShareDto;
import cn.edu.xmu.oomall.order.dto.OrderItemDto;
import cn.edu.xmu.oomall.order.service.IDubboOrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceMock implements IDubboOrderService {
    @Override
    public AfterSaleDto getAfterSaleByOrderItemId(Long orderItemId) {
        return null;
    }

    @Override
    public Boolean isShopOwnOrder(Long shopId, Long orderId) {
        return null;
    }

    @Override
    public Boolean isCustomerOwnOrder(Long customerId, Long orderId) {
        return true;
    }

    @Override
    public Boolean isCustomerOwnOrderItem(Long customerId, Long orderItemId) {
        return true;
    }

    @Override
    public OrderItemDto getOrderItem(Long orderItemId) {
        OrderItemDto orderItemDto=new OrderItemDto();
        orderItemDto.setId(1L);
        orderItemDto.setSkuId(1L);
        return orderItemDto;
    }

    @Override
    public Boolean orderCanBePaid(Long id) {
        return null;
    }

    @Override
    public void checkOrderPaid(Long id, Long amount) {

    }

    @Override
    public Long getOrderCanBeRefundPrice(Long id) {
        return null;
    }

    @Override
    public List<EffectiveShareDto> getEffectiveShareRecord() {
        return null;
    }
}
