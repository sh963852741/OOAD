package cn.edu.xmu.flashsale.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

public class FlashState {
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum FalshStatus {
        OFFLINE((byte)0,"已下线"),
        ONLINE((byte)1,"已上线"),
        DELETE((byte)2,"已删除");

        private static final Map<Byte, FalshStatus> typeMap;
        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            typeMap = new HashMap();
            for (FalshStatus enum1 : values()) {
                typeMap.put(enum1.code, enum1);
            }
        }

        private byte code;
        private String description;

        FalshStatus(byte code, String description) {
            this.code = code;
            this.description = description;
        }

        public static FalshStatus getTypeByCode(Integer code) {
            return typeMap.get(code);
        }

        public Byte getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }
}
