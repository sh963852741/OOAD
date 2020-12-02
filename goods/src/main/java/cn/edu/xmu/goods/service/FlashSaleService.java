package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.model.bo.FlashSaleItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * @author Ming Qiu
 * @date Created in 2020/11/21 15:00
 **/
@Service
public class FlashSaleService {
    @Resource
    private ReactiveRedisTemplate<String, Serializable> reactiveRedisTemplate;
    public Flux<FlashSaleItem> getFlashSale(Long segId)
   {
        return reactiveRedisTemplate.opsForSet().members("1").map(x-> (FlashSaleItem) x);
   }
}

