package com.lmhy.tio.client;

import com.lmhy.tio.server.TioPacket;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;

import java.nio.ByteBuffer;

public class TioClientAioHandler implements ClientAioHandler {
    private static TioPacket heartbeatPacket = new TioPacket();

    @Override
    public Packet heartbeatPacket() {
        return heartbeatPacket;
    }

    @Override
    public Packet decode(ByteBuffer byteBuffer, ChannelContext channelContext) throws AioDecodeException {
        int readableLength = byteBuffer.limit() - byteBuffer.position();
        if (readableLength < TioPacket.HEADER_LENGHT) {
            return null;
        }
        int bodyLength = byteBuffer.getInt();
        if (bodyLength < 0) {
            throw new AioDecodeException("bodyLength [" + bodyLength + "] is not right, remote:" +
                    channelContext.getClientNode());
        }
        //计算本次需要的数据长度
        int neededLength = TioPacket.HEADER_LENGHT + bodyLength;
        //收到的数据是否足够组包
        int isDataEnough = readableLength - neededLength;
        // 不够消息体长度(剩下的 buffe 组不了消息体)
        if (isDataEnough < 0) {
            return null;
        } else //组包成功
        {
            TioPacket imPacket = new TioPacket();
            if (bodyLength > 0) {
                byte[] dst = new byte[bodyLength];
                byteBuffer.get(dst);
                imPacket.setBody(dst);
            }
            return imPacket;
        }
    }

    @Override
    public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        TioPacket tioPacket = (TioPacket) packet;
        byte[] body = tioPacket.getBody();
        int bodyLen = 0;
        if (body != null) {
            bodyLen = body.length;
        }
        //bytebuffer 的总长度是 = 消息头的长度 + 消息体的长度
        int allLen = TioPacket.HEADER_LENGHT + bodyLen;
        //创建一个新的 bytebuffer
        ByteBuffer buffer = ByteBuffer.allocate(allLen);
        //设置字节序
        buffer.order(groupContext.getByteOrder());
        //写入消息头----消息头的内容就是消息体的长度
        buffer.putInt(bodyLen);
        //写入消息体
        if (body != null) {
            buffer.put(body);
        }
        return buffer;
    }

    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {
        TioPacket tioPacket = (TioPacket) packet;
        byte[] body = tioPacket.getBody();
        if (body == null) {
            return;
        }
        String str = new String(body, TioPacket.CHARSET);
        System.out.println("收到消息：" + str);
    }
}
