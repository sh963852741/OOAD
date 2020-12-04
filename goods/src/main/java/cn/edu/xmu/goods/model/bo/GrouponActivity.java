package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.GrouponActivityPo;
import cn.edu.xmu.ooad.model.VoObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class GrouponActivity implements VoObject {
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

        @JsonProperty("name")
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

    @Override
    public Map<String, Object> createVo() {
        Map<String,Object> data=new HashMap<>();
        data.put("id",this.getId());
        data.put("name",this.getName());
        data.put("beginTime",this.getBeginTime().toString());
        data.put("endTime",this.getEndTime().toString());
        return data;
    }

    public GrouponActivity(GrouponActivityPo po){
        this.setId(po.getId());
        this.setName(po.getName());
        this.setBeginTime(po.getBeginTime());
        this.setEndTime(po.getEndTime());
        this.setStatus(GrouponStatus.getTypeByCode(po.getState().intValue()));
        this.setSpuId(po.getGoodsSpuId());
        this.setStrategy(po.getStrategy());
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
