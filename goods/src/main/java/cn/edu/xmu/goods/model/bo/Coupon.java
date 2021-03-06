package cn.edu.xmu.goods.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Coupon {
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum CouponStatus {
        UNCLAIMED((byte)0,"未领取"),
        NORMAL((byte)1,"已领取"),
        USED((byte)2,"已使用"),
        EXPIRED((byte)3,"已失效");

        private static final Map<Byte, Coupon.CouponStatus> typeMap;
        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            typeMap = new HashMap();
            for (Coupon.CouponStatus enum1 : values()) {
                typeMap.put(enum1.code, enum1);
            }
        }

        private byte code;
        @JsonProperty("name")
        private String name;

        CouponStatus(byte code, String description) {
            this.code = code;
            this.name = description;
        }

        public static Coupon.CouponStatus getTypeByCode(Integer code) {
            return typeMap.get(code);
        }

        public Byte getCode() {
            return code;
        }

        public String getDescription() {
            return name;
        }
    }

    private Long id;
    private String sn;
    private String name;
    private Long customerId;
    private Long activityId;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;

}
