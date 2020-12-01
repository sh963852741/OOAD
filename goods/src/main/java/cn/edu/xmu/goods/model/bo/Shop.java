package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.ShopPo;
import cn.edu.xmu.goods.model.vo.ShopRetVo;
import cn.edu.xmu.goods.model.vo.ShopSimpleRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shop implements VoObject {
    public enum State {
        FORBID(3, "逻辑删除"),
        ONLINE(4, "上线"),
        OFFLINE(5,"下线"),
        MODIFIED(6,"待修改"),
        EXAME(7,"待审核");
        private static final Map<Integer, Shop.State> stateMap;
        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            stateMap = new HashMap();
            for (Shop.State enum1 : values()) {
                stateMap.put(enum1.code, enum1);
            }
        }
        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static Shop.State getTypeByCode(Integer code) {
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
    private String createdBy;
    private String modifyBy;

    @Override
    public Object createVo() {
        return null;
    }

    @Override
    public ShopSimpleRetVo createSimpleVo() {
        ShopSimpleRetVo ret= new ShopSimpleRetVo();
        ret.setId(this.getId());
        ret.setName(this.getName());
        ret.setState(this.createSimpleVo().getState());
        return ret;
    }

    public Shop(ShopPo shopPo){
        this.setId(shopPo.getId());
        this.setName(shopPo.getName());
    }

    public ShopPo createPo(){
        ShopPo shopPo=new ShopPo();
        shopPo.setId(this.getId());
        shopPo.setName(this.getName());
        shopPo.setState(this.createSimpleVo().getState());
        return shopPo;
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class SimpleShop {
    private Long id;
    private String name;
    private byte state;
}