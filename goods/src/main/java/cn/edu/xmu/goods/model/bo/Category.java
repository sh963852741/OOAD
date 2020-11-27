package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.vo.CategoryRetVo;
import cn.edu.xmu.goods.model.vo.CategoryVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.goods.model.po.CategoryPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class Category implements VoObject, Serializable {
    private Long id;
    private Long pid;
    private String name;
    public Category() {
    }
    public Category(CategoryPo po){
        this.id = po.getId();
        this.pid = po.getPid();
        this.name = po.getName();

    }
    @Override
    public Object createVo() {
        return new CategoryRetVo(this);
    }
    @Override
    public Object createSimpleVo() {
        return new CategoryRetVo(this);
    }
    public CategoryPo createUpdatePo(CategoryVo vo){
        CategoryPo po = new CategoryPo();
        po.setId(this.getId());
        po.setName(vo.getName());
        po.setPid(this.getPid());
        return po;
    }
    public CategoryPo getCategoryPo() {
        CategoryPo po = new CategoryPo();
        po.setId(this.getId());
        po.setName(this.getName());
        po.setPid(this.getPid());
        return po;
    }
    public SimpleCategory createSimpleCategory(){
        return new SimpleCategory(this.getId(),this.getName());
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class SimpleCategory{
    private Long id;
    private String name;
}