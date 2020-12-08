package cn.edu.xmu.goods.client.dubbo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkuDTO implements Serializable {

    /**
     * 后台Sku状态
     */
    public enum State {
        FORBID(6, "已删除"),
        NORM(4, "上架"),
        OFFSHELF(0,"未上架");
        private static final Map<Integer, State> stateMap;

        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            stateMap = new HashMap<>();
            for (State enum1 : values()) {
                stateMap.put(enum1.code, enum1);
            }
        }

        private int code;
        private String description;

        State(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static State getTypeByCode(Integer code) {
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

    private String detail;

    private String skuSn;

    private String imageUrl;

    private Integer inventory;

    private Long originalPrice;

    private Long price;

    private String configuration;

    private Long weight;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private Long goodsSpuId;

    private Byte disable;

}




