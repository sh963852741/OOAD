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
        TimeSegment timeSegment1 = new TimeSegment();
        timeSegment1.setId(0L);
        timeSegment1.setStartTime(LocalTime.MIDNIGHT);
        timeSegment1.setEndTime(LocalTime.NOON);
        TimeSegment timeSegment2 = new TimeSegment();
        list.add(timeSegment1);
        timeSegment2.setId(1L);
        timeSegment2.setStartTime(LocalTime.NOON);
        timeSegment2.setEndTime(LocalTime.MIDNIGHT);
        list.add(timeSegment2);

        return list;
    }

    @Override
    public TimeSegment getTimeSegmentById(long id) {
        TimeSegment timeSegment = new TimeSegment();
        timeSegment.setId(1L);
        timeSegment.setStartTime(LocalTime.MIDNIGHT);
        timeSegment.setEndTime(LocalTime.NOON);
        return timeSegment;
    }
}
