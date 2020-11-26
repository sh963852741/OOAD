package cn.edu.xmu.goods.service;


import cn.edu.xmu.goods.dao.GoodsDao;
import cn.edu.xmu.goods.model.vo.SkuSelectVo;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Yifei Wang
 * @Date: 2020/11/25 16:41
 */

@Service
public class GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    /**
     * 功能描述: 获取商品的所有状态
     * @Param: []
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/25 20:09
     */
    public ReturnObject getGoodsStates(){
            return null;
    }

    public ReturnObject getAllSkus(SkuSelectVo vo,Integer page,Integer PageInfo){
        return null;
    }

    public ReturnObject getSkuDetails(Integer SkuId){
        return null;
    }


}
