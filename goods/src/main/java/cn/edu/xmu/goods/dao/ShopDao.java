package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.*;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.po.ShopPo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.slf4j.LoggerFactory;

@Repository
public class ShopDao {
    @Autowired
    ShopPoMapper shopPoMapper;
    private  static  final Logger logger = LoggerFactory.getLogger(ShopDao.class);

    /**
     * 功能描述: 根据id获取shop
     * @Param: [id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Lei Yang
     * @Date: 2020/11/29 22:10
     */
    public ReturnObject getShopById(Long id){
        ShopPo shopPo;
        try {
            shopPo = shopPoMapper.selectByPrimaryKey(id);
        }catch (Exception e){
            logger.error("selectShopDetails: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        if(shopPo==null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        Shop shop=new Shop(shopPo);

        return new ReturnObject<>(shop);
    }
}
