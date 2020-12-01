package cn.edu.xmu.goods.model.bo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class PresaleActivity {
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum PresaleStatus {
        PENDING((byte)0,"尚未开始"),
        DEPOSIT((byte)1,"支付定金"),
        BALANCE((byte)2,"支付尾款"),
        FINISHED((byte)3,"已结束"),
        CANCELED((byte)4,"已取消");

        private static final Map<Byte, PresaleStatus> typeMap;
        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            typeMap = new HashMap();
            for (PresaleStatus enum1 : values()) {
                typeMap.put(enum1.code, enum1);
            }
        }

        private byte code;
        @JsonProperty("name")
        private String description;

        PresaleStatus(byte code, String description) {
            this.code = code;
            this.description = description;
        }

        public static PresaleStatus getTypeByCode(Integer code) {
            return typeMap.get(code);
        }

        public Byte getCode() {
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
