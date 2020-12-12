package cn.edu.xmu.flashsale.service.dubbo;

import cn.edu.xmu.flashsale.model.bo.TimeSegment;

import java.util.List;

public interface ITimeSegmentService {
    /**
     * 返回所有秒杀时间段
     * @return 若没有，请返回空List
     */
    public List<TimeSegment> getFlashSaleSegments();

    /**
     * 返回指定ID的秒杀时间段
     * @param id
     * @return 若没有，返回null
     */
    public TimeSegment getTimeSegmentById(long id);
}
