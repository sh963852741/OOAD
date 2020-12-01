package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.ShopPo;
import cn.edu.xmu.goods.model.vo.ShopRetVo;
import cn.edu.xmu.goods.model.vo.ShopSimpleRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
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
        NORM(6,"正常"),
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
    private Byte state;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    @Override
    public ShopRetVo createVo(){
        ShopRetVo vo=new ShopRetVo();
        vo.setId(this.getId());
        vo.setName(this.getName());
        vo.setGmtCreate(this.getGmtCreated());
        vo.setGmtModified(this.getGmtModified());
        vo.setState(this.getState());
        return vo;
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
        this.setState(shopPo.getState());
        this.setGmtCreated(shopPo.getGmtCreate());
        this.setGmtModified(shopPo.getGmtModified());
    }

    public ShopPo createPo(){
        ShopPo shopPo=new ShopPo();
        shopPo.setId(this.getId());
        shopPo.setName(this.getName());
        shopPo.setState(this.getState());
        shopPo.setGmtCreate(this.getGmtCreated());
        shopPo.setGmtModified(this.getGmtModified());
        return shopPo;
    }

    /**
     * 根据 errCode 修饰 API 返回对象的 HTTP Status
     * @param returnObject 原返回 Object
     * @return 修饰后的返回 Object
     */
    public static Object decorateReturnObject(ReturnObject returnObject) {
        switch (returnObject.getCode()) {
            case RESOURCE_ID_NOTEXIST:
                // 404：资源不存在
                return new ResponseEntity(
                        ResponseUtil.fail(returnObject.getCode(), returnObject.getErrmsg()),
                        HttpStatus.NOT_FOUND);
            case INTERNAL_SERVER_ERR:
                // 500：数据库或其他严重错误
                return new ResponseEntity(
                        ResponseUtil.fail(returnObject.getCode(), returnObject.getErrmsg()),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            case OK:
                // 200: 无错误
                Object data = returnObject.getData();
                if (data != null){
                    return ResponseUtil.ok(data);
                }else{
                    return ResponseUtil.ok();
                }
            default:
                return ResponseUtil.fail(returnObject.getCode(), returnObject.getErrmsg());
        }
    }

}

/*@Data
@NoArgsConstructor
@AllArgsConstructor
class SimpleShop {
    private Long id;
    private String name;
    private byte state;
}*/