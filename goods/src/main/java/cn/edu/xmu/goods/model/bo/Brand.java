package cn.edu.xmu.goods.model.bo;
import cn.edu.xmu.goods.model.vo.BrandRetVo;
import cn.edu.xmu.goods.model.vo.BrandSimpleRetVo;
import cn.edu.xmu.goods.model.vo.BrandVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.goods.model.po.BrandPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public  class Brand implements VoObject, Serializable {
    private Long id;
    private String name;
    private String imageUrl;
    private String detail;

    public Brand() {
    }
    public Brand(BrandPo po){
        this.id = po.getId();
        this.name = po.getName();
        this.imageUrl = po.getImageUrl();
        this.detail = po.getDetail();
    }

    @Override
    public Object createVo() {
        return new BrandRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return new BrandSimpleRetVo(this);
    }
    public BrandPo createUpdatePo(BrandVo vo){
        BrandPo po = new BrandPo();
        po.setId(this.getId());
        po.setName(vo.getName());
        po.setDetail(vo.getDetail());
        return po;
    }
    public BrandPo gotBrandPo() {
        BrandPo po = new BrandPo();
        po.setId(this.getId());
        po.setName(this.getName());
        po.setImageUrl(this.getImageUrl());
        po.setDetail(this.getDetail());
        return po;
    }
    public SimpleBrand createSimpleBrand(){
        return new SimpleBrand(this.getId(),this.getName(),this.getImageUrl());
    }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class SimpleBrand{
    private Long id;
    private String name;
    private String imageUrl;
}
