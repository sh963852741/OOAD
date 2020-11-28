package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.model.po.PresaleActivityPo;
import cn.edu.xmu.goods.model.vo.ActivityFinderVo;
import cn.edu.xmu.goods.model.vo.PresaleActivityVo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ActivityServiceTest {

    @Autowired
    private ActivityService activityService;

    static private HashMap<String, Long> activityIdHash = new HashMap<>(); // 静态变量处于常量区，数据可以保存


    //region 预售
    @Test
    void getPresaleActivityStatus() {
        ReturnObject ret = activityService.getPresaleActivityStatus();
        assertEquals(ResponseCode.OK, ret.getCode());
    }

    @Test
    @Order(1)
    void addPresaleActivity() {
        PresaleActivityVo vo =new PresaleActivityVo();
        vo.setName("测试预售活动");
        vo.setBeginTime(LocalDateTime.now().minusHours(3));
        vo.setPayTime(LocalDateTime.now().plusHours(3));
        vo.setEndTime(LocalDateTime.now().plusDays(1));
        vo.setAdvancePayPrice(120L);
        vo.setRestPayPrice(500L);
        vo.setQuantity(200);

        ReturnObject<PresaleActivityVo> ret = activityService.addPresaleActivity(vo, 647L, 1L);
        assertEquals(ResponseCode.OK, ret.getCode());
        assertEquals(vo.getAdvancePayPrice(),ret.getData().getAdvancePayPrice());
        assertEquals(vo.getBeginTime(),ret.getData().getBeginTime());
        assertNotNull(ret.getData().getId());
        activityIdHash.put("runningPActivity", ret.getData().getId());
    }

    @Test
    void getPresaleActivities() {
        ActivityFinderVo vo = new ActivityFinderVo();
        vo.setPage(1);
        vo.setPageSize(10);
        vo.setShopId(1L);
        vo.setTimeline((byte)2);
        ReturnObject ret = activityService.getPresaleActivities(vo, true);
        assertEquals(ResponseCode.OK, ret.getCode());
    }

    @Test
    @Order(2)
    void getPresaleActivitiesBySPU() {
        ActivityFinderVo vo = new ActivityFinderVo();
        vo.setPage(1);
        vo.setPageSize(10);
        vo.setShopId(1L);
        vo.setTimeline((byte)2);
        vo.setSpuId(647L);
        ReturnObject<List<PresaleActivityVo>> ret = activityService.getPresaleActivities(vo, true);
        assertEquals(ResponseCode.OK, ret.getCode());
        assertTrue(ret.getData().size()>0);
    }

    @Test
    @Order(2)
    void modifyPresaleActivity() {
        assertTrue(activityIdHash.containsKey("runningPActivity"));

        PresaleActivityVo vo =new PresaleActivityVo();
        vo.setName("测试预售活动-2");
        vo.setBeginTime(LocalDateTime.now().minusHours(4));
        vo.setPayTime(LocalDateTime.now().plusHours(3));
        vo.setEndTime(LocalDateTime.now().plusDays(1));
        vo.setAdvancePayPrice(240L);
        vo.setRestPayPrice(500L);
        vo.setQuantity(30);

        ReturnObject<PresaleActivityVo> ret = activityService.modifyPresaleActivity(activityIdHash.get("runningPActivity"), vo,1L);
        assertEquals(ResponseCode.OK, ret.getCode());
        assertEquals(vo.getAdvancePayPrice(),ret.getData().getAdvancePayPrice());
        assertEquals(vo.getBeginTime(),ret.getData().getBeginTime());
        assertNotNull(ret.getData().getId());
    }

    @Test
    @Order(3)
    void delPresaleActivity() {
        assertTrue(activityIdHash.containsKey("runningPActivity"));

        ReturnObject ret = activityService.delPresaleActivity(activityIdHash.get("runningPActivity"));
        assertEquals(ResponseCode.OK, ret.getCode());
        activityIdHash.remove("runningPActivity");
    }
    //endregion

    @Test
    void grouponActivityStatus() {
        ReturnObject ret = activityService.getPresaleActivityStatus();
        assertEquals(ResponseCode.OK, ret.getCode());
    }

    @Test
    @Order(2)
    void getGrouponActivities() {
    }

    @Test
    @Order(1)
    void addGrouponActivity() {
    }

    @Test
    @Order(2)
    void modifyGrouponActivity() {
    }

    @Test
    @Order(3)
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