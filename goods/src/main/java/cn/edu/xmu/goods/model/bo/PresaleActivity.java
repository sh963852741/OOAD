package cn.edu.xmu.goods.model.bo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class PresaleActivity {
    public enum PresaleStatus {
        PENDING(0,"尚未开始"),
        DEPOSIT(1,"支付定金"),
        BALANCE(2,"支付尾款"),
        FINISHED(3,"已结束"),
        CANCELED(4,"已取消");

        private static final Map<Integer, PresaleStatus> typeMap;
        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            typeMap = new HashMap();
            for (PresaleStatus enum1 : values()) {
                typeMap.put(enum1.code, enum1);
            }
        }

        private int code;
        private String description;

        PresaleStatus(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public static PresaleStatus getTypeByCode(Integer code) {
            return typeMap.get(code);
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
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private LocalDateTime couponTime;
    private PresaleStatus status;
    private Long shopId;
    private Long spuId;
    private Long quantity;
    private Integer advancePayPrice;
    private Integer restPayPrice;

}
