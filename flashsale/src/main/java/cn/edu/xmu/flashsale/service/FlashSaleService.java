package cn.edu.xmu.flashsale.service;

import cn.edu.xmu.flashsale.dao.FlashSaleDao;
import cn.edu.xmu.flashsale.mapper.FlashSaleItemPoMapper;
import cn.edu.xmu.flashsale.mapper.FlashSalePoMapper;
import cn.edu.xmu.flashsale.model.bo.FlashSaleItem;
import cn.edu.xmu.flashsale.model.bo.TimeSegment;
import cn.edu.xmu.flashsale.model.po.FlashSaleItemPo;
import cn.edu.xmu.flashsale.model.po.FlashSalePo;
import cn.edu.xmu.flashsale.service.dubbo.ITimeSegmentService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Ming Qiu
 * @date Created in 2020/11/21 15:00
 **/
@Service
public class FlashSaleService implements InitializingBean {

    @Value("${flashsale.loadtime}")
    private Integer loadTime;

    @Resource
    private ReactiveRedisTemplate<String, Serializable> reactiveRedisTemplate;

    @Autowired
    ITimeSegmentService timeSegmentService;
    @Autowired
    FlashSaleDao flashSaleDao;

    public Flux<FlashSaleItem> getFlashSale(Long segId)
   {
        return reactiveRedisTemplate.opsForSet().members("1").map(x-> (FlashSaleItem) x);
   }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<TimeSegment> timeSegments = timeSegmentService.getFlashSaleSegments();

        List<TimeSegment> timeSegmentsForToday = new ArrayList<>();
        List<TimeSegment> timeSegmentsForTomorrow = new ArrayList<>();
        for(TimeSegment timeSegment:timeSegments){
            if (timeSegment.getStartTime().getHour() < loadTime){
                /* 这一部分的应该是明天的秒杀 */
                timeSegmentsForTomorrow.add(timeSegment);
            } else {
                /* 这一部分的应该是今天的秒杀 */
                timeSegmentsForToday.add(timeSegment);
            }
        }

        /* 获取了所有的秒杀段 */
        List<FlashSalePo> effectFlashSaleForToday = new ArrayList<>();
        effectFlashSaleForToday.addAll(flashSaleDao.selectFlashSaleByTimeAndDate(
                LocalDate.now(),
                timeSegmentsForToday.stream().map(e -> e.getId()).collect(Collectors.toList())
        ));
        effectFlashSaleForToday.addAll(flashSaleDao.selectFlashSaleByTimeAndDate(
                LocalDate.now().plusDays(1),
                timeSegmentsForTomorrow.stream().map(e -> e.getId()).collect(Collectors.toList())
        ));

        for(FlashSalePo flashSalePo:effectFlashSaleForToday){
            List<FlashSaleItemPo> flashSaleItemPos = flashSaleDao.getFlashSaleItemByFlashSaleId(flashSalePo.getId());
        }

    }
}

