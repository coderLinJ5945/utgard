package com.hc.opc.utgard.emqtt.test;

import com.hc.opc.utgard.emqtt.EmqttUtil;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class SendMessageTest {
    public static void main(String[] args) {
        MqttMessage mqttMessage = new MqttMessage();
        String result = "ClientId认证测试";
        mqttMessage.setPayload(result.toString().getBytes());
        EmqttUtil.senMessages(mqttMessage);
    }
}
