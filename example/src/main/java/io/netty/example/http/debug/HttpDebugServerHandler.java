/*
 * Copyright 2013 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.example.http.debug;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleStateEvent;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpHeaderValues.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

public class HttpDebugServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    private static final byte[] CONTENT = { 'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd' };
    // 记录读超时几次了，用来判断是否断开该连接
    int readIdleTimes = 0;

    /**
     * 个人追加
     * 用户事件触发
     *
     * 当 IdleStateHandler 发现读超时后，会调用 fireUserEventTriggered() 去执行后一个 Handler 的 userEventTriggered 方法。
     * 所以，根据心跳检测状态去关闭连接的就写在这里！
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // 入站的消息就是 IdleStateEvent 具体的事件
            IdleStateEvent event = (IdleStateEvent) evt;

            String eventType = null;
            // 我们在 IdleStateHandler 中也看到了，它有读超时，写超时，读写超时等
            // 所以，这里我们需要判断事件类型
            switch (event.state()) {
                case READER_IDLE:
                    eventType = "读空闲";
                    readIdleTimes++; // 读空闲的计数加 1
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break; // 不处理
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break; // 不处理
            }

            // 打印触发了一次超时警告
            System.out.println(ctx.channel().remoteAddress() + "超时事件：" + eventType);

            // 当读超时超过 3 次，我们就端口该客户端的连接
            // 注：读超时超过 3 次，代表起码有 4 次 3s 内客户端没有发送心跳包或普通数据包
            if (readIdleTimes > 3) {
                System.out.println(" [server]读空闲超过3次，关闭连接，释放更多资源");
                ctx.channel().writeAndFlush("idle close");
                ctx.channel().close(); // 手动断开连接
            }
        }
    }



    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
        if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;

            boolean keepAlive = HttpUtil.isKeepAlive(req);
            FullHttpResponse response = new DefaultFullHttpResponse(req.protocolVersion(), OK,
                                                                    Unpooled.wrappedBuffer(CONTENT));
            response.headers()
                    .set(CONTENT_TYPE, TEXT_PLAIN)
                    .setInt(CONTENT_LENGTH, response.content().readableBytes());

            if (keepAlive) {
                if (!req.protocolVersion().isKeepAliveDefault()) {
                    response.headers().set(CONNECTION, KEEP_ALIVE);
                }
            } else {
                // Tell the client we're going to close the connection.
                response.headers().set(CONNECTION, CLOSE);
            }

            ChannelFuture f = ctx.write(response);

            if (!keepAlive) {
                f.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


    /**
     * 可以结合水平线控制flush频率
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        if(!ctx.channel().isWritable()){
            ctx.flush();
        }
        System.out.println("channelWritabilityChanged");
    }
}
