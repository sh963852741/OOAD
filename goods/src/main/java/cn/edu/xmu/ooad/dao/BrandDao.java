package cn.edu.xmu.ooad.dao;

import cn.edu.xmu.ooad.mapper.BrandPoMapper;
import cn.edu.xmu.ooad.model.po.BrandPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BrandDao {
    @Autowired
    BrandPoMapper brandPoMapper;

    BrandPo getBrandById(long id){
        return brandPoMapper.selectByPrimaryKey(id);
    }

    int addBrandById(long id,String name,String detail){
        BrandPo brandPo = new BrandPo();
        brandPo.setId(id);
        brandPo.setName(name);
        brandPo.setDetail(detail);
        brandPo.setImageUrl(null);
        return brandPoMapper.insert(brandPo);
    }

    int setBrandById(long id,String name,String detail){
        BrandPo brandPo = new BrandPo();
        brandPo.setId(id);
        brandPo.setName(name);
        brandPo.setDetail(detail);
        return brandPoMapper.updateByPrimaryKey(brandPo);
    }

    int deleteBrandById(long id){
        return brandPoMapper.deleteByPrimaryKey(id);
    }
}
