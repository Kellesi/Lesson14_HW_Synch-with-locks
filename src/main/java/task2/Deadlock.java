package task2;

import java.util.concurrent.locks.ReentrantLock;

public class Deadlock {
    public static void main(String[] args) {
        ReentrantLock lock1 = new ReentrantLock();
        ReentrantLock lock2 = new ReentrantLock();

        Thread th1= new Thread(()->{
            System.out.println(Thread.currentThread().getName()+" start");
            lock1.lock();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            lock2.lock();
            System.out.println(Thread.currentThread().getName()+" end");
            lock2.unlock();
            lock1.unlock();
        }, "Thread1");

        Thread th2= new Thread(()->{
            System.out.println(Thread.currentThread().getName()+" start");
            lock2.lock();
            lock1.lock();
            System.out.println(Thread.currentThread().getName()+" end");
            lock1.unlock();
            lock2.unlock();
        },"Thread2");

        th1.start();
        th2.start();
    }
}
