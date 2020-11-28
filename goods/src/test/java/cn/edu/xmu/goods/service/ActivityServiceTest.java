package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.model.po.PresaleActivityPo;
import cn.edu.xmu.goods.model.vo.ActivityFinderVo;
import cn.edu.xmu.goods.model.vo.PresaleActivityVo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ActivityServiceTest {

    @Autowired
    private ActivityService activityService;


    @Test
    void getPresaleActivityStatus() {
        ReturnObject ret = activityService.getPresaleActivityStatus();
        assertEquals(0, ret.getCode());
    }

    @Test
    void getPresaleActivities() {
        ActivityFinderVo vo = new ActivityFinderVo();
        vo.setPage(1);
        vo.setPageSize(10);
        vo.setShopId(1L);
        vo.setTimeline((byte)2);
        ReturnObject ret = activityService.getPresaleActivities(vo, true);
        assertEquals(0, ret.getCode());
    }

    @Test
    void addPresaleActivity() {
        PresaleActivityVo vo =new PresaleActivityVo();
        vo.setName("测试预售活动");
        vo.setBeginTime(LocalDateTime.now().minusHours(3));
        vo.setPayTime(LocalDateTime.now().plusHours(3));
        vo.setEndTime(LocalDateTime.now().plusDays(1));
        vo.setAdvancePayPrice(120L);
        vo.setRestPayPrice(500L);
        vo.setQuantity(200L);

        ReturnObject<PresaleActivityPo> ret = activityService.addPresaleActivity(vo, 647L);
        assertEquals(ResponseCode.OK, ret.getCode());
        assertEquals(vo.getAdvancePayPrice(),ret.getData().getAdvancePayPrice());
        // assertEquals(vo.getBeginTime(),)
    }

    @Test
    void modifyPresaleActivity() {
    }

    @Test
    void delPresaleActivity() {
    }

    @Test
    void grouponActivityStatus() {
    }

    @Test
    void getGrouponActivities() {
    }

    @Test
    void addGrouponActivity() {
    }

    @Test
    void modifyGrouponActivity() {
    }

    @Test
    void delGrouponActivity() {
    }

    @Test
    void couponActivityStatus() {
    }

    @Test
    void getCouponActivities() {
    }

    @Test
    void addCouponActivity() {
    }

    @Test
    void modifyCouponActivity() {
    }

    @Test
    void delCouponActivity() {
    }

    @Test
    void getCouponList() {
    }

    @Test
    void useCoupon() {
    }

    @Test
    void delCoupon() {
    }

    @Test
    void refundCoupon() {
    }

    @Test
    void claimCoupon() {
    }
}