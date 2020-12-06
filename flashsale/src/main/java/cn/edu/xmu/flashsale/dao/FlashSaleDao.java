package cn.edu.xmu.flashsale.dao;

import cn.edu.xmu.flashsale.mapper.FlashSaleItemPoMapper;
import cn.edu.xmu.flashsale.mapper.FlashSalePoMapper;
import cn.edu.xmu.flashsale.model.po.FlashSaleItemPo;
import cn.edu.xmu.flashsale.model.po.FlashSalePo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class FlashSaleDao {
    @Autowired
    FlashSaleItemPoMapper flashSaleItemPoMapper;
    @Autowired
    FlashSalePoMapper flashSalePoMapper;

    public int addFlashSale(FlashSalePo po){
        po.setGmtCreate(LocalDateTime.now());
        return flashSalePoMapper.insert(po);
    }

    public int deleteFlashSale(long id){
        return flashSalePoMapper.deleteByPrimaryKey(id);
    }

    public FlashSalePo getFlashSale(long id){
        return flashSalePoMapper.selectByPrimaryKey(id);
    }

    public int setFlashSaleDate(long id, LocalDateTime dateTime){
        FlashSalePo po = new FlashSalePo();
        po.setId(id);
        po.setGmtModified(LocalDateTime.now());
        po.setFlashDate(dateTime);
        return flashSalePoMapper.updateByPrimaryKeySelective(po);
    }

    public int addFlashSaleItem(FlashSaleItemPo po){
        return flashSaleItemPoMapper.insert(po);
    }

    public int deleteFlashSaleItem(long id){
        return flashSaleItemPoMapper.deleteByPrimaryKey(id);
    }
}
