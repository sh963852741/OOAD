package cn.edu.xmu.ooad.dao;

import cn.edu.xmu.ooad.mapper.GoodsCategoryPoMapper;
import cn.edu.xmu.ooad.model.po.GoodsCategoryPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDao {
    @Autowired
    GoodsCategoryPoMapper categoryMapper;

    GoodsCategoryPo getCategoryById(int id){
        GoodsCategoryPo category=categoryMapper.selectByPrimaryKey(id);
        return category;
    }

    int setCategoryById(int id, int pid, String name){
        GoodsCategoryPo categoryPo = new GoodsCategoryPo();
        categoryPo.setId(id);
        categoryPo.setPid(pid);
        categoryPo.setName(name);
        return categoryMapper.updateByPrimaryKey(categoryPo);
    }

    int addCategoryById(int pid, String name){
        GoodsCategoryPo categoryPo = new GoodsCategoryPo();
        categoryPo.setPid(pid);
        categoryPo.setName(name);
        return categoryMapper.insert(categoryPo);
    }

    int delCategoryById(int id){
        return categoryMapper.deleteByPrimaryKey(id);
    }
}
