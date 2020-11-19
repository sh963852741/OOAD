package cn.edu.xmu.ooad.model.vo;

import cn.edu.xmu.ooad.model.bo.Brand;
import lombok.Data;

@Data
public class BrandRetVo {
    private Long id;
    private String name;
    private String imageUrl;
    private String detail;

    public BrandRetVo(Brand brand) {
        this.id = brand.getId();
        this.name = brand.getName();
        this.detail = brand.getDetail();
        this.imageUrl = brand.getImageUrl();
    }
}
