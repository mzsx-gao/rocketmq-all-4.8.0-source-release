package org.apache.rocketmq.example.demo1_quickstart;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
/**
 * 异步发送
 */
public class AsyncProducer {
    public static void main(String[] args) throws Exception{
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
        // 设置NameServer的地址
//        producer.setNamesrvAddr("localhost:9876");
        producer.setNamesrvAddr("localhost:9876");
        // 启动Producer实例
        producer.start();
        //retryTimesWhenSendFailed 同步模式下内部尝试发送消息的最大次数 默认值是 2
        //retryTimesWhenSendAsyncFailed 异步模式下内部尝试发送消息的最大次数 默认值是 2
        producer.setRetryTimesWhenSendAsyncFailed(0);
        //启用Broker故障延迟机制
        producer.setSendLatencyFaultEnable(true);

        for (int i = 0; i < 100; i++) {
            final int index = i;
            // 创建消息，并指定Topic，Tag和消息体
            Message msg = new Message("TopicTest", "TagA", "OrderID888",
                    "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
            // SendCallback接收异步返回结果的回调
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.printf("%-10d OK %s %n", index, sendResult.getMsgId());
                }
                @Override
                public void onException(Throwable e) {
                    System.out.printf("%-10d Exception %s %n", index, e);e.printStackTrace();
                }
            });
        }
        Thread.sleep(10000);
        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }
}
