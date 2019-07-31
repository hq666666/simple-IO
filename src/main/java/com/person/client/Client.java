package com.person.client;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author hq
 * 客户端
 */
public class Client {

    private final  static  String HOST = "127.0.0.1";
    private final  static  Integer PORT = 8989;
    public static void main(String[] args) {
        /**
         * 模拟选项：
         *          1000
         *          5000
         */
        for(int i=1;i<=5000;i++){
           new Thread(()->{
               try {
                   new Socket(HOST,PORT);
               } catch (IOException e) {
                   throw  new RuntimeException("连接异常");
               }
           }).start();

        }
    }
}
