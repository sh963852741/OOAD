package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.ShopDao;
import cn.edu.xmu.goods.model.bo.Comment;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.bo.Spu;
import cn.edu.xmu.goods.model.po.ShopPo;
import cn.edu.xmu.goods.model.vo.ShopRetVo;
import cn.edu.xmu.goods.model.vo.ShopVo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.goods.model.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class ShopService {
    private  static  final Logger logger = LoggerFactory.getLogger(ShopService.class);
    @Autowired
    private ShopDao shopDao;

    /**
     * 功能描述: 根据shopid获取shop详细信息
     * @Param: [ShopId]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Lei Yang
     * @Date: 2020/11/29 23:21
     */
    public ReturnObject<Shop> getShopByShopId(Long ShopId){
        return shopDao.getShopById(ShopId);
    }

    /**
     * 功能描述: 商家申请店铺
     * @Param: [po]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Lei Yang
     * @Date: 2020/11/29 22:10
     */
    public ReturnObject  newShop(ShopVo shopVo) {
        ShopPo po=new ShopPo();
        po.setName(shopVo.getName());

        ReturnObject ret=shopDao.newShop(po);
        if(ret.getCode()!= ResponseCode.OK){
            return ret;
        }
        Shop shop=new Shop((ShopPo) ret.getData());
        ShopRetVo vo=shop.createVo();
        return new ReturnObject(vo);
    }

    /**
     * 功能描述: 获取店铺所有状态
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Lei Yang
     * @Date: 2020/12/1 20:36
     */
    public ReturnObject getShopStates(){
        return shopDao.getShopState();
    }

    /**
     * 功能描述: 修改商铺信息
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Lei Yang
     * @Date: 2020/12/1 20:36
     */
    public ReturnObject updateShop(Long id,Long shopId, ShopVo shopVo) {
        Shop shop=new Shop();
        shop.setId(id.longValue());
        shop.setName(shopVo.getName());
        ReturnObject ret=shopDao.UpdateShop(shop);
        return ret;
    }

    /**
     * 功能描述: 关闭商铺
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Lei YangLong
     * @Date: 2020/12/1 20:36
     */
    public ReturnObject deleteShopById(Long id) {
        ReturnObject ret=shopDao.deleteShopbyID(id.longValue());
        return ret;
    }

    /**
     * 功能描述: 管理员审核商铺
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Lei YangLong
     * @Date: 2020/12/1 20:36
     */
    public ReturnObject passShop(Long id,ShopConclusionVo conclusion) {
        Shop shop=new Shop();
        shop.setId(id.longValue());
        shop.setState(conclusion.getConclusion()==true? Shop.State.NORM.getCode().byteValue() : Shop.State.EXAME.getCode().byteValue());
        ReturnObject ret=shopDao.updateShopState(shop);
        return ret;
    }

    /**
     * 功能描述: 管理员上线商铺
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Lei YangLong
     * @Date: 2020/12/1 20:36
     */
    public ReturnObject onShelfShop(Long id) {
        Shop shop=new Shop();
        shop.setId(id.longValue());
        shop.setState(Shop.State.ONLINE.getCode().byteValue());
        ReturnObject ret=shopDao.UpdateShop(shop);
        return ret;
    }

    /**
     * 功能描述: 管理员下线商铺
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Lei YangLong
     * @Date: 2020/12/1 20:36
     */
    public ReturnObject offShelfShop(Long id) {
        Shop shop=new Shop();
        shop.setId(id.longValue());
        shop.setState(Shop.State.OFFLINE.getCode().byteValue());
        ReturnObject ret=shopDao.UpdateShop(shop);
        return ret;
    }
}