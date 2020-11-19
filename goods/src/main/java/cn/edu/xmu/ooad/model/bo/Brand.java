package cn.edu.xmu.ooad.model.bo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.model.po.BrandPo;
import cn.edu.xmu.ooad.model.vo.BrandRetVo;
import cn.edu.xmu.ooad.model.vo.BrandSimpleRetVo;
import cn.edu.xmu.ooad.model.vo.BrandVo;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

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
}
