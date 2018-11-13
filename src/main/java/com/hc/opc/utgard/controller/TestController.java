package com.hc.opc.utgard.controller;

import com.hc.opc.utgard.config.OpcConfig;
import org.jinterop.dcom.common.JIException;
import org.openscada.opc.dcom.list.ClassDetails;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.list.ServerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.concurrent.Executors;

/**
 * 这是一个测试类
 * 1、用户获取显示当前连接服务器上所有支持OPC DA2.0规范的OPC服务器应用
 */
@Controller
@RequestMapping(value = "/test")
public class TestController {

    private Logger logger = LoggerFactory.getLogger(TestController.class);

    @Resource
    private OpcConfig opcConfig;

    @RequestMapping(value = "/helloWorld")
    @ResponseBody
    public String test() {
        return "helloWorld";
    }


    /**
     * 获取所有的opc 服务信息
     * @return
     */
    @RequestMapping(value = "/getOPCServer")
    @ResponseBody
    public Collection<ClassDetails> getOPCServer(){
        Collection<ClassDetails> classDetails = null;
        try {
            ServerList serverList = new ServerList(opcConfig.getHost(), opcConfig.getUser(), opcConfig.getPassword(), opcConfig.getDomain());
            classDetails = GetAllOPCServer.showAllOPCServer(serverList);

        } catch (JIException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if(classDetails == null ){
            logger.error("serverList 获取失败<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        }
        return classDetails;
    }


    /**
     * 获取配置文件中的服务的所有item
     *
     */
    @RequestMapping(value = "/getItems")
    @ResponseBody
    public Collection<String> getItems(){
        ConnectionInformation ci = new ConnectionInformation();
        ci.setHost(opcConfig.getHost());
        ci.setDomain(opcConfig.getDomain());
        ci.setUser(opcConfig.getUser());
        ci.setPassword(opcConfig.getPassword());
        ci.setClsid(opcConfig.getClsid());
        Server server = new Server(ci, Executors.newSingleThreadScheduledExecutor());
        try {
            server.connect();
            try {
                Collection<String> stringCollection = server.getFlatBrowser().browse();
                return  stringCollection;
            } catch (IllegalArgumentException e) {
                logger.error("获取items异常《《《《《《《《《《《《");
            } catch (UnknownHostException e) {
                logger.error("获取items异常《《《《《《《《《《《《");
            } catch (JIException e) {
                logger.error("获取items异常《《《《《《《《《《《《");
            }
            server.disconnect();
        } catch (UnknownHostException e) {
            logger.error("获取配置文件时连接异常《《《《《《《《《《《《");
        } catch (JIException e) {
            logger.error("获取配置文件时连接异常《《《《《《《《《《《《");
        } catch (AlreadyConnectedException e) {
            logger.error("获取配置文件时连接异常《《《《《《《《《《《《");
        }

        return null;
    }



}
