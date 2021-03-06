package com.lmhy.tio.server;

import org.tio.server.AioServer;
import org.tio.server.ServerGroupContext;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;

import java.io.IOException;

public class ServerMain {

    //handler, 包括编码、解码、消息处理
    public static ServerAioHandler aioHandler = new TioServerAioHandler();

    //事件监听器，可以为null，但建议自己实现该接口，可以参考showcase了解些接口
    public static ServerAioListener aioListener = null;

    //一组连接共用的上下文对象
    public static ServerGroupContext serverGroupContext = new ServerGroupContext(aioHandler, aioListener);

    //aioServer对象
    public static AioServer aioServer = new AioServer(serverGroupContext);

    //有时候需要绑定ip，不需要则null
    public static String serverIp = null;

    //监听的端口
    public static int serverPort = Const.PORT;

    public static void main(String[] args) throws IOException {
        serverGroupContext.setHeartbeatTimeout(Const.TIMEOUT);
        aioServer.start(serverIp, serverPort);
    }
}
