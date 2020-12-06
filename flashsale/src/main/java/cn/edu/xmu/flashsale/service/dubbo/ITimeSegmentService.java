package cn.edu.xmu.flashsale.service.dubbo;

import cn.edu.xmu.flashsale.model.bo.TimeSegment;

import java.util.List;

public interface ITimeSegmentService {
    public List<TimeSegment> getFlashSaleSegments();
}
