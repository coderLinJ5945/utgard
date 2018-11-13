package com.hc.opc.utgard.emqtt;

import com.hc.opc.utgard.config.EmqttConfig;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.UUID;

/**
 * emqtt 对接工具类
 */
//@Component
public class EmqttUtil {

    private static Logger logger = LoggerFactory.getLogger(EmqttUtil.class);

    @Resource
    private  EmqttConfig emqttConfig;
    // 维护一个本类的静态变量
    public static EmqttUtil emqttUtil;
   // @PostConstruct
    private void init(){
        emqttUtil = this;
        emqttUtil.emqttConfig = this.emqttConfig;
    }
    //发送
    public static void senMessages(final MqttMessage mqttMessage)  {
        // 1.实例化mqtt客户端
        System.out.println(emqttUtil.emqttConfig.getHost());
        MqttClient client = null;
        try {
            client = new MqttClient(emqttUtil.emqttConfig.getHost(), UUID.randomUUID().toString());

        // 2.设置mqtt连接属性
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        // 3.连接
        client.connect(options);
        client.publish(emqttUtil.emqttConfig.getTopic(), mqttMessage);
        logger.info("发送EMQTT消息成功，消息内容：{}",new String(mqttMessage.getPayload()));
        System.out.println("发送EMQTT消息成功,消息内容："+new String(mqttMessage.getPayload()));
        } catch (MqttException e) {
            logger.error("发送EMQTT消息异常，异常内容：{}"+ e);
        }
    }

    /**
     * 接收 emqtt 消息方法， 用于测试
     * @param host ip
     * @param topic 主题
     * @throws MqttException
     */
    public static void receiveMessages(String host,String topic) throws MqttException {
        System.out.println( );
        MqttClient client = new MqttClient(host, UUID.randomUUID().toString());
        // 2.设置mqtt连接属性
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        // 3.连接
        client.connect(options);
        // 4.监听
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                System.out.println();
            }
            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                logger.info("》》》接收EMQTT消息成功，消息内容：",new String(mqttMessage.getPayload()));
                System.out.println("》》》接收EMQTT消息成功，消息内容："+new String(mqttMessage.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                System.out.println();
            }
        });
        while (true) {
            client.subscribe(topic, 2);
        }
    }

    public static void main(String[] args) {
        MqttMessage mqttMessage = new MqttMessage();
        String result = "ClientId认证测试";
        mqttMessage.setPayload(result.toString().getBytes());
        // 1.实例化mqtt客户端
        MqttClient client = null;
        try {
            client = new MqttClient("tcp://127.0.0.1:1883", "clientIdTest");

            // 2.设置mqtt连接属性
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            // 3.连接
            client.connect(options);
            client.publish("hellolinj", mqttMessage);
            System.out.println("发送EMQTT消息成功,消息内容："+new String(mqttMessage.getPayload()));
        } catch (MqttException e) {
            logger.error("发送EMQTT消息异常，异常内容：{}"+ e);
        }
    }
}
