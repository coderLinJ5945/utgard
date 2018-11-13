package com.hc.opc.utgard.init.test;

import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.da.browser.Branch;
import org.openscada.opc.lib.da.browser.FlatBrowser;
import org.openscada.opc.lib.da.browser.Leaf;

import java.net.UnknownHostException;
import java.util.concurrent.Executors;
public class ListGroupAndItemTest {
    /*列举连接下的所有Group和 Item */
    public static void main(String[] args) throws AlreadyConnectedException, JIException, UnknownHostException {
        final ConnectionInformation ci = new ConnectionInformation();
        ci.setHost("192.168.30.131");
        ci.setDomain("");
        ci.setUser("OPCUser");
        ci.setPassword("123456");
        ci.setClsid("f8582cf2-88fb-11d0-b850-00c0f0104305");
        final Server server = new Server(ci, Executors.newSingleThreadScheduledExecutor());
        server.connect();

        dumpTree(server.getTreeBrowser().browse(),0);
        //dumpFlat(server.getFlatBrowser());

        server.disconnect();
    }

    private static void dumpFlat(final FlatBrowser browser)
            throws IllegalArgumentException, UnknownHostException, JIException {
        for (String name : browser.browse()) {
            System.out.println(name);
        }
    }

    private static void dumpTree(final Branch branch, final int level) {

        for (final Leaf leaf : branch.getLeaves()) {
            dumpLeaf(leaf, level);
        }
       /* for (final Branch subBranch : branch.getBranches()) {
           // dumpBranch(subBranch, level);
            dumpTree(subBranch, level + 1);
        }*/
    }

    private static String printTab(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("\t");
        }
        return sb.toString();
    }

    private static void dumpLeaf(final Leaf leaf, final int level) {
        System.out.println(printTab(level) + "Leaf: " + leaf.getName() + ":"
                + leaf.getItemId());
    }

   /* private static void dumpBranch(final Branch branch, final int level) {
        System.out.println(printTab(level) + "Branch: " + branch.getName());
    }*/
}
