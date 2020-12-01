package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Shop;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShopRetVo {
    private Long id;
    private String name;
    private byte state;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
