package com.ww.oio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @ClassName:
 * @author:
 * @Description: OIOServer
 * @Date
 **/
public class PlainOioServer {
    public void serve(int port) throws IOException {
        //绑定端口
        final ServerSocket socket = new ServerSocket(port);
        try {
            for (; ; ) {
                //接受连接
                final Socket clientSocket = socket.accept();
                System.out.println("Accepted connection from " + clientSocket);
                //创建一个新的线程来处理该连接
                new Thread(() -> {
                    OutputStream out;
                    try {
                        out = clientSocket.getOutputStream();
                        out.write("Hi\r\n".getBytes("UTF-8"));
                        out.flush();
                        //关闭连接
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
