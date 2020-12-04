package cn.edu.xmu.goods.service.dubbo;

import cn.edu.xmu.goods.model.bo.dubbo.OrderItem;
import cn.edu.xmu.goods.model.bo.dubbo.Shop;

import java.util.List;
import java.util.Map;

/**
 * @author xincong yao
 * @date 2020-11-17
 */
public interface IShopService {

	/**
	 * 获取商铺消息
	 * @param skuId
	 * @return
	 */
	Shop getShop(Long skuId);

}
