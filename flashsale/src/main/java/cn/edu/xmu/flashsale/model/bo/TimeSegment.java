package cn.edu.xmu.flashsale.model.bo;

import lombok.Data;

import java.time.LocalTime;

/**
 * 秒杀时间段
 */
@Data
public class TimeSegment {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
}
