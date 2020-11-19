package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Brand;
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
