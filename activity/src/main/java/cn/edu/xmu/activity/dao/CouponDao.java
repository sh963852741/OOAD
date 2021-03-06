package cn.edu.xmu.activity.dao;

import cn.edu.xmu.activity.mapper.CouponPoMapper;
import cn.edu.xmu.activity.model.bo.Coupon;
import cn.edu.xmu.activity.model.po.CouponPo;
import cn.edu.xmu.activity.model.po.CouponPoExample;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.JacksonUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public class CouponDao {
    @Autowired
    CouponPoMapper couponPoMapper;
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public int addCoupon(CouponPo couponPo, long activityId, long customerId){
        couponPo.setActivityId(activityId);
        couponPo.setCustomerId(customerId);
        couponPo.setState(Coupon.CouponStatus.NORMAL.getCode());
        couponPo.setCouponSn(Common.genSeqNum());
        couponPo.setGmtCreate(LocalDateTime.now());

        String json = JacksonUtil.toJson(couponPo);
        Message message = MessageBuilder.withPayload(json).build();
        rocketMQTemplate.sendOneWay("coupon-topic", message);
        return couponPoMapper.insert(couponPo);
    }

//    public int addCoupon(CouponPo couponPo, long activityId, long customerId){
//        couponPo.setActivityId(activityId);
//        couponPo.setCustomerId(customerId);
//        couponPo.setState(Coupon.CouponStatus.NORMAL.getCode());
//        couponPo.setCouponSn(Common.genSeqNum());
//        couponPo.setGmtCreate(LocalDateTime.now());
//        return couponPoMapper.insert(couponPo);
//    }

    public int cancelCoupon(long activityId){
        CouponPoExample example = new CouponPoExample();
        CouponPoExample.Criteria criteria = example.createCriteria();
        criteria.andActivityIdEqualTo(activityId);
        criteria.andStateEqualTo(Coupon.CouponStatus.NORMAL.getCode());

        CouponPo couponPo =new CouponPo();
        couponPo.setState(Coupon.CouponStatus.EXPIRED.getCode());
        couponPo.setGmtModified(LocalDateTime.now());
        // 按理说不能把已使用的优惠券设置为已作废，记得改
        return couponPoMapper.updateByExampleSelective(couponPo, example);
    }

    public int modifyCoupon(CouponPo couponPo){
        couponPo.setGmtModified(LocalDateTime.now());
        return couponPoMapper.updateByPrimaryKeySelective(couponPo);
    }

    public CouponPo getCoupon(Long id) {
        return couponPoMapper.selectByPrimaryKey(id);
    }

    public PageInfo<CouponPo> getCouponList(Long userId, Byte state, int page, int pageSize){
        PageHelper.startPage(page, pageSize);

        CouponPoExample example = new CouponPoExample();
        CouponPoExample.Criteria criteria =  example.createCriteria();
        if(state != null){
            criteria.andStateEqualTo(state);
        }
        criteria.andCustomerIdEqualTo(userId);

        List<CouponPo> couponPoList = couponPoMapper.selectByExample(example);

        return PageInfo.of(couponPoList);
    }

    public int delCoupon(long id){
        return couponPoMapper.deleteByPrimaryKey(id);
    }
}
