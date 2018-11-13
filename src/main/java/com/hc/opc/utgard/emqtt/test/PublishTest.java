package com.hc.opc.utgard.emqtt.test;

import com.hc.opc.utgard.emqtt.EmqttUtil;
import org.eclipse.paho.client.mqttv3.MqttException;

public class PublishTest {

    public static void main(String[] args) throws MqttException {
        EmqttUtil.receiveMessages("tcp://127.0.0.1:1883","hellolinj");

    }


    /*public static void main(String[] args) throws MqttException {
        String host = "tcp://39.104.138.161:8081";
        String topic = "hello";
        String clientId = "12345";// clientId不能重复
        // 1.设置mqtt连接属性
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        // 2.实例化mqtt客户端
        MqttClient client = new MqttClient(host, clientId);
        // 3.连接
        client.connect(options);
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload("消息内容".getBytes());
        client.publish(topic, mqttMessage);

    }*/
}
