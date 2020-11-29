package cn.edu.xmu.goods.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class GrouponActivity {
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum GrouponStatus {
        PENDING((byte)0,"尚未开始"),
        RUNNING((byte)1,"活动进行中"),
        FINISHED((byte)2,"已结束"),
        CANCELED((byte)3,"已取消");

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
