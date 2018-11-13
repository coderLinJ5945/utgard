package com.hc.opc.utgard.init;

import com.hc.opc.utgard.config.OpcConfig;
import org.jinterop.dcom.common.JIException;
import org.openscada.opc.dcom.da.OPCSERVERSTATUS;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Utgard 对接 opc 读取数据（同步）
 *
 */
@Component
public class UtgardTutorial implements Runnable{

    private static Logger logger = LoggerFactory.getLogger(UtgardTutorial.class);

    private static UtgardTutorial tutorial;

    //程序连接开关，默认是关闭
    private static AtomicBoolean opcIsConnecting  = new AtomicBoolean(false);

    @Resource
    private  OpcConfig opcConfig;

    @PostConstruct
    public void intBean(){
        tutorial = this;
        tutorial.opcConfig = this.opcConfig;
    }
    /**
    *   暂时设计成程序启动时开启，需要考虑设置开关的方式
    */
    @PostConstruct
    public  void init(){
        logger.info("程序初始化<<<<<<<<<<<<<<<<<<");
        UtgardTutorial utgardTutorial = new UtgardTutorial();
        new Thread(utgardTutorial).start();
    }
    //重写run方法,启动时分出线程执行监听opc操作
    public void run(){
        Server server = clientOpc(tutorial.opcConfig);
        //这里由于新建线程，导致注入对象为null ，所以将OpcConfig挂载在tutorial class下
        System.out.println("连接到发送《《《《《《《《<<<<<<<<<<<<<<<<<");
        getOpcItem(server,tutorial.opcConfig.getPeriod(),tutorial.opcConfig.getItemIdList());
    }
    public static Server clientOpc(OpcConfig opcConfig){
        final ConnectionInformation ci = new ConnectionInformation();
        ci.setHost(opcConfig.getHost());
        ci.setDomain(opcConfig.getDomain());
        ci.setUser(opcConfig.getUser());
        ci.setPassword(opcConfig.getPassword());
        ci.setClsid(opcConfig.getClsid());
        Server server = new Server(ci, Executors.newSingleThreadScheduledExecutor());
        //设置重连逻辑
        server =  reClient(server);
        return server;
        /*if(!opcIsConnecting.get()){
            try {
                server.connect();
            } catch (UnknownHostException e) {
                logger.error("opc连接异常，异常原因:UnknownHostException 未知host");
            } catch (JIException e) {
                logger.error("opc连接异常，异常原因:JIException 访问被拒绝");
            } catch (AlreadyConnectedException e) {
                logger.error("opc连接异常，异常原因:AlreadyConnectedException 已连接");
            }
            OPCSERVERSTATUS opcserverstatus = server.getServerState();
            //初略的判断连接状态，后面有可能需要细致的分析OPCSERVERSTATUS 状态
            if(opcserverstatus == null){
                clientOpc(opcConfig);
            }
            return server;
        }*/
        // TODO: 2018/9/7  需要打印server之后的连接信息 来判断连接是否成功 ，暂时默认都为连接成功
        //return server;
    }

    /**
     *  获取设备返回的数据
     * @param server 连接返回的
     * @param period 轮询间隔
     */
    public void getOpcItem(Server server, int period, List<String> itemIdList){
        try {
            final AccessBase access = new SyncAccess(server, period);
            if(!itemIdList.isEmpty()){
                for (String itemId : itemIdList) {
                    //这里需要新建线程监听，根据itemId来新建线程，否则会出现一个item出问题导致所有的都无法完成正常监听 工作
                    logger.info("添加监听item："+itemId);
                    access.addItem(itemId,new AccessItem());
                }
                do{
                    access.bind();
                }while (true);
            }
        } catch (UnknownHostException e) {
            logger.error("发送消息异常，异常原因:未知host",e);
        } catch (NotConnectedException e) {
            logger.error("发送消息异常，异常原因:没有连接",e);
        } catch (JIException e) {
            logger.error("发送消息异常，异常原因:没有权限",e);
        } catch (DuplicateGroupException e) {
            logger.error("发送消息异常，异常原因:{}",e);
        } catch (AddFailedException e) {
            logger.error("发送消息异常，异常原因:添加失败",e);
        }
    }

    /**
     * 当连接没连上，60秒尝试一下重新连接
     * @param server
     * @return
     */
    public static Server reClient(Server server) {
        if(!opcIsConnecting.get()){
            try {
                server.connect();
            } catch (UnknownHostException e) {
                logger.error("opc连接异常，异常原因:UnknownHostException 未知host");
            } catch (JIException e) {
                logger.error("opc连接异常，异常原因:JIException 访问被拒绝");
            } catch (AlreadyConnectedException e) {
                logger.error("opc连接异常，异常原因:AlreadyConnectedException 已连接");
            }
            OPCSERVERSTATUS opcserverstatus = server.getServerState();
            //连接上之后，修改 opcIsConnecting状态
            if(opcserverstatus != null){
                logger.info("opc连接成功！>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                opcIsConnecting.set(true);
                return  server;
            }else{
                //没有连接上重新连接
                logger.error("opc服务连接失败，1分钟后尝试重新连接<<<<<<<<<<<<<<<<<<<<<");
                opcIsConnecting.set(false);
                try {
                    Thread.currentThread().sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                reClient(server);
            }
        }
        return server;
    }


}
