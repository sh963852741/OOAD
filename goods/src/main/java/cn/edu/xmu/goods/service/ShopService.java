package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.ShopDao;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.po.ShopPo;
import cn.edu.xmu.goods.model.vo.ShopRetVo;
import cn.edu.xmu.goods.model.vo.ShopVo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.goods.model.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class ShopService {
    private  static  final Logger logger = LoggerFactory.getLogger(ShopService.class);
    @Autowired
    private ShopDao shopDao;

    /**
     * 功能描述: 根据shopid获取shop详细信息
     * @Param: [ShopId]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Lei Yang
     * @Date: 2020/11/29 23:21
     */
    public ReturnObject<Shop> getShopByShopId(Long ShopId){
        return shopDao.getShopById(ShopId);
    }

    /**
     * 功能描述: 商家申请店铺
     * @Param: [po]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Lei Yang
     * @Date: 2020/11/29 22:10
     */
    public ReturnObject  newShop(ShopVo shopVo) {
        ShopPo po=new ShopPo();
        po.setName(shopVo.getName());

        ReturnObject ret=shopDao.newShop(po);
        if(ret.getCode()!= ResponseCode.OK){
            return ret;
        }
        Shop shop=new Shop((ShopPo) ret.getData());
        ShopRetVo vo=shop.createVo();
        return new ReturnObject(vo);
    }
}