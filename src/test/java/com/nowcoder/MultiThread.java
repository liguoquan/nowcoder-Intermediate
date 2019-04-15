package com.nowcoder;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class MyThread extends Thread {
    private int tid;

    public MyThread(int tid) {
        this.tid = tid;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                System.out.println(String.format("T%d:%d", tid, i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Producer implements Runnable{
    private BlockingQueue<String> q;

    Producer (BlockingQueue<String> q){
        this.q = q;
    }
    @Override
    public void run() {
        try {
            for (int i=0;i<10;i++){
                Thread.sleep(1000);
                q.put(String.valueOf(i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable{
    private BlockingQueue<String> q;

    Consumer (BlockingQueue<String> q){
        this.q = q;
    }
    @Override
    public void run() {
        try {
            while (true){
                System.out.println(Thread.currentThread().getName() + ":" + q.take());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

public class MultiThread {
    public static void testThread() {
        for (int i = 0; i < 10; i++) {
//            new MyThread(i).start();
        }
        for (int i = 0; i < 10; i++) {
            int tid = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < 10; i++) {
                            Thread.sleep(1000);
                            System.out.println(String.format("T%d:%d", tid, i));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void testBlockingQueue(){
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(10);
        new Thread(new Producer(blockingQueue)).start();
        new Thread(new Consumer(blockingQueue),"consumer1").start();
        new Thread(new Consumer(blockingQueue),"consumer2").start();

    }

    private static int count = 0;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void sleep(int millis){
        try {
//            Thread.sleep(new Random().nextInt(millis));
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testWithAtomic(){
        for(int j=0;j<10;j++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i=0;i<10;i++){
                        sleep(1000);
                        System.out.println(atomicInteger.incrementAndGet());
                    }

                }
            }).start();
        }
    }

    public static void testWithoutAtomic(){
        for(int j=0;j<10;j++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i=0;i<10;i++){
                        sleep(1000);
                        count ++;
                        System.out.println(count);
                    }

                }
            }).start();
        }
    }

    private static ThreadLocal<Integer> threadLocalUserIds = new ThreadLocal<>();
    private static int userId;

    public static void testThreadLocal(){
        for(int i=0;i<10;i++){
            final int fianlI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    threadLocalUserIds.set(fianlI);
                    sleep(1000);
                    System.out.println("ThreadLocal:" + threadLocalUserIds.get());
                }
            }).start();
        }

        for(int i=0;i<10;i++){
            final int fianlI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    userId = fianlI;
                    sleep(1000);
                    System.out.println("NoThreadLocal:" + userId);
                }
            }).start();
        }
    }

    public static void testAtomic(){
        testWithAtomic();
//        testWithoutAtomic();
    }

    public static void testExecutor(){
//        ExecutorService service = Executors.newSingleThreadExecutor();
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<10;i++){
                    sleep(1000);
                    System.out.println("Executor1 " + i);
                }
            }
        });
        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<10;i++){
                    sleep(1000);
                    System.out.println("Executor2 " + i);
                }
            }
        });
        service.shutdown();
        while (!service.isTerminated()){
            sleep(1000);
            System.out.println("Wait for termination.");
        }
    }

    public static void testFuture(){
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Integer> future = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                sleep(1000);
                return 1;
            }
        });
        service.shutdown();
        try {
            System.out.println(future.get(100,TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        testThread();
//        testBlockingQueue();
//        testAtomic();
//        testThreadLocal();
//        testExecutor();
        testFuture();
    }
}
