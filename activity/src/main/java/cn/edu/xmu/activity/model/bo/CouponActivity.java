package cn.edu.xmu.activity.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CouponActivity {
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum CouponStatus {
        OFFLINE((byte)0,"已下线"),
        ONLINE((byte)1,"已上线"),
        DELETE((byte)2,"已删除");

        private static final Map<Byte, CouponStatus> typeMap;
        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            typeMap = new HashMap();
            for (CouponStatus enum1 : values()) {
                typeMap.put(enum1.code, enum1);
            }
        }

        private byte code;
        private String description;

        CouponStatus(byte code, String description) {
            this.code = code;
            this.description = description;
        }

        public static CouponStatus getTypeByCode(Integer code) {
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
    private Byte state;
    private Long shopId;
    private Long quantity;
    private Byte quantityType;
    private Integer validTerm;
    private String imgUrl;
    private String strategy;
    private String createdBy;
    private String modifyBy;
}
