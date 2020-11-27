package cn.edu.xmu.goods.model.bo;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Coupon {
    public enum CouponStatus {
        PENDING((byte)0,"尚未开始"),
        AVAILABLE((byte)1,"可用"),
        EXPIRED((byte)2,"已过期"),
        CANCELLED((byte)3,"已作废");

        private static final Map<Byte, Coupon.CouponStatus> typeMap;
        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            typeMap = new HashMap();
            for (Coupon.CouponStatus enum1 : values()) {
                typeMap.put(enum1.code, enum1);
            }
        }

        private byte code;
        private String description;

        CouponStatus(byte code, String description) {
            this.code = code;
            this.description = description;
        }

        public static Coupon.CouponStatus getTypeByCode(Integer code) {
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
    private String sn;
    private String name;
    private Long customerId;
    private Long activityId;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;

}
