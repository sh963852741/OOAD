package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Category;
import lombok.Data;

@Data
public class CategoryRetVo {
    private Long id;
    private String name;
    private Long pid;
    public CategoryRetVo(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.pid = category.getId();
    }
}
