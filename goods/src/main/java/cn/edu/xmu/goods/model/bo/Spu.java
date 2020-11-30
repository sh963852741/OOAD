package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.SPUPo;
import cn.edu.xmu.goods.model.vo.SpuVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Spu {

    /**
     * 后台SPU状态
     */
    public enum State {
        OFFSHELF(2,"下架"),
        DELETE(3, "逻辑删除"),
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

    private LocalDateTime gmtCreated;

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
        this.setGmtCreated(po.getGmtCreate());
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
        po.setGmtCreate(this.getGmtCreated());
        po.setGmtModified(this.getGmtModified());
        po.setDisabled(this.getState());
        return po;
    }
}

