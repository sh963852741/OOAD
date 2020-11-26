package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.FloatPricePoMapper;
import cn.edu.xmu.goods.mapper.SKUPoMapper;
import cn.edu.xmu.goods.mapper.SPUPoMapper;
import cn.edu.xmu.goods.model.bo.Good;
import cn.edu.xmu.goods.model.bo.Sku;
import cn.edu.xmu.goods.model.po.FloatPricePo;
import cn.edu.xmu.goods.model.po.FloatPricePoExample;
import cn.edu.xmu.goods.model.po.SKUPo;
import cn.edu.xmu.goods.model.po.SKUPoExample;
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

}
