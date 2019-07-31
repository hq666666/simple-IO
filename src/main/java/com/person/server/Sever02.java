package com.person.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hq
 * 服务端：
 *      使用线程池模拟
 */
public class Sever02 {
    private static  final Logger log = LoggerFactory.getLogger(Server01.class);
    private final  static  Integer PORT = 8989;

    //当前的线程数
    private static AtomicInteger now = new AtomicInteger(0);
    //总线程数
    private static AtomicInteger total = new AtomicInteger(0);
    //开始时间
    private static long beginTime =0;

    private static final ExecutorService pool = Executors.newFixedThreadPool(100);

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(PORT);
        new Thread(new Sever02.Monitor()).start();
        while (true){
            Socket socket = serverSocket.accept();
            if(beginTime == 0)beginTime = System.currentTimeMillis();
            now.incrementAndGet();
            total.incrementAndGet();
            pool.execute(()->{
                try {
                    PrintWriter writer = new PrintWriter(socket.getOutputStream());
                    Thread.sleep(2000);
                    String msg = "hello";
                    writer.write(msg);
                    writer.close();
                    now.decrementAndGet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    static class Monitor implements Runnable {
        @Override
        public void run() {
            while (true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //获取活动的线程池中的线程数
                int activeCount = ((ThreadPoolExecutor) pool).getActiveCount();
                log.info("now={},total={},threadActiveAccount={}",now.get(),total.get(),activeCount);
                if(now.get()==0 && total.get() == 5000){
                    log.info("spend time ={}s",(System.currentTimeMillis()-beginTime)/1000);
                }
            }
        }
    }
}
