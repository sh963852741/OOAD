package cn.edu.xmu.goods.service;


import cn.edu.xmu.goods.dao.GoodsDao;
import cn.edu.xmu.goods.model.bo.Good;
import cn.edu.xmu.goods.model.vo.SkuSelectReturnVo;
import cn.edu.xmu.goods.model.vo.SkuSelectVo;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
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
            return goodsDao.getGoodState();
    }

    public ReturnObject getAllSkus(SkuSelectVo vo,Integer page,Integer pageSize){
        return goodsDao.getAllSkus(vo, page, pageSize);
    }

    public ReturnObject getSkuDetails(Integer SkuId){
        return null;
    }


}
