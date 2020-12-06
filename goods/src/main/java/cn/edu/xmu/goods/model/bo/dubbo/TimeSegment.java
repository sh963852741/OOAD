package cn.edu.xmu.goods.model.bo.dubbo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeSegment {
    private Long id;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
}
