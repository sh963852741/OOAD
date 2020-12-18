package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@ApiModel(value = "sku搜索视图")
public class SkuSelectVo {

    private Long shopId;

    private String spuSn;

    private Long spuId;

    private String skuSn;

    public  Boolean isNull(){
        return shopId==null && spuSn==null && spuId == null && skuSn == null;
    }
}
