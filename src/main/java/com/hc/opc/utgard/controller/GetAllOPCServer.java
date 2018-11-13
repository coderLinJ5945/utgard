package com.hc.opc.utgard.controller;

import org.jinterop.dcom.common.JIException;
import org.openscada.opc.dcom.list.ClassDetails;
import org.openscada.opc.lib.list.Categories;
import org.openscada.opc.lib.list.Category;
import org.openscada.opc.lib.list.ServerList;

import java.net.UnknownHostException;
import java.util.Collection;

public class GetAllOPCServer {

    /**
     * 该函数用于显示当前连接服务器上所有支持OPC DA2.0规范的OPC服务器应用
     * @param serverList
     * @throws JIException
     * @throws IllegalArgumentException
     * @throws UnknownHostException
     */
    public static Collection<ClassDetails> showAllOPCServer(ServerList serverList)
            throws JIException, IllegalArgumentException, UnknownHostException {

        final Collection<ClassDetails> detailsList = serverList.listServersWithDetails (
                new Category[] { Categories.OPCDAServer20 }, new Category[] {} );

        for ( final ClassDetails details : detailsList )
        {
            System.out.println ( String.format ( "ClsId: %s", details.getClsId () ) );
            System.out.println ( String.format ( "\tProgID: %s", details.getProgId () ) );
            System.out.println ( String.format ( "\tDescription: %s", details.getDescription () ) );
        }
        return  detailsList;
    }
}
