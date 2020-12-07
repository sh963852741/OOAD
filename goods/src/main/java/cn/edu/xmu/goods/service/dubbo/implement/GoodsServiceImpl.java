package cn.edu.xmu.goods.service.dubbo.implement;


import cn.edu.xmu.goods.dao.GoodsDao;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.po.SKUPo;
import cn.edu.xmu.goods.model.vo.SkuRetVo;
import cn.edu.xmu.goods.service.GoodsService;
import cn.xmu.edu.goods.client.IGoodsService;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.xmu.edu.goods.client.dubbo.OrderItemDTO;
import cn.xmu.edu.goods.client.dubbo.ShopDTO;
import cn.xmu.edu.goods.client.dubbo.Sku;
import cn.xmu.edu.goods.client.dubbo.SpuDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DubboService(version = "0.0.1-SNAPSHOT")
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsDao goodsDao;

    @Override
    public Long getPrice(Long skuId) {
        if(skuId == null){
            return null;
        }
        ReturnObject<SkuRetVo> ret = goodsService.getSkuDetails(skuId);
        if(ret.getCode()!= ResponseCode.OK){
            return null;
        }
        return ret.getData().getOriginalPrice();
    }

    @Override
    public Map<ShopDTO, List<OrderItemDTO>> classifySku(List<OrderItemDTO> orderItemDTOS) {
        Map<Long,List<OrderItemDTO>> temp = new HashMap<Long,List<OrderItemDTO>>();
        for(OrderItemDTO orderItemDTO : orderItemDTOS){
            ReturnObject<Long> ret = goodsService.getShopIdBySkuId(orderItemDTO.getId());
            if(ret.getCode() != ResponseCode.OK){
                return null;
            }
            Long shopId = ret.getData();
            if(temp.containsKey(ret.getData())){
                temp.get(shopId).add(orderItemDTO);
            }else{
                List<OrderItemDTO> orderItemDTOList =new ArrayList<>();
                orderItemDTOList.add(orderItemDTO);
                temp.put(shopId, orderItemDTOList);
            }
        }
        Map<ShopDTO,List<OrderItemDTO>> ret = new HashMap<>();
        for (Map.Entry<Long, List<OrderItemDTO>> entry : temp.entrySet()) {
            ShopDTO shopDTO = new ShopDTO(entry.getKey());
            ret.put(shopDTO,entry.getValue());
        }
        return ret;
    }

    @Override
    public Sku getSku(Long skuId) {
        ReturnObject<SKUPo> skuRet=goodsDao.getSkuById(skuId);
        if(skuRet.getCode()!=ResponseCode.OK){
            return new Sku();
        }
        Sku sku=new Sku();
        SKUPo skuPo=skuRet.getData();
        sku.setConfiguration(skuPo.getConfiguration());
        sku.setDisable(skuPo.getDisabled());
        sku.setId(skuPo.getId());
        sku.setImageUrl(skuPo.getImageUrl());
        sku.setInventory(skuPo.getInventory());
        sku.setName(skuPo.getName());
        sku.setOriginalPrice(skuPo.getOriginalPrice());
        sku.setSkuSn(skuPo.getSkuSn());
        sku.setWeight(skuPo.getWeight());
        sku.setGmtCreate(skuPo.getGmtCreate());
        sku.setGmtModified(skuPo.getGmtModified());
        sku.setDetail(skuPo.getDetail());
        sku.setGoodsSpuId(skuPo.getGoodsSpuId());
        return sku;
    }

    @Override
    public SpuDTO getSimpleSpuById(Long spuId) {
        return null;
    }

    @Override
    public Long getShopIdBySkuId(long skuId) {
        return null;
    }


}
