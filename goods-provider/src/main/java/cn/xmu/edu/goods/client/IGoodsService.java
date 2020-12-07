package cn.xmu.edu.goods.client;



import cn.xmu.edu.goods.client.dubbo.OrderItemDTO;
import cn.xmu.edu.goods.client.dubbo.ShopDTO;
import cn.xmu.edu.goods.client.dubbo.SkuDTO;
import cn.xmu.edu.goods.client.dubbo.SpuDTO;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yifei Wang
 * @Date: 2020/12/6 10:49
 */
public interface IGoodsService {

	/**
	 * 获取价格
	 * @param skuId
	 * @return
	 */
	Long getPrice(Long skuId);

	/**
	 * 根据sku所属商铺划分orderItem
	 * 返回的OrderItem需要skuId, name, quantity, price
	 * @param orderItemDTOS
	 * @return
	 */
	Map<ShopDTO, List<OrderItemDTO>> classifySku(List<OrderItemDTO> orderItemDTOS);

	/**
	 * 功能描述: 根据skuId获取sku  没有查询到则返回一个sku 里面全部为空
	 * @Param:
	 * @Return:
	 * @Author: Yifei Wang
	 * @Date: 2020/12/6 14:46
	 */
	SkuDTO getSku(Long skuId);

	/**
	 * 根据SPU ID返回SPU的DTO
	 * @param spuId
	 * @return
	 */
	SpuDTO getSimpleSpuById(Long spuId);

	/**
	 * 根据SKU ID获取店铺
	 * @param skuId
	 * @return
	 */
	ShopDTO getShopBySKUId(Long skuId);
}
