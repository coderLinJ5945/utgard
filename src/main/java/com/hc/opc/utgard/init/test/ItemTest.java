package com.hc.opc.utgard.init.test;

import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.*;

import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.Executors;

public class ItemTest {
    /*Item的同步查询*/
    public static void main(String[] args) throws AlreadyConnectedException, JIException, UnknownHostException, NotConnectedException, DuplicateGroupException, AddFailedException {
        ConnectionInformation ci = new ConnectionInformation();
        ci.setHost("192.168.30.131");
        ci.setDomain("");
        ci.setUser("OPCUser");
        ci.setPassword("123456");
        ci.setClsid("f8582cf2-88fb-11d0-b850-00c0f0104305");

        Server server = new Server(ci,
                Executors.newSingleThreadScheduledExecutor());
        server.connect();

        Group group = server.addGroup();
        Item item = group.addItem("Random.Real5");

        Map<String, Item> items = group.addItems("Random.Real1",
                "Random.Real2", "Random.Real3", "Random.Real4");

        dumpItem(item);

        for (Map.Entry<String, Item> temp : items.entrySet()) {
            dumpItem(temp.getValue());
        }

        server.dispose();
    }

    private static void dumpItem(Item item) throws JIException {
        System.out.println("[" + (++count) + "],ItemName:[" + item.getId()
                + "],value:" + item.read(false).getValue());
    }
    private static int count;
}
