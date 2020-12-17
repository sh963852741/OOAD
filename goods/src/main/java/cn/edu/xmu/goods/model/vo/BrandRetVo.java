package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Brand;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BrandRetVo {
    private Long id;
    private String name;
    private String imageUrl;
    private String detail;
    private LocalDateTime gmtModified;
    private LocalDateTime gmtCreate;

    public BrandRetVo(Brand brand) {
        this.id = brand.getId();
        this.name = brand.getName();
        this.detail = brand.getDetail();
        this.imageUrl = brand.getImageUrl();
        this.gmtCreate =brand.getGmtCreate();
        this.gmtModified = brand.getGmtModified();
    }
}
