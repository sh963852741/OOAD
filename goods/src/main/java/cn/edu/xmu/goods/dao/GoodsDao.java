package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.CategoryPoMapper;
import cn.edu.xmu.goods.mapper.FloatPricePoMapper;
import cn.edu.xmu.goods.mapper.SKUPoMapper;
import cn.edu.xmu.goods.mapper.SPUPoMapper;
import cn.edu.xmu.goods.model.bo.FloatPrice;
import cn.edu.xmu.goods.model.bo.Good;
import cn.edu.xmu.goods.model.bo.Sku;
import cn.edu.xmu.goods.model.bo.Spu;
import cn.edu.xmu.goods.model.po.*;
import cn.edu.xmu.goods.model.vo.FloatPriceRetVo;
import cn.edu.xmu.goods.model.vo.SkuSelectReturnVo;
import cn.edu.xmu.goods.model.vo.SkuSelectVo;
import cn.edu.xmu.goods.model.vo.SkuSimpleRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Yifei Wang
 * @Date: 2020/11/25 16:25
 **/
@Repository
public class GoodsDao {

    private  static  final Logger logger = LoggerFactory.getLogger(GoodsDao.class);

    @Autowired
    SKUPoMapper skuPoMapper;
    @Autowired
    SPUPoMapper spuPoMapper;
    @Autowired
    FloatPricePoMapper floatPricePoMapper;
    @Autowired
    CategoryPoMapper categoryPoMapper;

    /**
     * 功能描述: 获取商品的所有状态
     * @Param: []
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/25 20:10
     */
    public ReturnObject getGoodState(){
        List<Map<String, Object>> stateList=new ArrayList<>();
        for (Good.State enum1 : Good.State.values()) {
            Map<String,Object> temp=new HashMap<>();
            temp.put("code",enum1.getCode());
            temp.put("name",enum1.getDescription());
            stateList.add(temp);
        }
        return new ReturnObject<>(stateList);
    }

