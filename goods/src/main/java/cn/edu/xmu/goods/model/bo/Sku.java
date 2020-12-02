package cn.edu.xmu.goods.model.bo;


import cn.edu.xmu.goods.dao.GoodsDao;
import cn.edu.xmu.goods.model.po.SKUPo;
import cn.edu.xmu.goods.model.vo.SkuChangeVo;
import cn.edu.xmu.goods.model.vo.SkuRetVo;
import cn.edu.xmu.goods.model.vo.SkuSimpleRetVo;
import cn.edu.xmu.goods.utility.SpringContextHelper;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.time.LocalDateTime;
import java.util.ListIterator;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sku implements VoObject {

    /**
     * 后台Sku状态
     */
    public enum State {
        FORBID(6, "已删除"),
        NORM(4, "上架"),
        OFFSHELF(0,"未上架");
        private static final Map<Integer, Sku.State> stateMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            stateMap = new HashMap();
            for (Sku.State enum1 : values()) {
                stateMap.put(enum1.code, enum1);
            }
        }

        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static Sku.State getTypeByCode(Integer code) {
            return stateMap.get(code);
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }


    private Long id;

    private String name;

    private String detail;

    private String skuSn;

    private String imageUrl;

    private Integer inventory;

    private Long originalPrice;

    private Long price;

    private String configuration;

    private Long weight;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private Long goodsSpuId;

    private Byte disable;

    @Override
    public SkuRetVo createVo() {
        SkuRetVo vo=new SkuRetVo();
        vo.setId(this.getId());
        vo.setConfiguration(this.getConfiguration());
        vo.setDetail(this.getDetail());
        vo.setGmtCreate(this.getGmtCreate().toString());
        vo.setImageUrl(this.getImageUrl());
        vo.setGmtModified(this.getGmtModified().toString());
        vo.setInventory(this.getInventory());
        vo.setName(this.getName());
        vo.setOriginalPrice(this.getOriginalPrice());
        vo.setPrice(this.getPrice());
        vo.setSkuSn(this.getSkuSn());
        vo.setWeight(this.getWeight());
        return vo;
    }

    @Override
    public SkuSimpleRetVo createSimpleVo() {
        SkuSimpleRetVo ret=new SkuSimpleRetVo();
        ret.setId(this.getId());
        ret.setImageUrl(this.getImageUrl());
        ret.setDisable(this.getDisable());
        ret.setInventory(this.getInventory());
        ret.setName(this.getName());
        ret.setOriginalPrice(this.getOriginalPrice());
        ret.setPrice(this.getPrice());
        ret.setSkuSn(this.getSkuSn());
        return ret;
    }

    public Sku(SKUPo skuPo){
        this.setConfiguration(skuPo.getConfiguration());
        this.setDisable(skuPo.getDisabled());
        this.setId(skuPo.getId());
        this.setImageUrl(skuPo.getImageUrl());
        this.setInventory(skuPo.getInventory());
        this.setName(skuPo.getName());
        this.setOriginalPrice(skuPo.getOriginalPrice());
        this.setSkuSn(skuPo.getSkuSn());
        this.setWeight(skuPo.getWeight());
        this.setGmtCreate(skuPo.getGmtCreated());
        this.setGmtModified(skuPo.getGmtModified());
        this.setDetail(skuPo.getDetail());
        this.setGoodsSpuId(skuPo.getGoodsSpuId());
    }

    public Sku(SkuChangeVo vo){
        this.setName(vo.getName());
        this.setOriginalPrice(vo.getOriginalPrice());
        this.setConfiguration(vo.getConfiguration());
        this.setWeight(vo.getWeight());
        this.setInventory(vo.getInventory());
        this.setDetail(vo.getDetail());
    }

    public SKUPo createPo(){
        SKUPo skuPo=new SKUPo();
        skuPo.setId(this.getId());
        skuPo.setName(this.getName());
        skuPo.setImageUrl(this.getImageUrl());
        skuPo.setConfiguration(this.getConfiguration());
        skuPo.setDetail(this.getDetail());
        skuPo.setDisabled(this.getDisable());
        skuPo.setGmtCreated(this.getGmtCreate());
        skuPo.setGmtModified(this.getGmtModified());
        skuPo.setGoodsSpuId(this.getGoodsSpuId());
        skuPo.setOriginalPrice(this.getOriginalPrice());
        skuPo.setSkuSn(this.getSkuSn());
        skuPo.setWeight(this.getWeight());
        if(this.getInventory()!=null)
        skuPo.setInventory(this.getInventory().intValue());
        return skuPo;
    }


}




