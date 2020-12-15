package cn.edu.xmu.oomall.other.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author XQChen
 * @version 创建时间：2020/12/9 下午10:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSegmentDTO implements Serializable {
    private Long id;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
}
