package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.FloatPricePoMapper;
import cn.edu.xmu.goods.mapper.SKUPoMapper;
import cn.edu.xmu.goods.mapper.SPUPoMapper;
import cn.edu.xmu.ooad.mapper.FloatPricePoMapper;
import cn.edu.xmu.ooad.mapper.SKUPoMapper;
import cn.edu.xmu.ooad.mapper.SPUPoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GoodsDao {
    @Autowired
    SKUPoMapper skuPoMapper;
    @Autowired
    SPUPoMapper spuPoMapper;
    @Autowired
    FloatPricePoMapper floatPricePoMapper;

    
}
