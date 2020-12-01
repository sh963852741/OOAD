package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.*;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.bo.Spu;
import cn.edu.xmu.goods.model.po.ShopPo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

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
    public ReturnObject getShopState(){return null;}

    /**
     * 功能描述: 商家申请店铺
     * @Param: [po]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Lei Yang
     * @Date: 2020/11/29 22:10
     */
    public ReturnObject newShop(ShopPo po)
    {
        try{
            int ret;
            po.setGmtCreate(LocalDateTime.now());
            po.setGmtModified(LocalDateTime.now());
            po.setState(Shop.State.OFFLINE.getCode().byteValue());
            ret=shopPoMapper.insertSelective(po);
            if(ret==0){
                return new ReturnObject(ResponseCode.FIELD_NOTVALID);
            }else{
                return new ReturnObject(po);
            }
        }catch (Exception e){
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }
    /**
     * 功能描述: 商家修改店铺信息
     * @Param: [po]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Lei Yang
     * @Date: 2020/11/29 22:10
     */
    public ReturnObject UpdateShop(){return null;}

    /**
     * 功能描述: 关闭店铺
     * @Param: [po]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Lei Yang
     * @Date: 2020/11/29 22:10
     */
    public ReturnObject deleteShopbyID(long ID){return null;}

    /**
     * 功能描述: 审核店铺
     * @Param: [po]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Lei Yang
     * @Date: 2020/11/29 22:10
     */
    public ReturnObject passShop(long Shop_ID,long ID){return null; }

    /**
     * 功能描述: 管理员上线店铺
     * @Param: [po]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Lei Yang
     * @Date: 2020/11/29 22:10
     */
    public ReturnObject OnlineShop(long ID){return null;}

    /**
     * 功能描述: 下线店铺
     * @Param: [po]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Lei Yang
     * @Date: 2020/11/29 22:10
     */
    public ReturnObject OfflineShop(long ID){return null;}
}

