package cn.edu.xmu.activity.service;

import cn.edu.xmu.activity.model.bo.CouponActivity;
import cn.edu.xmu.activity.model.bo.GrouponActivity;
import cn.edu.xmu.activity.model.bo.PresaleActivity;
import cn.edu.xmu.activity.model.vo.ActivityFinderVo;
import cn.edu.xmu.activity.model.vo.CouponActivityVo;
import cn.edu.xmu.activity.model.vo.GrouponActivityVo;
import cn.edu.xmu.activity.model.vo.PresaleActivityVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashMap;

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
        Assertions.assertEquals(vo.getAdvancePayPrice(),ret.getData().getAdvancePayPrice());
        Assertions.assertEquals(vo.getBeginTime(),ret.getData().getBeginTime());
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
        ReturnObject ret = activityService.getPresaleActivities(vo);
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
        ReturnObject<PageInfo<VoObject>> ret = activityService.getPresaleActivities(vo);
        assertEquals(ResponseCode.OK, ret.getCode());
        assertTrue(ret.getData().getList().size()>0);
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
        Assertions.assertEquals(vo.getAdvancePayPrice(),ret.getData().getAdvancePayPrice());
        Assertions.assertEquals(vo.getBeginTime(),ret.getData().getBeginTime());
        assertNotNull(ret.getData().getId());
    }

    @Test
    @Order(3)
    void delPresaleActivity() {
        assertTrue(activityIdHash.containsKey("runningPActivity"));

        ReturnObject ret = activityService.modifyPresaleActivity(activityIdHash.get("runningPActivity"),1L, PresaleActivity.PresaleStatus.DELETE.getCode());
        assertEquals(ResponseCode.OK, ret.getCode());
        activityIdHash.remove("runningPActivity");
    }
    //endregion

    //region 团购
    @Test
    void grouponActivityStatus() {
        ReturnObject ret = activityService.getPresaleActivityStatus();
        assertEquals(ResponseCode.OK, ret.getCode());
    }

    @Test
    @Order(2)
    void getGrouponActivities() {
        ActivityFinderVo vo = new ActivityFinderVo();
        vo.setPage(1);
        vo.setPageSize(10);
        vo.setShopId(1L);
        vo.setTimeline((byte)2);
        ReturnObject ret = activityService.getGrouponActivities(vo, true);
        assertEquals(ResponseCode.OK, ret.getCode());
    }

    @Test
    @Order(2)
    void getGrouponActivitiesBySPU() {
        ActivityFinderVo vo = new ActivityFinderVo();
        vo.setPage(1);
        vo.setPageSize(10);
        vo.setShopId(1L);
        vo.setTimeline((byte)2);
        vo.setSpuId(647L);
        ReturnObject<PageInfo<VoObject>> ret = activityService.getGrouponActivities(vo, true);
        assertEquals(ResponseCode.OK, ret.getCode());
        assertTrue(ret.getData().getList().size() > 0);
    }

    @Test
    @Order(1)
    void addGrouponActivity() {
        GrouponActivityVo vo =new GrouponActivityVo();
        vo.setName("测试团购活动");
        vo.setBeginTime(LocalDateTime.now().minusHours(3));
        vo.setStrategy("{}");
        vo.setEndTime(LocalDateTime.now().plusDays(1));

        ReturnObject<GrouponActivityVo> ret = activityService.addGrouponActivity(vo, 647L, 1L);
        assertEquals(ResponseCode.OK, ret.getCode());
        Assertions.assertEquals(vo.getStrategy(),ret.getData().getStrategy());
        Assertions.assertEquals(vo.getBeginTime(),ret.getData().getBeginTime());
        assertNotNull(ret.getData().getId());
        activityIdHash.put("runningGActivity", ret.getData().getId());
    }

    @Test
    @Order(2)
    void modifyGrouponActivity() {
        assertTrue(activityIdHash.containsKey("runningGActivity"));

        GrouponActivityVo vo =new GrouponActivityVo();
        vo.setName("测试团购活动-改");
        vo.setBeginTime(LocalDateTime.now().minusHours(5));
        vo.setStrategy("{\"data\": \"123\"}");
        vo.setEndTime(LocalDateTime.now().plusDays(1));

        ReturnObject<GrouponActivityVo> ret = activityService.modifyGrouponActivity(activityIdHash.get("runningGActivity"), vo, 1L);
        assertEquals(ResponseCode.OK, ret.getCode());
        Assertions.assertEquals(vo.getStrategy(),ret.getData().getStrategy());
        Assertions.assertEquals(vo.getBeginTime(),ret.getData().getBeginTime());
        // assertNotNull(ret.getData().getId());
    }

    @Test
    @Order(3)
    void delGrouponActivity() {
        assertTrue(activityIdHash.containsKey("runningGActivity"));

        ReturnObject<?> ret = activityService.modifyGrouponActivity(activityIdHash.get("runningGActivity"),1, GrouponActivity.GrouponStatus.DELETE.getCode());
        assertEquals(ResponseCode.OK, ret.getCode());
        activityIdHash.remove("runningGActivity");
    }
    //endregion

    //region 优惠活动
    @Test
    void couponActivityStatus() {
        ReturnObject<CouponActivity.CouponStatus[]> ret = activityService.getCouponActivityStatus();
        assertEquals(ResponseCode.OK, ret.getCode());
    }

    @Test
    @Order(2)
    void getCouponActivities() {
        ActivityFinderVo vo = new ActivityFinderVo();
        vo.setPage(1);
        vo.setPageSize(10);
        vo.setShopId(1L);
        vo.setTimeline((byte)2);
        ReturnObject<PageInfo<VoObject>> ret = activityService.getCouponActivities(vo);
        assertEquals(ResponseCode.OK, ret.getCode());
        assertTrue(ret.getData().getList().size() > 0);
    }

    @Test
    @Order(1)
    void addCouponActivity() {
        CouponActivityVo vo =new CouponActivityVo();
        vo.setName("测试优惠活动");
        vo.setBeginTime(LocalDateTime.now().minusHours(3));
        vo.setStrategy("{}");
        vo.setEndTime(LocalDateTime.now().plusDays(1));
        vo.setQuantity(30);
        vo.setQuantityType((byte)1);

        ReturnObject<CouponActivityVo> ret = activityService.addCouponActivity(vo, 1L,1L );
        assertEquals(ResponseCode.OK, ret.getCode());
        Assertions.assertEquals(vo.getStrategy(),ret.getData().getStrategy());
        Assertions.assertEquals(vo.getQuantity(),ret.getData().getQuantity());
        Assertions.assertEquals(vo.getBeginTime(),ret.getData().getBeginTime());
        Assertions.assertEquals(vo.getQuantityType(),ret.getData().getQuantityType());
        assertNotNull(ret.getData().getId());
        activityIdHash.put("runningCActivity", ret.getData().getId());
    }

    @Test
    @Order(2)
    void modifyCouponActivity() {
        assertTrue(activityIdHash.containsKey("runningCActivity"));

        CouponActivityVo vo =new CouponActivityVo();
        vo.setName("测试优惠活动-改");
        vo.setBeginTime(LocalDateTime.now().minusHours(3));
        vo.setStrategy("{}");
        vo.setEndTime(LocalDateTime.now().plusDays(1));
        vo.setQuantity(50);
        vo.setQuantityType((byte)0);

        ReturnObject<CouponActivityVo> ret = activityService.modifyCouponActivity(activityIdHash.get("runningCActivity"), vo, 647L);
        assertEquals(ResponseCode.OK, ret.getCode());
        Assertions.assertEquals(vo.getStrategy(),ret.getData().getStrategy());
        Assertions.assertEquals(vo.getQuantity(),ret.getData().getQuantity());
        Assertions.assertEquals(vo.getBeginTime(),ret.getData().getBeginTime());
        Assertions.assertEquals(vo.getQuantityType(),ret.getData().getQuantityType());
        // assertNotNull(ret.getData().getId());
    }

    @Test
    @Order(3)
    void delCouponActivity() {
        assertTrue(activityIdHash.containsKey("runningCActivity"));

        ReturnObject<?> ret = activityService.delCouponActivity(activityIdHash.get("runningCActivity"));
        assertEquals(ResponseCode.OK, ret.getCode());
        activityIdHash.remove("runningCActivity");
    }
    //endregion 优惠活动
}