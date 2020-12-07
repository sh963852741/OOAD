package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Brand;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


@Data
public class BrandVo {
    @NotBlank(message = "商品名不能为空")
    private String name;
    private String detail;
    private LocalDateTime getGmtCreate;

    /**
     * 构造函数
     */
    public Brand createBrand() {
        Brand brand = new Brand();
        brand.setName(this.name);
        brand.setDetail(this.detail);
        brand.setGmtCreate(this.getGmtCreate);
        return brand;
    }
}
