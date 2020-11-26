package cn.edu.xmu.goods.model.bo;

import java.util.HashMap;
import java.util.Map;

public enum Timeline {
    BEGIN(0,"尚未开始"),
    NEXTDAY(1,"明天开始"),
    RUNNING(2,"正在进行"),
    FINISHED(3,"已经结束");

    private static final Map<Integer, Timeline> typeMap;

    static { //由类加载机制，静态块初始加载对应的枚举属性到map中，而不用每次取属性时，遍历一次所有枚举值
        typeMap = new HashMap();
        for (Timeline enum1 : values()) {
            typeMap.put(enum1.code, enum1);
        }
    }

    private int code;
    private String description;

    Timeline(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static Timeline getTypeByCode(Integer code) {
        return typeMap.get(code);
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
