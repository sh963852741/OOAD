package cn.edu.xmu.goods.model.bo;


import cn.edu.xmu.goods.model.po.FloatPricePo;
import cn.edu.xmu.goods.model.vo.FloatPriceRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FloatPrice implements VoObject {

    /**
     * 后台Sku状态
     */
    public enum State {
        VALID(0, "失效"),
        NORM(1, "正常");

        private static final Map<Integer, FloatPrice.State> stateMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            stateMap = new HashMap();
            for (FloatPrice.State enum1 : values()) {
                stateMap.put(enum1.code, enum1);
            }
        }

        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static FloatPrice.State getTypeByCode(Integer code) {
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

    private Long goodsSkuId;

    private Long activityPrice;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private Integer quantity;

    private Long createdBy;

    private Long invalidBy;

    private Byte valid;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;

    public FloatPrice(FloatPricePo po){
        this.setId(po.getId());
        this.setGoodsSkuId(po.getGoodsSkuId());
        this.setActivityPrice(po.getActivityPrice());
        this.setBeginTime(po.getBeginTime());
        this.setEndTime(po.getEndTime());
        this.setQuantity(po.getQuantity());
        this.setCreatedBy(po.getCreatedBy());
        this.setInvalidBy(po.getInvalidBy());
        this.setValid(po.getValid());
        this.setGmtCreated(po.getGmtCreated());
        this.setGmtModified(po.getGmtModified());
    }

    public FloatPricePo createPo(){
        FloatPricePo po=new FloatPricePo();
        po.setId(this.getId());
        po.setGoodsSkuId(this.getGoodsSkuId());
        po.setActivityPrice(this.getActivityPrice());
        po.setBeginTime(this.getBeginTime());
        po.setEndTime(this.getEndTime());
        po.setQuantity(this.getQuantity());
        po.setCreatedBy(this.getCreatedBy());
        po.setInvalidBy(this.getInvalidBy());
        po.setValid(this.getValid());
        po.setGmtCreated(this.getGmtCreated());
        po.setGmtModified(this.getGmtModified());
        return po;
    }

    @Override
    public FloatPriceRetVo createVo() {
        FloatPriceRetVo retVo=new FloatPriceRetVo();
        retVo.setActivityPrice(this.getActivityPrice());
        retVo.setBeginTime(this.getBeginTime());
        retVo.setCreateBy(this.getCreatedBy());
        retVo.setEndTime(this.getEndTime());
        retVo.setValid(this.getValid());
        retVo.setId(this.getId());
        retVo.setGmtCreated(this.getGmtCreated());
        retVo.setGmtModified(this.getGmtModified());
        retVo.setQuantity(this.getQuantity());
        return retVo;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
