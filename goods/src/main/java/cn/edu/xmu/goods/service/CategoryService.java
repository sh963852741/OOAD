package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.CategoryDao;
import cn.edu.xmu.goods.model.po.CategoryPo;
import cn.edu.xmu.goods.model.vo.CategoryVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.goods.model.bo.Category;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    private CategoryDao categoryDao;

    /**
     * 功能描述: 获取商品分类关系
     * @Param: [id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/26 22:13
     */
    public ReturnObject getSubCategories(Integer id) {
        return categoryDao.getSubCategories(id.longValue());
    }

    /**
     * 功能描述: 新建商品分类
     * @Param: [id, name]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 8:51
     */
    public ReturnObject newCategory(Integer id, String name) {
        CategoryPo categoryPo=new CategoryPo();
        categoryPo.setPid(id.longValue());
        categoryPo.setName(name);
        ReturnObject ret=categoryDao.insertCategory(categoryPo);
        return ret;
    }

    /**
     * 功能描述: 修改商品类目
     * @Param: [id, name]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 16:40
     */
    public ReturnObject changeCategory(Integer id, String name) {
        CategoryPo po=new CategoryPo();
        po.setId(id.longValue());
        po.setName(name);
        ReturnObject ret=categoryDao.updateCategory(po);
        return ret;
    }

    /**
     * 功能描述: 删除商品类目
     * @Param: [id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 16:52
     */
    public ReturnObject deleteCategoryById(Integer id) {
        ReturnObject ret=categoryDao.deleteCategoryById(id.longValue());
        return ret;
    }

}
