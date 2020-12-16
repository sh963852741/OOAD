package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.CategoryPoMapper;
import cn.edu.xmu.goods.model.bo.Category;
import cn.edu.xmu.goods.model.po.CategoryPo;
import cn.edu.xmu.goods.model.po.CategoryPoExample;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CategoryDao {

    private  static  final Logger logger = LoggerFactory.getLogger(CategoryDao.class);

    @Autowired
    private CategoryPoMapper categoryPoMapper;

    /**
     * 功能描述: 获取商品分类列表
     * @Param: [id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/26 22:15
     */
    public ReturnObject getSubCategories(Long id){
        CategoryPoExample example=new CategoryPoExample();
        CategoryPoExample.Criteria criteria=example.createCriteria();
        criteria.andPidEqualTo(id);
        try {
            List<CategoryPo> categoryPos = categoryPoMapper.selectByExample(example);
            ReturnObject<List> ret=new ReturnObject<>(categoryPos);
            return ret;
        }catch (Exception e){
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    /**
     * 功能描述: 新建商品分类
     * @Param: [categoryPo]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 8:53
     */
    public ReturnObject insertCategory(CategoryPo categoryPo) {
        try{
            categoryPo.setGmtCreate(LocalDateTime.now());
            categoryPo.setGmtModified(categoryPo.getGmtCreate());
            int ret=categoryPoMapper.insertSelective(categoryPo);
            if (ret == 0) {
                logger.debug("insertSku: insert failed: " + categoryPo.getId());
                return new ReturnObject(ResponseCode.FIELD_NOTVALID);
            }
            return new ReturnObject(categoryPo);
        }catch (Exception e){
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    /**
     * 功能描述: 修改商品类目
     * @Param: [po]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 16:46
     */
    public ReturnObject updateCategory(CategoryPo po) {
        try{
            int ret;
            ret=categoryPoMapper.updateByPrimaryKeySelective(po);
            if(ret ==0){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }else{
                return new ReturnObject(ResponseCode.OK);
            }
        }catch (Exception e){
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }

    }

    /**
     * 功能描述: 根据id删除Category
     * @Param: id
     * @Return: returnobject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 16:53
     */
    public ReturnObject deleteCategoryById(Long id){
        try{
            int ret;
            ret= categoryPoMapper.deleteByPrimaryKey(id);
            if(ret==0){
                return new ReturnObject(ResponseCode.FIELD_NOTVALID);
            }else{
                return new ReturnObject(ResponseCode.OK);
            }
        }catch (Exception e){
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    /**
     * 功能描述: 根据id获取Category
     * @Param: [id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/28 10:39
     */
    public ReturnObject getCategoryById(Long id){
        try{
            CategoryPo categoryPo=categoryPoMapper.selectByPrimaryKey(id);
            if(categoryPo==null){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            Category category=new Category(categoryPo);
            return new ReturnObject(category);
        }catch (Exception e){
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

}
