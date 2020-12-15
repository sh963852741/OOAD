package cn.edu.xmu.goods.client.dubbo;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CouponActivityDTO implements Serializable {
    private Long id;
    private String name;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
}
