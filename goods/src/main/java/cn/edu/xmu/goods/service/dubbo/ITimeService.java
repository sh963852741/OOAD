package cn.edu.xmu.goods.service.dubbo;

import cn.edu.xmu.goods.model.bo.dubbo.TimeSegment;

public interface ITimeService {
    /**
     *
     * @param id 秒杀时间段ID
     * @return 若不存在则返回null
     */
    public TimeSegment getById(long id);
}
