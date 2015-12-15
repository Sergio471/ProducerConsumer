class Q {
    int n;
    volatile boolean isPut;
    int numOfActiveProducers;

    synchronized void decNumOfActiveProducers() {
        this.numOfActiveProducers--;
    }

    synchronized void incNumOfActiveProducers() {
        this.numOfActiveProducers++;
    }

    synchronized int getNumOfActiveProducers() {
        return this.numOfActiveProducers;
    }

    synchronized void put(int n, String producerName) {
        while (isPut) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e);
            }
        }
        this.n = n;
        System.out.println("Producer [" + producerName + "] put: " + n);
        isPut = true;
        notifyAll();
    }

    synchronized int get(String consumerName) {
        while (!isPut) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e);
            }
        }
        isPut = false;
        notifyAll();
        System.out.println("Consumer [" + consumerName + "] got: " + this.n);
        return this.n;
    }
}

class Producer implements Runnable{
    final int MAX_NUMBER = 10;

    Q q;
    Thread t;
    String name;

    public Producer(String name, Q q) {
        this.name = name;
        this.q = q;
        q.incNumOfActiveProducers();
        t = new Thread(this, name);
    }

    public void run() {
        for (int p = 1; p <= MAX_NUMBER ; p++) {
            q.put(p, name);
        }
        q.decNumOfActiveProducers();
        System.out.println("Exiting producer " + name);
    }
}

class Consumer implements Runnable {
    Q q;
    Thread t;
    String name;

    public Consumer(String name, Q q) {
        this.q = q;
        this.name = name;
        t = new Thread(this, name);
    }

    public void run() {
        while (q.getNumOfActiveProducers() > 0) {
            int c = q.get(name);
        }
        System.out.println("Exiting consumer " + name);
    }
}

public class ProducerConsumer {
    public static void main(String[] args) {
        Q q = new Q();

        new Producer("Producer 1", q).t.start();
        new Producer("Producer 2", q).t.start();
        new Producer("Producer 3", q).t.start();
        new Producer("Producer 4", q).t.start();
        
        new Consumer("Consumer 1", q).t.start();
        new Consumer("Consumer 2", q).t.start();
        new Consumer("Consumer 3", q).t.start();
        new Consumer("Consumer 4", q).t.start();
        new Consumer("Consumer 5", q).t.start();
        new Consumer("Consumer 6", q).t.start();
        new Consumer("Consumer 7", q).t.start();
        new Consumer("Consumer 8", q).t.start();
        new Consumer("Consumer 9", q).t.start();
        new Consumer("Consumer 10", q).t.start();
        new Consumer("Consumer 11", q).t.start();
    }
}
