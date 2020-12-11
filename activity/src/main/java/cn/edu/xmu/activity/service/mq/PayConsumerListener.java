package cn.edu.xmu.activity.service.mq;

import cn.edu.xmu.activity.mapper.CouponPoMapper;
import cn.edu.xmu.activity.model.po.CouponPo;
import cn.edu.xmu.ooad.util.JacksonUtil;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 消息消费者
 * @author Ming Qiu
 * @date Created in 2020/11/7 22:47
 **/
@Service
@RocketMQMessageListener(topic = "coupon-topic", consumeMode = ConsumeMode.CONCURRENTLY, consumeThreadMax = 10, consumerGroup = "activity-group")
public class PayConsumerListener implements RocketMQListener<String> {
    private static final Logger logger = LoggerFactory.getLogger(PayConsumerListener.class);
    @Autowired
    CouponPoMapper couponPoMapper;
    @Override
    public void onMessage(String message) {
        CouponPo po = JacksonUtil.toObj(message, CouponPo.class);
        logger.info("onMessage: got message coupon =" + po +" time = "+ LocalDateTime.now());
        couponPoMapper.insert(po);
        return;
    }
}
