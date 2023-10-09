package task2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class LiveLock {
    public static void main(String[] args) {
        ReentrantLock lock1 = new ReentrantLock();
        ReentrantLock lock2 = new ReentrantLock();

        Thread thread1 = new Thread(()-> {
            while (true) {
                try {
                    lock1.tryLock(50,TimeUnit.MICROSECONDS);
                    System.out.println(Thread.currentThread().getName()+(": lock1 is locked"));
                    Thread.sleep(50);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName()+(": try to lock lock2"));
                if (lock2.tryLock()) {
                    System.out.println(Thread.currentThread().getName()+(": win"));
                    lock2.unlock();
                    lock1.unlock();
                    break;
                } else {
                    System.out.println(Thread.currentThread().getName()+(": cannot lock lock2, releasing lock1."));
                    lock1.unlock();
                }

            }
        },"Thread1");

        Thread thread2 = new Thread(()->{while (true) {
            try {
                lock2.tryLock(50,TimeUnit.MICROSECONDS);
                System.out.println(Thread.currentThread().getName()+(": lock2 is locked"));
                Thread.sleep(50);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName()+(": try to lock lock1"));
            if (lock1.tryLock()) {
                System.out.println(Thread.currentThread().getName()+(": win"));
                lock1.unlock();
                lock2.unlock();
                break;
            } else {
                System.out.println(Thread.currentThread().getName()+(": cannot lock lock1, releasing lock2."));
                lock2.unlock();
            }
        }

        },"Thread2");

        thread1.start();
        thread2.start();
    }
}

