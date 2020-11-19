package cn.edu.xmu.ooad.model.vo;

import cn.edu.xmu.ooad.model.bo.Brand;
import lombok.Data;

@Data
public class BrandSimpleRetVo {
    private Long id;
    private String name;

    public BrandSimpleRetVo(Brand brand) {
        this.id = brand.getId();
        this.name = brand.getName();
    }
}
