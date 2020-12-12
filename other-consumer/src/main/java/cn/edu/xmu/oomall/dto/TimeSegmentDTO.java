package cn.edu.xmu.oomall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author XQChen
 * @version 创建时间：2020/12/9 下午10:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSegmentDTO {
    private Long id;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
}
