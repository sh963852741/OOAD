package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.po.ShopPo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.goods.mapper.ShopPoMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ReturnObject<Shop> getShopById(Long id){
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
    /**
     * 功能描述: 获取shop所有状态
     * @Param:
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Lei Yang
     * @Date: 2020/12/1 10:11
     */
    public ReturnObject getShopState()
    {
        List<Map<String, Object>> stateList=new ArrayList<>();
        for (Shop.State enum1 : Shop.State.values()) {
            Map<String,Object> temp=new HashMap<>();
            temp.put("code",enum1.getCode());
            temp.put("name",enum1.getDescription());
            stateList.add(temp);
        }
        return new ReturnObject<>(stateList);
    }

    /**
     * 功能描述: 商家申请店铺
     * @Param: [po]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Lei Yang
     * @Date: 2020/11/29 22:10
     */
    public ReturnObject newShop(ShopPo po)
    {
//        try{
            int ret;
            po.setGmtCreate(LocalDateTime.now());
            po.setGmtModified(LocalDateTime.now());
            po.setState(Shop.State.EXAME.getCode().byteValue());
            ret=shopPoMapper.insertSelective(po);
            if(ret==0){
                return new ReturnObject(ResponseCode.FIELD_NOTVALID);
            }else{
                return new ReturnObject(po);
            }
//        }catch (Exception e){
//            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
//        }
    }
    /**
     * 功能描述: 商家修改店铺信息
     * @Param: [po]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Lei Yang
     * @Date: 2020/11/29 22:10
     */
    public ReturnObject UpdateShop(Long id, Shop shop)
    {
        int ret;
        ShopPo shopPo=shopPoMapper.selectByPrimaryKey(id);
        if(shopPo == null){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        shopPo.setName(shop.getName());
        if(shopPo.getState()==Shop.State.NOTPASS.getCode().byteValue()
                ||shopPo.getState()==Shop.State.FORBID.getCode().byteValue())
        {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        else
        {
            try{
                shopPo.setGmtModified(LocalDateTime.now());
                ret=shopPoMapper.updateByPrimaryKeySelective(shopPo);
            }catch (Exception e){
                logger.error("updateShop: DataAccessException:" + e.getMessage());
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
            }
            if (ret == 0) {
                logger.debug("updateShop: update fail. shop id: " + shopPo.getId());
                return new ReturnObject(ResponseCode.FIELD_NOTVALID);
            } else {
                logger.debug("updateShop: update shop success : " + shopPo.toString());
                return new ReturnObject();
            }
        }
    }

    /**
     * 功能描述: 修改店铺状态
     * @Param: [po]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Lei Yang
     * @Date: 2020/11/29 22:10
     */
    public ReturnObject updateShopState(Shop shop)
    {
        ShopPo shopPo=shop.createPo();
        int ret;
        try {
            ret = shopPoMapper.updateByPrimaryKeySelective(shopPo);
        } catch (Exception e) {
            logger.error("updateShopState: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        if (ret == 0) {
            logger.debug("updateShop: update fail ! " + shopPo.toString());
            return new ReturnObject(ResponseCode.FIELD_NOTVALID);
        } else {
            logger.debug("updateShop: update shop success ! " + shopPo.toString());
            return new ReturnObject();
        }
    }
}
