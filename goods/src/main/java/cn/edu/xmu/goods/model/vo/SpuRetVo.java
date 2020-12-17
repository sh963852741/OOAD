package cn.edu.xmu.goods.model.vo;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.*;

@Data
@ApiModel(value = "spu返回视图")
@AllArgsConstructor
@NoArgsConstructor
public class SpuRetVo {

    private Long id;
    private String name;
    private Map<String,Object> brand;
    private Map<String,Object> category;
    private Map<String,Object> freight;
    private Map<String,Object> shop;
    private String goodsSn;
    private String detail;
    private String imageUrl;
    private Byte state;
    private SpecVo spec;
    private List<SkuSimpleRetVo> skuList;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Boolean disable;

}
