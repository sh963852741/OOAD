package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.CategoryDao;
import cn.edu.xmu.goods.model.po.CategoryPo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private GoodsService goodsService;

    /**
     * 功能描述: 获取商品分类关系
     * @Param: [id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/26 22:13
     */
    public ReturnObject getSubCategories(Long id) {
        if(categoryDao.getCategoryById(id).getData() == null && id!=0){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        return categoryDao.getSubCategories(id);
    }

    /**
     * 功能描述: 新建商品分类
     * @Param: [id, name]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 8:51
     */
    public ReturnObject newCategory(Long id, String name) {
        if(categoryDao.getCategoryById(id).getData() == null && id !=0 ){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(categoryDao.hasSameName(name)){
            return new ReturnObject(ResponseCode.CATEGORY_NAME_SAME);
        }
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
    public ReturnObject changeCategory(Long id, String name) {
        if(categoryDao.getCategoryById(id).getData() == null){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(categoryDao.hasSameName(name)){
            return new ReturnObject<>(ResponseCode.CATEGORY_NAME_SAME);
        }
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
    public ReturnObject deleteCategoryById(Long id) {
        goodsService.setCategoryZero(id);
        var sub = categoryDao.getSubCategories(id);
        if(sub.getCode().equals(ResponseCode.OK)){
            for(CategoryPo categoryPo:sub.getData()){
                goodsService.setCategoryZero(categoryPo.getId());
            }
        }
        ReturnObject ret=categoryDao.deleteCategoryById(id.longValue());
        return ret;
    }

}
