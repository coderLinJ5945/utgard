package com.hc.opc.utgard;

import org.jinterop.dcom.common.JIException;
import org.openscada.opc.dcom.da.OPCSERVERSTATUS;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.da.Server;

import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Tes {

    private static AtomicBoolean opcIsConnecting = new AtomicBoolean(false);

    public static void main(String[] args) throws InterruptedException {
        reClient("开始测试");
        System.out.println("123");
    }
    private static boolean test = false;
    public static  String  reClient(String result) throws InterruptedException {
        if(!opcIsConnecting.get()){
            System.out.println("执行连接尝试");
            if(test){//如果连接成功则返回
                opcIsConnecting.set(true);
                result = "Success";
                return result;
            }else{
                opcIsConnecting.set(false);
                Thread.currentThread().sleep(5000);
                reClient(result);
            }

        }
        return result;
    }
}
