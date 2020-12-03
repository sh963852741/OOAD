package cn.edu.xmu.goods.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class GrouponActivity {
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum GrouponStatus {
        NEW((byte)0,"发布"),
        RUNNING((byte)1,"上线"),
        CANCELED((byte)2,"已下线"),
        SUCCESS((byte)3,"已成团"),
        FAILURE((byte)4,"未成团");

        private static final Map<Byte, GrouponActivity.GrouponStatus> typeMap;
        static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
            typeMap = new HashMap();
            for (GrouponActivity.GrouponStatus enum1 : values()) {
                typeMap.put(enum1.code, enum1);
            }
        }

        private byte code;
        private String description;

        GrouponStatus(byte code, String description) {
            this.code = code;
            this.description = description;
        }

        public static GrouponActivity.GrouponStatus getTypeByCode(Integer code) {
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
    private GrouponActivity.GrouponStatus status;
    private Long shopId;
    private Long spuId;
    private Long quantity;
    private String strategy;
}
