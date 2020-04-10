package com.wq.mmall.rocketmq;

import com.wq.mmall.message.Arg;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * Author: weiqiang
 * Time: 2020/4/10 下午2:35
 */
@Service
@RocketMQMessageListener(consumerGroup = "test-group", topic = "topic")
public class MQ implements RocketMQListener<Arg> {

    @Override
    public void onMessage(Arg arg) {
        System.out.println(arg.getMes()+"___________________");
    }
}
