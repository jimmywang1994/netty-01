package com.ww.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        //创建eventLoop处理客户端事件，需要适用于NIO实现
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //创建bootstrap
            Bootstrap b = new Bootstrap();
            b.group(group)
                    //适用于NIO传输类型的channel类型
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) throws Exception {
                            //创建channel时，向channelPipeline中添加一个EchoClientHandler实例
                            socketChannel.pipeline().addLast(new EchoClientlHandler());
                        }
                    });
            //连接到远程节点，阻塞等待直到连接完成
            ChannelFuture future = b.connect().sync();
            //阻塞，直到channel关闭
            future.channel().closeFuture().sync();
        } finally {
            //关闭线程池并释放所有资源
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("useage:" + EchoClient.class.getSimpleName() + "<host><port>");
            return;
        }
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        new EchoClient(host, port).start();
    }
}
