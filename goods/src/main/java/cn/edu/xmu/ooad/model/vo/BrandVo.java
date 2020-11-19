package cn.edu.xmu.ooad.model.vo;

import cn.edu.xmu.ooad.model.bo.Brand;

import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class BrandVo {
    @NotBlank(message = "商品名不能为空")
    private String name;
    private String detail;

    /**
     * 构造函数
     */
    public Brand createBrand() {
        Brand brand = new Brand();
        brand.setName(this.name);
        brand.setDetail(this.detail);
        return brand;
    }
}
