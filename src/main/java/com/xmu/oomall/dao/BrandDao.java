package com.xmu.oomall.dao;

import com.xmu.oomall.mapper.BrandPoMapper;
import com.xmu.oomall.model.po.BrandPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BrandDao {
    @Autowired
    BrandPoMapper brandPoMapper;

    BrandPo getBrandById(int id){
        return brandPoMapper.selectByPrimaryKey(id);
    }

    int addBrandById(int id,String name,String detail){
        BrandPo brandPo = new BrandPo();
        brandPo.setId(id);
        brandPo.setName(name);
        brandPo.setDetail(detail);
        brandPo.setImageUrl(null);
        return brandPoMapper.insert(brandPo);
    }

    int setBrandById(int id,String name,String detail){
        BrandPo brandPo = new BrandPo();
        brandPo.setId(id);
        brandPo.setName(name);
        brandPo.setDetail(detail);
        return brandPoMapper.updateByPrimaryKey(brandPo);
    }

    int deleteBrandById(int id){
        return brandPoMapper.deleteByPrimaryKey(id);
    }
}
