package com.lmhy.tio.client;

import com.lmhy.tio.server.Const;
import com.lmhy.tio.server.TioPacket;
import lombok.extern.slf4j.Slf4j;
import org.tio.client.AioClient;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Aio;
import org.tio.core.Node;

@Slf4j
public class TioClientCore {
    //服务器节点
    public static Node serverNode = new Node(Const.SERVER, Const.PORT);
    //handler, 包括编码、解码、消息处理
    public static TioClientAioHandler aioClientHandler = new TioClientAioHandler();
    //事件监听器，可以为 null，但建议自己实现该接口，可以参考 showcase 了解些接口
    public static ClientAioListener aioListener = null;
    //断链后自动连接的，不想自动连接请设为 null
    private static ReconnConf reconnConf = new ReconnConf(5000L);
    //一组连接共用的上下文对象
    public static ClientGroupContext clientGroupContext = new ClientGroupContext(aioClientHandler,
            aioListener, reconnConf);

    public static AioClient aioClient = null;
    public static ClientChannelContext clientChannelContext = null;

    public static void start() {
        clientGroupContext.setHeartbeatTimeout(Const.TIMEOUT);
        try {
            aioClient = new AioClient(clientGroupContext);
            clientChannelContext = aioClient.connect(serverNode);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[Tio]Error - {}", e);
        }
    }

    public static void send(String msg) {
        try {
            TioPacket packet = new TioPacket();
            packet.setBody(msg.getBytes(TioPacket.CHARSET));
            Aio.send(clientChannelContext, packet);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
