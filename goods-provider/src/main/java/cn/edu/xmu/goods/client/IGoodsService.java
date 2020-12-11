package cn.edu.xmu.goods.client;



import cn.edu.xmu.goods.client.dubbo.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yifei Wang
 * @Date: 2020/12/6 10:49
 */
public interface IGoodsService {

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

	Long getGoodWeightBySku(Long skuId);

	Long getFreightModelIdBySku(Long skuID);

	Boolean deleteFreightModelIdBySku(Long modelId, Long shopId);

	//下单获取名字和价格 用户普通订单
	List<PriceDTO> getPriceAndName(List<OrderItemDTO> orderItemDTOS);

}
