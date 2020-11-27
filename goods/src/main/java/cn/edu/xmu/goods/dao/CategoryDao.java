package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.CategoryPoMapper;
import cn.edu.xmu.goods.model.bo.Category;
import cn.edu.xmu.goods.model.po.CategoryPo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDao {
    @Autowired
    CategoryPoMapper categoryPoMapper;
    public ReturnObject<Category> addCategory(Category category) {
        CategoryPo categoryPo = category.getCategoryPo();
        ReturnObject<Category> retObj = null;
        try{
            int ret = categoryPoMapper.insertSelective(categoryPo);
            if (ret == 0) {
                //插入失败
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("新增失败：" + categoryPo.getName()));
            } else {
                //插入成功
                category.setId(categoryPo.getId());
                retObj = new ReturnObject<>(category);
            }
        }
        catch (DataAccessException e) {
            // 其他数据库错误
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }
    public ReturnObject<Category> setCategory(Category category){
        CategoryPo categoryPo = category.getCategoryPo();
        ReturnObject<Category> retObj = null;
        try{
            int ret = categoryPoMapper.updateByPrimaryKeySelective(categoryPo);
            if (ret == 0) {
                retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("品牌id不存在：" + categoryPo.getId()));
            } else {
                retObj = new ReturnObject<>();
            }
        }
        catch (DataAccessException e) {
            // 其他数据库错误
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误：%s", e.getMessage()));
        }
        catch (Exception e) {
            // 其他Exception错误
            retObj = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("发生了严重的数据库错误：%s", e.getMessage()));
        }
        return retObj;
    }
    public ReturnObject<Object> deleteCategoryById(Long id) {
        ReturnObject<Object> retObj = null;
        int ret = categoryPoMapper.deleteByPrimaryKey(id);
        if (ret == 0) {
            retObj = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("分类id不存在：" + id));
        }
        else{
            retObj = new ReturnObject<>();
        }
        return retObj;
    }
    public ReturnObject<Category> getCategoryById(long id) {
        CategoryPo categoryPo = categoryPoMapper.selectByPrimaryKey(id);
        if (categoryPo == null) {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        Category category = new Category(categoryPo);
        return new ReturnObject<>(category);
    }
}
