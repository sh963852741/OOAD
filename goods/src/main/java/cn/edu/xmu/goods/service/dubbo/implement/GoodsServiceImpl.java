package cn.edu.xmu.goods.service.dubbo.implement;


import cn.edu.xmu.goods.dao.GoodsDao;
import cn.edu.xmu.goods.model.po.SKUPo;
import cn.edu.xmu.goods.model.vo.SkuRetVo;
import cn.edu.xmu.goods.service.GoodsService;
import cn.xmu.edu.goods.client.IGoodsService;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.xmu.edu.goods.client.dubbo.OrderItemDTO;
import cn.xmu.edu.goods.client.dubbo.ShopDTO;
import cn.xmu.edu.goods.client.dubbo.SkuDTO;
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
    public SkuDTO getSku(Long skuId) {
        ReturnObject<SKUPo> skuRet=goodsDao.getSkuById(skuId);
        if(skuRet.getCode()!=ResponseCode.OK){
            return new SkuDTO();
        }
        SkuDTO skuDTO =new SkuDTO();
        SKUPo skuPo=skuRet.getData();
        skuDTO.setConfiguration(skuPo.getConfiguration());
        skuDTO.setDisable(skuPo.getDisabled());
        skuDTO.setId(skuPo.getId());
        skuDTO.setImageUrl(skuPo.getImageUrl());
        skuDTO.setInventory(skuPo.getInventory());
        skuDTO.setName(skuPo.getName());
        skuDTO.setOriginalPrice(skuPo.getOriginalPrice());
        skuDTO.setSkuSn(skuPo.getSkuSn());
        skuDTO.setWeight(skuPo.getWeight());
        skuDTO.setGmtCreate(skuPo.getGmtCreate());
        skuDTO.setGmtModified(skuPo.getGmtModified());
        skuDTO.setDetail(skuPo.getDetail());
        skuDTO.setGoodsSpuId(skuPo.getGoodsSpuId());
        return skuDTO;
    }

    @Override
    public SpuDTO getSimpleSpuById(Long spuId) {
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
    public Long getShopIdBySkuId(long skuId) {
        return null;
    }


}
