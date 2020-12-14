package cn.edu.xmu.flashsale.service.dubbo.implement;

import cn.edu.xmu.flashsale.dao.FlashSaleDao;
import cn.edu.xmu.flashsale.model.bo.FlashSaleItem;
import cn.edu.xmu.flashsale.model.bo.RedisFlash;
import cn.edu.xmu.flashsale.model.po.FlashSaleItemPo;
import cn.edu.xmu.goods.client.IFlashSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class FlashSaleServiceImpl implements IFlashSaleService {

    @Resource
    private RedisTemplate<String, RedisFlash> redisTemplate;
    @Autowired
    private FlashSaleDao flashSaleDao;

    @Override
    public Long getSeckillId(Long skuId) {
        String hashKey ="FlashSaleSKU" + LocalDate.now().toString();
        RedisFlash redisFlash = (RedisFlash)redisTemplate.boundHashOps(hashKey).get(skuId);
        if (redisFlash == null){
            return -1L;
        }

        if (redisFlash.getBeginTime().isBefore(LocalTime.now())
        && redisFlash.getEndTime().isAfter(LocalTime.now())){
            return redisFlash.getActivityId();
        }
        return -1L;
    }

    @Override
    public Long getPrice(Long skuId) {
        String hashKey ="FlashSaleSKU" + LocalDate.now().toString();
        RedisFlash redisFlash = (RedisFlash)redisTemplate.boundHashOps(hashKey).get(skuId);
        if (redisFlash == null){
            return -1L;
        }

        if (redisFlash.getBeginTime().isBefore(LocalTime.now())
                && redisFlash.getEndTime().isAfter(LocalTime.now())){
            return redisFlash.getPrice();
        }
        return -1L;
    }

    @Override
    public Integer delFlashSaleBySegId(Long segId) {
        String setKey1 ="FlashSale" + LocalDate.now().toString() + segId;
        String setKey2 ="FlashSale" + LocalDate.now().plusDays(1).toString() + segId;
        redisTemplate.delete(setKey1);
        redisTemplate.delete(setKey2);

        return flashSaleDao.delFlashSaleByTimeSegment(segId);
    }


}
