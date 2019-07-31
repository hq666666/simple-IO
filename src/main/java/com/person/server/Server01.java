package com.person.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hq
 * 服务端：
 *      使用多线程模拟：
 *
 *          01：使用1000条线程进行模拟，总共花费3s;
 *          02：使用5000条线程进行模拟，有2/5的线程连接超时；
 *
 */
public class Server01 {

    private static  final Logger log = LoggerFactory.getLogger(Server01.class);
    private final  static  Integer PORT = 8989;

    //当前的线程数
    private static AtomicInteger now = new AtomicInteger(0);
    //总线程数
    private static AtomicInteger total = new AtomicInteger(0);
    //开始时间
    private static long beginTime =0;

    public static void main(String[] args) throws IOException {
        //服务端socket
        ServerSocket serverSocket = new ServerSocket(PORT);
        //启动监控线程
        new Thread(new Server01.Monitor()).start();

        while(true){
            Socket accept = serverSocket.accept();
            if(beginTime == 0){
                beginTime = System.currentTimeMillis();
            }
            //incrementAndGet(此方法在多线程中具有原子性)
            now.incrementAndGet();
            total.incrementAndGet();
            new Thread(()->{
                try {
                    PrintWriter writer = new PrintWriter(accept.getOutputStream());
                    Thread.sleep(2000);
                     String msg = "hello";
                     writer.write(msg);
                     writer.close();
                     now.decrementAndGet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    static class Monitor implements Runnable{
        @Override
        public void run() {
           while (true){
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               log.info("now={},total={}",now.get(),total.get());
               /**
                * total选项：
                *
                *       1000
                *       5000
                */
               if(now.get() ==0 && total.get() == 5000){
                   log.info("spend time={}s ",(System.currentTimeMillis()-beginTime)/1000);
               }
           }
        }
    }
}
