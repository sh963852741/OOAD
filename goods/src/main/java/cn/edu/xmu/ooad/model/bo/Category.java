package cn.edu.xmu.ooad.model.bo;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.model.po.CategoryPo;
import cn.edu.xmu.ooad.model.vo.CategoryRetVo;
import cn.edu.xmu.ooad.model.vo.CategoryVo;
import lombok.Data;

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
    public CategoryPo gotCategoryPo() {
        CategoryPo po = new CategoryPo();
        po.setId(this.getId());
        po.setName(this.getName());
        po.setPid(this.getPid());
        return po;
    }
}
