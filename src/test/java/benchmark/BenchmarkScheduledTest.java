package benchmark;

import org.noear.folkmq.client.MqClient;
import org.noear.folkmq.client.MqClientImpl;
import org.noear.folkmq.server.MqServer;
import org.noear.folkmq.server.MqServerImpl;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class BenchmarkScheduledTest {
    public static void main(String[] args) throws Exception {
        //服务端
        MqServer server = new MqServerImpl()
                .addAccess("folkmq", "YapLHTx19RlsEE16")
                .start(9393);

        Thread.sleep(1000);
        int count = 1000_000;
        int timeout = 1000 * 100;
        CountDownLatch countDownLatch = new CountDownLatch(count);

        //客户端
        MqClient client = new MqClientImpl(
                "folkmq://127.0.0.1:9393?accessKey=folkmq&accessSecretKey=YapLHTx19RlsEE16");

        //订阅
        client.subscribe("hot", "a", message -> {
            //System.out.println("::" + topic + " - " + message);
        });

        client.subscribe("test", "a", message -> {
            //System.out.println("::" + topic + " - " + message);
            countDownLatch.countDown();
        });

        //发布预热
        for (int i = 0; i < 100; i++) {
            client.publish("hot", "hot-" + i).get();
        }

        //发布测试
        long start_time = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            client.publish("test", "test-" + i, new Date(System.currentTimeMillis() + 5000));
        }
        long sendTime = System.currentTimeMillis() - start_time;

        System.out.println("sendTime: " + sendTime);
        countDownLatch.await(timeout, TimeUnit.MILLISECONDS);

        long consumeTime = System.currentTimeMillis() - start_time;

        System.out.println("sendTime: " + sendTime);
        System.out.println("consumeTime: " + consumeTime + ", count: " + (count - countDownLatch.getCount()));
    }
}
