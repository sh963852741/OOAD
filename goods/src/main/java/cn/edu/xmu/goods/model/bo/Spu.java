package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.dao.BrandDao;
import cn.edu.xmu.goods.dao.CategoryDao;
import cn.edu.xmu.goods.dao.GoodsDao;
import cn.edu.xmu.goods.dao.ShopDao;
import cn.edu.xmu.goods.model.po.SPUPo;
import cn.edu.xmu.goods.model.vo.SkuSimpleRetVo;
import cn.edu.xmu.goods.model.vo.SpuRetVo;
import cn.edu.xmu.goods.utility.SpringContextHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Spu {

    /**
     * 后台SPU状态
     */
    public enum State {
        OFFSHELF(0,"未上架"),
        DELETE(6, "已删除"),
        NORM(4, "正常");


        private static final Map<Integer, Spu.State> stateMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            stateMap = new HashMap();
            for (Spu.State enum1 : values()) {
                stateMap.put(enum1.code, enum1);
            }
        }

        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static Spu.State getTypeByCode(Integer code) {
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

    private Long brandId;

    private Long categoryId;

    private Long freightId;

    private Long shopId;

    private String goodsSn;

    private String detail;

    private String imageUrl;

    private Byte state;

    private String spec;

    private Byte disabled;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public Spu(SPUPo po){
        this.setId(po.getId());
        this.setName(po.getName());
        this.setBrandId(po.getBrandId());
        this.setCategoryId(po.getCategoryId());
        this.setFreightId(po.getFreightId());
        this.setShopId(po.getShopId());
        this.setGoodsSn(po.getGoodsSn());
        this.setDetail(po.getDetail());
        this.setImageUrl(po.getImageUrl());
        this.setState(po.getState());
        this.setSpec(po.getSpec());
        this.setGmtCreate(po.getGmtCreate());
        this.setGmtModified(po.getGmtModified());
        this.setDisabled(po.getState());
    }

    public SPUPo createPo(){
        SPUPo po=new SPUPo();
        po.setId(this.getId());
        po.setName(this.getName());
        po.setBrandId(this.getBrandId());
        po.setCategoryId(this.getCategoryId());
        po.setFreightId(this.getFreightId());
        po.setShopId(this.getShopId());
        po.setGoodsSn(this.getGoodsSn());
        po.setDetail(this.getDetail());
        po.setImageUrl(this.getImageUrl());
        po.setState(this.getState());
        po.setSpec(this.getSpec());
        po.setGmtCreate(this.getGmtCreate());
        po.setGmtModified(this.getGmtModified());
        po.setDisabled(this.getState());
        return po;
    }

    public SpuRetVo createVo(){
        SpuRetVo vo=new SpuRetVo();
        vo.setId(this.getId());
        vo.setDetail(this.getDetail());
        vo.setGoodsSn(this.getGoodsSn());
        vo.setDisable(this.getDisabled());
        vo.setImageUrl(this.getImageUrl());
        vo.setName(this.getName());
        vo.setGmtCreate(this.getGmtCreate());
        vo.setGmtModified(this.getGmtModified());
        vo.setState(this.getState());
        vo.setSpec(this.getSpec());
        vo.setBrand(this.getSimpleBrandVo());
        vo.setCategory(this.getSimpleCategory());
        vo.setFreight(this.getSimpleFreight());
        vo.setShop(this.getSimpleShop());
        vo.setSkuList(this.getSimpleSkus());
        return vo;
    }

    //获取简单的brand
    private Map<String,Object> getSimpleBrandVo(){
        Map<String,Object> map=new HashMap<>();
        BrandDao bean= SpringContextHelper.getBean(BrandDao.class);
        if(this.getBrandId()==null){
            return null;
        }
        ReturnObject ret=bean.getBrandById(this.getBrandId());
        if(ret.getCode()!=ResponseCode.OK){
            return null;
        }
        Brand brand=(Brand)ret.getData();
        map.put("id",brand.getId());
        map.put("name",brand.getName());
        map.put("imageUrl",brand.getImageUrl());
        return map;
    }

    private Map<String,Object> getSimpleShop(){
        Map<String,Object>map=new HashMap<>();
        ShopDao shopDao=SpringContextHelper.getBean(ShopDao.class);
        if(this.getShopId()==null){
            return null;
        }
        ReturnObject ret=shopDao.getShopById(this.getShopId());
        if(ret.getCode()!=ResponseCode.OK){
            return null;
        }
        Shop shop=(Shop)ret.getData();
        map.put("id",shop.getId());
        map.put("name",shop.getName());
        return map;
    }

    private Map<String,Object> getSimpleFreight(){
        return null;
    }

    private List<SkuSimpleRetVo> getSimpleSkus(){
        GoodsDao goodsDao=SpringContextHelper.getBean(GoodsDao.class);
        ReturnObject ret=goodsDao.getSkusBySpu(this.getId());
        if(ret.getCode()!=ResponseCode.OK){
            return null;
        }
        return (List<SkuSimpleRetVo>) ret.getData();
    }

    //获取简单的category
    private Map<String,Object> getSimpleCategory(){
        Map<String,Object>map=new HashMap<>();
        CategoryDao categoryDao=SpringContextHelper.getBean(CategoryDao.class);
        if(this.getCategoryId()==null){
            return null;
        }
        ReturnObject ret=categoryDao.getCategoryById(this.getCategoryId());
        if(ret.getCode()!= ResponseCode.OK){
            return null;
        }
        Category category=(Category) ret.getData();
        map.put("id",category.getId());
        map.put("name",category.getName());
        return map;
    }


}

