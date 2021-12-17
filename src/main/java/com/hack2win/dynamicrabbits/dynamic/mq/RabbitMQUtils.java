package com.hack2win.dynamicrabbits.dynamic.mq;

import com.hack2win.dynamicrabbits.dynamic.mq.util.ResourceUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQUtils {

    public static Channel getChannel() throws Exception {
        // 处理连接信息
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(ResourceUtil.getKey("rabbitmq.uri"));
        //创建一个新的连接 即TCP连接
        Connection connection = factory.newConnection();
        //创建一个通道
        final Channel channel = connection.createChannel();
        return channel;
    }
}
