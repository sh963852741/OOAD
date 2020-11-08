package com.xmu.oomall.dao;

import com.xmu.oomall.mapper.ShopPoMapper;
import com.xmu.oomall.model.po.ShopPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ShopDao {
    @Autowired
    ShopPoMapper shopPoMapper;

    int setShopState(long id, byte state){
        ShopPo shopPo = new ShopPo();
        shopPo.setState(state);
        shopPo.setId(id);
        return shopPoMapper.updateByPrimaryKeySelective(shopPo);
    }

    int addShop(ShopPo shopPo){
        return shopPoMapper.insert(shopPo);
    }

    int delShop(long id){
        return shopPoMapper.deleteByPrimaryKey(id);
    }
}
