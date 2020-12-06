package cn.edu.xmu.flashsale.service.dubbo;

import cn.edu.xmu.flashsale.model.bo.TimeSegment;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 注意！这是测试类，应当由其他模块负责实现。
 */
@Service
public class MockTimeService implements ITimeSegmentService {
    public List<TimeSegment> getFlashSaleSegments(){
        /* 假数据 */
        List<TimeSegment> list = new ArrayList<>();
        TimeSegment timeSegment = new TimeSegment();
        timeSegment.setId(1L);
        timeSegment.setStartTime(LocalTime.MIDNIGHT);
        timeSegment.setEndTime(LocalTime.NOON);
        list.add(timeSegment);
        timeSegment.setId(2L);
        timeSegment.setStartTime(LocalTime.NOON);
        timeSegment.setEndTime(LocalTime.MIDNIGHT);
        list.add(timeSegment);

        return list;
    }
}
