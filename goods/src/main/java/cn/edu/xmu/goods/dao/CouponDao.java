package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.CouponPoMapper;
import cn.edu.xmu.goods.model.po.CouponPo;
import cn.edu.xmu.goods.model.po.CouponPoExample;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class CouponDao {
    @Autowired
    CouponPoMapper couponPoMapper;

    public int addCoupon(CouponPo couponPo){
        return couponPoMapper.insert(couponPo);
    }

    public int modifyCoupon(CouponPo couponPo){
        return couponPoMapper.updateByPrimaryKeySelective(couponPo);
    }

    public CouponPo getCoupon(Long id) {
        return couponPoMapper.selectByPrimaryKey(id);
    }

    public List<CouponPo> getCouponList(Long userId, Byte state, int page, int pageSize){
        PageHelper.startPage(page, pageSize);

        CouponPoExample example = new CouponPoExample();
        CouponPoExample.Criteria criteria =  example.createCriteria();
        criteria.andStateEqualTo(state);
        criteria.andCustomerIdEqualTo(userId);

        return couponPoMapper.selectByExample(example);
    }

    public int delCoupon(long id){
        return couponPoMapper.deleteByPrimaryKey(id);
    }
}
