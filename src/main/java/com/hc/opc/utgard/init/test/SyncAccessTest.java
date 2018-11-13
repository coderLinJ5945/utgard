package com.hc.opc.utgard.init.test;


import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.*;

import java.net.UnknownHostException;
import java.util.concurrent.Executors;

/*自动重连Item异步读取*/
public class SyncAccessTest {

    private static final int PERIOD = 1000;

    private static final int SLEEP = 2000000;
    /*自动重连Item异步读取*/
    public static void main(String[] args) throws DuplicateGroupException, NotConnectedException, JIException, UnknownHostException, AddFailedException, InterruptedException {
        ConnectionInformation ci = new ConnectionInformation();
        ci.setHost("192.168.30.131");
        ci.setDomain("");
        ci.setUser("OPCUser");
        ci.setPassword("123456");
        ci.setClsid("f8582cf2-88fb-11d0-b850-00c0f0104305");

        Server server = new Server(ci,
                Executors.newSingleThreadScheduledExecutor());

        AutoReconnectController controller = new AutoReconnectController(server);

        controller.connect();

        AccessBase access = new SyncAccess(server, PERIOD);
        AccessBase access2 = new SyncAccess(server, PERIOD);
        access.addItem(".u", new DataCallback() {
            private int i;

            public void changed(Item item, ItemState itemstate) {
                System.out.println("[" + (++i) + "],ItemName:[" + item.getId()
                        + "],value:" + itemstate.getValue());
            }
        });
        access2.addItem(".u", new DataCallback() {
            private int i;

            public void changed(Item item, ItemState itemstate) {
                System.out.println("22222[" + (++i) + "],ItemName:[" + item.getId()
                        + "],value:" + itemstate.getValue());
            }
        });

        access.bind();
        Thread.sleep(SLEEP);
        access.unbind();
        controller.disconnect();
    }

}
