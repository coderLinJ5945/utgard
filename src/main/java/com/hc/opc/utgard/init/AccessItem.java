package com.hc.opc.utgard.init;

import com.hc.opc.utgard.emqtt.EmqttUtil;
import net.sf.json.JSONObject;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.openscada.opc.lib.da.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实现成线程的 Item定时任务，这里必须使用多线程，防止多个item之前监听出现连锁问题
 */
public class AccessItem implements DataCallback{

    private Logger logger = LoggerFactory.getLogger(AccessItem.class);

    @Override
    public void changed(Item item, ItemState itemState) {
        JSONObject result = new JSONObject();
        result.put("itemId",item.getId());
        result.put("value",itemState.getValue().toString());
        result.put("timestamp",itemState.getTimestamp());
        result.put("quality",itemState.getQuality());
        System.out.println("发送消息内容："+result.toString());
        logger.info("发送消息内容："+result.toString());
       /* MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(result.toString().getBytes());
        EmqttUtil.senMessages(mqttMessage);*/

    }

}
