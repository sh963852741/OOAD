package cn.edu.xmu.ooad.service;

import cn.edu.xmu.ooad.dao.CategoryDao;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.model.bo.Category;
import cn.edu.xmu.ooad.model.vo.CategoryVo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    private CategoryDao categoryDao;
    public ReturnObject<Object> deleteCategory(Long id) {
        return categoryDao.deleteCategoryById(id);
    }

    public ReturnObject<VoObject> addCategory(CategoryVo vo) {
        Category category = vo.createCategory();
        ReturnObject<Category> retObj = categoryDao.addCategory(category);
        ReturnObject<VoObject> retCategory = null;
        if (retObj.getCode().equals(ResponseCode.OK)) {
            retCategory = new ReturnObject<>(retObj.getData());
        } else {
            retCategory = new ReturnObject<>(retObj.getCode(), retObj.getErrmsg());
        }
        return retCategory;
    }
    public ReturnObject <Object> setCategory( Long id, CategoryVo vo) {
        Category category = vo.createCategory();
        category.setId(id);
        ReturnObject<Category> retObj = categoryDao.setCategory(category);
        ReturnObject<Object> retCategory;
        if (retObj.getCode().equals(ResponseCode.OK)) {
            retCategory = new ReturnObject<>(retObj.getData());
        } else {
            retCategory = new ReturnObject<>(retObj.getCode(), retObj.getErrmsg());
        }
        return retCategory;
    }
    public ReturnObject<Object> getCategory(Long id) {
        ReturnObject<Category> retObj = categoryDao.getCategoryById(id);
        ReturnObject<Object> retCategory = null;
        if (retObj.getCode().equals(ResponseCode.OK)) {
            retCategory = new ReturnObject<>(retObj.getData());
        } else {
            retCategory = new ReturnObject<>(retObj.getCode(), retObj.getErrmsg());
        }
        return retCategory;
    }

}
