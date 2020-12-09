package cn.edu.xmu.goods.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FloatPriceRetVo {

    private Long id;
    private Long activityPrice;
    private Integer quantity;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Map<String, Object> createBy;
    private Map<String, Object> modifiedBy;
    private Byte valid;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
