package cn.edu.xmu.goods.service.dubbo.implement;


import cn.edu.xmu.goods.dao.GoodsDao;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.bo.Sku;
import cn.edu.xmu.goods.model.po.SKUPo;
import cn.edu.xmu.goods.model.vo.SkuRetVo;
import cn.edu.xmu.goods.service.GoodsService;
import cn.edu.xmu.goods.service.ShopService;
import cn.xmu.edu.goods.client.IGoodsService;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.xmu.edu.goods.client.dubbo.OrderItemDTO;
import cn.xmu.edu.goods.client.dubbo.ShopDTO;
import cn.xmu.edu.goods.client.dubbo.SkuDTO;
import cn.xmu.edu.goods.client.dubbo.SpuDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@DubboService(version = "0.0.1-SNAPSHOT")
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private GoodsDao goodsDao;

    @Override
    public Long getPrice(Long skuId) {
        if(skuId == null){
            return null;
        }
        ReturnObject<Long> ret = goodsService.getActicityPrice(skuId);
        if(ret.getCode() != ResponseCode.OK){
            return null;
        }
        return ret.getData();
    }

    @Override
    public Map<ShopDTO, List<OrderItemDTO>> classifySku(List<OrderItemDTO> orderItemDTOS) {
        if(orderItemDTOS == null){
            return null;
        }
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
    public SkuDTO getSku(Long skuId) {
        if(skuId == null){
            return null;
        }
        ReturnObject<Sku> skuRet=goodsDao.getSkuById(skuId);
        if(skuRet.getCode()!=ResponseCode.OK){
            return new SkuDTO();
        }
        SkuDTO skuDTO =new SkuDTO();
        Sku sku=skuRet.getData();
        skuDTO.setConfiguration(sku.getConfiguration());
        skuDTO.setDisable(sku.getDisable());
        skuDTO.setId(sku.getId());
        skuDTO.setImageUrl(sku.getImageUrl());
        skuDTO.setInventory(sku.getInventory());
        skuDTO.setName(sku.getName());
        skuDTO.setOriginalPrice(sku.getOriginalPrice());
        skuDTO.setSkuSn(sku.getSkuSn());
        skuDTO.setWeight(sku.getWeight());
        skuDTO.setGmtCreate(sku.getGmtCreate());
        skuDTO.setGmtModified(sku.getGmtModified());
        skuDTO.setDetail(sku.getDetail());
        skuDTO.setGoodsSpuId(sku.getGoodsSpuId());
        skuDTO.setPrice(sku.getPrice());
        return skuDTO;
    }

    @Override
    public SkuDTO getSimpleSku(Long skuId) {
        if(skuId == null){
            return null;
        }
        ReturnObject<Sku> skuRet=goodsDao.getSkuById(skuId);
        if(skuRet.getCode()!=ResponseCode.OK){
            return new SkuDTO();
        }
        SkuDTO skuDTO =new SkuDTO();
        Sku sku=skuRet.getData();
        skuDTO.setName(sku.getName());
        skuDTO.setPrice(sku.getPrice());
        return skuDTO;
    }

    @Override
    public SpuDTO getSimpleSpuById(Long spuId) {
        if(spuId == null){
            return null;
        }
       ReturnObject<HashMap<String,Object>> ret = goodsService.getSimpleSpuById(spuId);
       if(ret.getCode() != ResponseCode.OK){
           return null;
       }
       SpuDTO dto = new SpuDTO();
       dto.setId((Long)ret.getData().get("id"));
       dto.setName((String)ret.getData().get("name"));
       dto.setGoodsSn((String)ret.getData().get("goodsSn"));
       dto.setImageUrl((String)ret.getData().get("imageUrl"));
       dto.setDisable((Byte)ret.getData().get(""));
       return dto;
    }


    @Override
    public ShopDTO getShopBySKUId(Long skuId) {
        if(skuId == null){
            return null;
        }
        ReturnObject<Long> shopIdRet = goodsService.getShopIdBySkuId(skuId);
        log.debug("shopIdRet:" + shopIdRet.getData());
        if(shopIdRet.getCode() != ResponseCode.OK){
            return null;
        }
        Long shopId=shopIdRet.getData();
        ReturnObject<Shop> shopRet = shopService.getShopByShopId(shopId);
        log.debug("shopRet:" + shopRet.getData());
        if(shopRet.getCode() != ResponseCode.OK){
            return null;
        }
        Shop shop= shopRet.getData();
        ShopDTO shopDTO =shop.createDTO();
        log.debug("shopDTO:" + shopDTO);
        return shopDTO;
    }
}
