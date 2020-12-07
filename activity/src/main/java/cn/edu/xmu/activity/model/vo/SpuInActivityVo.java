package cn.edu.xmu.activity.model.vo;

import cn.xmu.edu.goods.client.dubbo.SpuDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SpuInActivityVo {
    private Long id;
    private String name;
    private String goodsSn;
    private String imageUrl;
    private Byte state;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Byte disable;

    public SpuInActivityVo(SpuDTO spuDTO){
        id = spuDTO.getId();
        name = spuDTO.getName();
        goodsSn = spuDTO.getGoodsSn();
        imageUrl = spuDTO.getImageUrl();
        state = spuDTO.getState();
        gmtCreate = spuDTO.getGmtCreate();
        gmtModified = spuDTO.getGmtModified();
        disable = spuDTO.getDisable();
    }
}
