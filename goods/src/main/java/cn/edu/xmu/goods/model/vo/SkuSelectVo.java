package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "sku搜索视图")
public class SkuSelectVo {

    private Long shopId;

    private String spuSn;

    private Long spuId;

    private String skuSn;
}