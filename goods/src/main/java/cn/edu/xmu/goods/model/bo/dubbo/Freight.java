package cn.edu.xmu.goods.model.bo.dubbo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Freight {
    private Long id;
    private String name;
    private Byte type;
    private Integer unit;
    private Byte idDefault;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
