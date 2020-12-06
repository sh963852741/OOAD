package cn.edu.xmu.oomall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhibin lan
 * @date 2020-11-23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculateFreightRequest {
    Long skuId;

    Integer count;
}
