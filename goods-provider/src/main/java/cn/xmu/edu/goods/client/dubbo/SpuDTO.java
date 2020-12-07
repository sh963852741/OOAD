package cn.xmu.edu.goods.client.dubbo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SpuDTO {
    private Long id;
    private String name;
    private String goodsSn;
    private Long shopId;
    private String imageUrl;
    private Byte state;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Byte disable;
}
