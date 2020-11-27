package cn.edu.xmu.goods.service;

import cn.edu.xmu.ooad.util.ReturnObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ActivityServiceTest {

    @Autowired
    private ActivityService activityService;

    @Test
    void getPresaleActivityStatus() {
        ReturnObject ret = activityService.getPresaleActivityStatus();
        assertEquals(200, ret.getCode());
    }

    @Test
    void getPresaleActivities() {
    }

    @Test
    void addPresaleActivity() {
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