    /**
     * 功能描述: 查询所有Sku
     * @Param: [vo, page, pageSize]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/25 21:03
     */
    public ReturnObject getAllSkus(SkuSelectVo vo,Integer page,Integer pageSize){
        SKUPoExample example =new SKUPoExample();
        SKUPoExample.Criteria criteria=example.createCriteria();
        if(null!=vo.getSkuSn() && !"".equals(vo.getSkuSn())){
            criteria.andSkuSnEqualTo(vo.getSkuSn());
        }
        if(null!=vo.getSpuId()){
            criteria.andGoodsSpuIdEqualTo(vo.getSpuId());
        }
        List<SKUPo> skuPoList =new ArrayList<>();
        List<SkuSimpleRetVo> skuSimpleRetVos = new ArrayList<>();
        try {
            PageHelper.startPage(page, pageSize);
            skuPoList = skuPoMapper.selectByExample(example);
            for (SKUPo po : skuPoList) {
                Sku sku = new Sku(po);

                //查询浮动价格
                FloatPricePoExample example1=new FloatPricePoExample();
                FloatPricePoExample.Criteria criteria1=example1.createCriteria();
                criteria1.andBeginTimeLessThanOrEqualTo(LocalDateTime.now());
                criteria1.andEndTimeGreaterThan(LocalDateTime.now());
                criteria1.andGoodsSkuIdEqualTo(po.getId());
                List<FloatPricePo> pricePos=floatPricePoMapper.selectByExample(example1);

                if(pricePos.size()==0){
                    //没查到浮动价格
                    sku.setPrice(po.getOriginalPrice());
                }else{
                    //查询到浮动价格
                    sku.setPrice(pricePos.get(0).getActivityPrice());
                }
                skuSimpleRetVos.add(sku.createSimpleVo());
            }
        }
        catch (Exception e) {
            logger.error("selectAllSkus: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        PageInfo<SKUPo> skuPoPageInfo=PageInfo.of(skuPoList);
        SkuSelectReturnVo retVo=new SkuSelectReturnVo();
        retVo.setList(skuSimpleRetVos);
        retVo.setPage((long)page);
        retVo.setPageSize((long)pageSize);
        retVo.setPages((long)skuPoPageInfo.getPages());
        retVo.setTotal(skuPoPageInfo.getTotal());
        return new ReturnObject<>(retVo);
    }

    /**
     * 功能描述: 根据id获取sku
     * @Param: [id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/26 16:43
     */
    public ReturnObject getSkuById(Long id){
        SKUPo skuPo;
        try {
            skuPo = skuPoMapper.selectByPrimaryKey(id);
        }catch (Exception e){
            logger.error("selectSkuDetails: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        if(skuPo==null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        Sku sku=new Sku(skuPo);

        return new ReturnObject<>(sku);
    }

    /**
     * 功能描述: 更新sku
     * @Param: [sku]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/26 17:26
     */
    public ReturnObject updateSku(Sku sku) {
        SKUPo skuPo=sku.createPo();
        int ret;
        try{
            ret=skuPoMapper.updateByPrimaryKeySelective(skuPo);
        }catch (Exception e){
            logger.error("updateSkuImg: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        if (ret == 0) {
            logger.debug("updateSku: update fail. sku id: " + sku.getId());
            return new ReturnObject(ResponseCode.FIELD_NOTVALID);
        } else {
            logger.debug("updateSku: update sku success : " + sku.toString());
            return new ReturnObject();
        }
    }

    /**
     * 功能描述: 通过SkuId获取ShopId
     * @Param: id
     * @Return: ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/26 20:16
     */
    public ReturnObject getShopIdBySkuId(Long id){
        try {
            SKUPo skuPo = skuPoMapper.selectByPrimaryKey(id);
            if(skuPo==null){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            SPUPo spuPo=spuPoMapper.selectByPrimaryKey(skuPo.getGoodsSpuId());
            if(spuPo==null){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            return new ReturnObject(spuPo.getShopId());
        }catch (Exception e){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    /**
     * 功能描述: 通过SpuId获取ShopId
     * @Param: id
     * @Return: ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/26 20:16
     */
    public ReturnObject getShopIdBySpuId(Long id){
        try {
            SPUPo spuPo=spuPoMapper.selectByPrimaryKey(id);
            if(spuPo==null){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            return new ReturnObject(spuPo.getShopId());
        }catch (Exception e){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    /**
     * 功能描述: 获取spu
     * @Param: [longValue]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 17:10
     */
    public ReturnObject getSpuById(Long id) {
        try {
            SPUPo spuPo=spuPoMapper.selectByPrimaryKey(id);
            if(spuPo==null){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            return new ReturnObject(spuPo);
        }catch (Exception e){
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    /**
     * 功能描述: 新建spu
     * @Param: [po]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 17:29
     */
    public ReturnObject newSpu(SPUPo po) {
        try{
            int ret;
            ret=spuPoMapper.insertSelective(po);
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
     * 功能描述: 更新spu
     * @Param: [updateSpu]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 19:00
     */
    public ReturnObject updateSpu(Spu updateSpu) {
        SPUPo spuPo=updateSpu.createPo();
        int ret;
        try{
            ret=spuPoMapper.updateByPrimaryKeySelective(spuPo);
        }catch (Exception e){
            logger.error("updateSkuImg: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        if (ret == 0) {
            logger.debug("updateSku: update fail. spu id: " + updateSpu.getId());
            return new ReturnObject(ResponseCode.FIELD_NOTVALID);
        } else {
            logger.debug("updateSku: update spu success : " + updateSpu.toString());
            return new ReturnObject();
        }

    }

    /**
     * 功能描述: 新建浮动价格
     * @Param: [po]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 19:09
     */
    public ReturnObject newFloatPrice(FloatPricePo po) {
        try{
            po.setGmtCreate(LocalDateTime.now());
            po.setGmtModified(LocalDateTime.now());
            int ret;
            ret=floatPricePoMapper.insertSelective(po);
            if(ret ==0){
                return new ReturnObject(ResponseCode.FIELD_NOTVALID);
            }else{
                FloatPrice bo=new FloatPrice(po);
                FloatPriceRetVo vo=bo.createVo();
                vo.setModifiedBy(po.getCreatedBy());
                return new ReturnObject(vo);
            }
        }catch (Exception e){
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }
    
    /**
     * 功能描述: 更新floatPrice
     * @Param: 
     * @Return: 
     * @Author: Yifei Wang
     * @Date: 2020/11/27 19:44
     */
    public ReturnObject updateFloatPrice(FloatPrice floatPrice){
        FloatPricePo po=floatPrice.createPo();
        int ret;
        try{
            ret=floatPricePoMapper.updateByPrimaryKeySelective(po);
        }catch (Exception e){
            logger.error("updateFLoatPrice: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        if (ret == 0) {
            logger.debug("updateFloatPrice: update fail. id: " + po.getId());
            return new ReturnObject(ResponseCode.FIELD_NOTVALID);
        } else {
            logger.debug("updateFloatPrice: update success : " + po.toString());
            return new ReturnObject();
        }
    }

    /**
     * 功能描述: 通过浮动价格id获取商铺id
     * @Param:
     * @Return:
     * @Author: Yifei Wang
     * @Date: 2020/11/27 19:58
     */
    public ReturnObject getShopIdByFloatPriceId(Long id){
        try {
            FloatPricePo po = floatPricePoMapper.selectByPrimaryKey(id);
            if(po==null){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            return getShopIdBySkuId(po.getGoodsSkuId());
        }catch (Exception e){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

}