package org.noear.folkmq.client;

import org.noear.socketd.transport.client.ClientConfigHandler;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * 消息客户端
 *
 * @author noear
 * @since 1.0
 */
public interface MqClient extends Closeable {
    /**
     * 连接
     */
    MqClient connect() throws IOException;

    /**
     * 断开连接
     */
    void disconnect() throws IOException;

    /**
     * 客户端配置
     */
    MqClient config(ClientConfigHandler configHandler);

    /**
     * 自动回执
     *
     * @param auto 自动（默认为 true）
     */
    MqClient autoAcknowledge(boolean auto);

    /**
     * 发布重试
     *
     * @param times 次数（默认为 0）
     */
    MqClient publishRetryTimes(int times);

    /**
     * 订阅主题
     *
     * @param topic           主题
     * @param consumer        消费者（实例 ip 或 集群 name）
     * @param consumerHandler 消费处理
     */
    void subscribe(String topic, String consumer, MqConsumeHandler consumerHandler) throws IOException;

    /**
     * 取消订阅主题
     *
     * @param topic    主题
     * @param consumer 消费者（实例 ip 或 集群 name）
     */
    void unsubscribe(String topic, String consumer) throws IOException;

    /**
     * 同步发布消息
     *
     * @param topic   主题
     * @param message 消息
     */
    void publish(String topic, IMqMessage message) throws IOException;

    /**
     * 异步发布消息
     *
     * @param topic   主题
     * @param message 消息
     */
    CompletableFuture<Boolean> publishAsync(String topic, IMqMessage message) throws IOException;
}
