package com.wq.mmall.rocketmq;

import com.wq.mmall.dao.RocketMQDao;
import com.wq.mmall.entity.Transaction;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.Optional;

/**
 * Author: weiqiang
 * Time: 2020/4/10 下午2:58
 */
@RocketMQTransactionListener(txProducerGroup = "test_transaction")

public class MQTransaction implements RocketMQLocalTransactionListener {

    @Autowired
    private RocketMQDao rocketMQDao;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        MessageHeaders headers = message.getHeaders();
        String transactionId = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);
        String share_id = (String) headers.get("share_id");

        System.out.println(transactionId + "_______" + share_id + "_______" + o);

        try {
            //执行事务代码
            //记录日志
          fail();
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            return RocketMQLocalTransactionState.ROLLBACK;

        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        MessageHeaders headers = message.getHeaders();
        String transactionId = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);
        Optional<Transaction> transaction = rocketMQDao.findByTransaction(transactionId);
        if (transaction.isPresent())
            return RocketMQLocalTransactionState.COMMIT;
        return RocketMQLocalTransactionState.ROLLBACK;
    }

    private void  fail() throws Exception {
        System.out.println("出错");
        throw  new Exception();
    }
}
