package com.xmu.oomall.domain;

import lombok.Data;

@Data
public class SKUPo {
    /*
    SKUid
     */
    private int id;

    /*
    SPUid
     */
    private int spuId;

    /*
    商品SKU名称
     */
    private String name;

    /*
    原始价格（分）
     */
    private int originalPrice;

    /*
    配置（JSON字符串）
     */
    private String configuration;

    /*
    重量（克）
     */
    private int weight;

    /*
    图片链接
     */
    private String imageUrl;

    /*
    库存量
     */
    private int inventory;

    /*
    详细信息
     */
    private String detail;

    /*
    是否禁止访问
     */
    private boolean disabled;
}
