package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.ShopDao;
import cn.edu.xmu.ooad.util.ReturnObject;
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
     * 功能描述: 获取shop详细信息
     * @Param: [ShopId]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Lei Yang
     * @Date: 2020/11/29 23:21
     */
    public ReturnObject getShop(Long ShopId){
        return shopDao.getShopById(ShopId);
    }

}