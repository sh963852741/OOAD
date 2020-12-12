package cn.edu.xmu.oomall.impl;

import cn.edu.xmu.oomall.dto.TimeSegmentDTO;

import java.util.List;

/**
 * @author XQChen
 * @version 创建时间：2020/12/9 下午10:07
 */
public interface ITimeSegmentService {

    /**
     * 返回所有秒杀时间段
     */
    public List<TimeSegmentDTO> getFlashSaleSegments();

    /**
     * 返回指定ID的秒杀时间段
     */
    public TimeSegmentDTO getFlashSaleSegmentById(Long id);
}
