package cn.edu.xmu.goods.model.bo;


import cn.edu.xmu.goods.model.po.ShopPo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.goods.model.vo.ShopRetVo;

import cn.edu.xmu.goods.model.vo.ShopVo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Shop implements VoObject, Serializable {

    private Long id;
    private String name;
    private Byte state;

    public Shop() {
    }
    public Shop(ShopPo po) {
        this.id = po.getId();
        this.name = po.getName();
        this.state = po.getState();
    }
    @Override
    public Object createVo() {
        return new ShopRetVo(this);
    }
    @Override
    public Object createSimpleVo() {
        return new ShopRetVo(this);
    }
    public ShopPo createUpdatePo(ShopVo vo){
        ShopPo po = new ShopPo();
        po.setId(this.getId());
        po.setName(vo.getName());
        po.setState(vo.getState());
        return po;
    }
    public ShopPo gotShopPo() {
        ShopPo po = new ShopPo();
        po.setId(this.getId());
        po.setName(this.getName());
        po.setState(this.getState());
        return po;
    }

}
