package task1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        Resources resources = new Resources();

        Producer producer = new Producer(resources);

        Consumer consumer = new Consumer(resources);

        consumer.start();
        producer.start();
    }
}

class Resources {

    private static final int CAPACITY = 5;
    private int counter = 0;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition cond = lock.newCondition();

    public void increaseCounter() {
        lock.lock();
        try {
            while (counter == CAPACITY) {
                waitSomeTime();
            }
            Thread thread = Thread.currentThread();
            ++counter;
            System.out.println("Increase counter, thread: " + thread.getName() + ", counter= " + counter);
        } finally {
            cond.signal();
            lock.unlock();
        }
    }

    public void decreaseCounter() {
        lock.lock();
        try {
            while (counter == 0) {
                waitSomeTime();
            }
            Thread thread = Thread.currentThread();
            --counter;
            System.out.println("Decrease counter, thread: " + thread.getName() + ", counter= " + counter);
        }finally {
            cond.signal();
            lock.unlock();
        }
    }

    private static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void waitSomeTime() {
        try {
            cond.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


}

class Consumer extends Thread {

    private final Resources resources;


    Consumer(Resources resources) {
        this.resources = resources;
        setName("ConsumerProducer.Consumer");
    }


    @Override
    public void run() {
        while (true) {
            // try to get resource monitor
            resources.decreaseCounter();
            // release resource monitor
        }
    }
}

class Producer extends Thread {
    private final Resources resources;

    Producer(Resources resources) {
        this.resources = resources;
        setName("ConsumerProducer.Producer");
    }

    @Override
    public void run() {
        while (true) {
            // try to get resource monitor
            resources.increaseCounter();
            // release resource monitor
        }
    }
}