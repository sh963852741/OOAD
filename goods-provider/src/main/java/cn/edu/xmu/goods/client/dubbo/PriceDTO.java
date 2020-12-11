package cn.edu.xmu.goods.client.dubbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceDTO implements Serializable {

    private String name;
    private Long SkuId;
    //如果尾款为0 则表示是价格
    private Long prePrice;

    //尾款
    private Long finalPrice;

    PriceDTO(SkuDTO dto){
        this.setName(dto.getName());
        this.setSkuId(dto.getId());
        this.setPrePrice(dto.getPrice());
    }
}
