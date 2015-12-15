class Q {
    int n;
    boolean isPut;
    int numOfActiveProducers;

    //Object producersMonitor = new Object();

    synchronized void decNumOfActiveProducers() {
        this.numOfActiveProducers--;
    }

    synchronized void incNumOfActiveProducers() {
        this.numOfActiveProducers++;
    }

    synchronized int getNumOfActiveProducers() {
        return this.numOfActiveProducers;
    }

    synchronized void put(int n) {
        //synchronized(producersMonitor) {
            while (isPut) {
                try {
                    wait();
                    //producersMonitor.wait();
                } catch (InterruptedException e) {
                    System.out.println("Interrupted: " + e);
                }
            }
            this.n = n;
            isPut = true;
        //}
        notifyAll();
    }

    synchronized int get() {
        while (!isPut) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e);
            }
        }
        isPut = false;
        // Problem is here. Not a producer is notified, the notified thread starts waiting and that's it.
        // We need to specifically notify the producer.
        //synchronized(producersMonitor) {
        //    producersMonitor.notifyAll();
        notifyAll();
        //}
        return this.n;
    }
}

class Producer implements Runnable{
    final int MAX_NUMBER = 100;

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
            //synchronized(q) {
                q.put(p);
                System.out.println("Producer [" + name + "] put: " + p);
            //}
        }
        q.decNumOfActiveProducers();
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
            //synchronized(q) {
                int c = q.get();
                System.out.println("Consumer [" + name + "] got: " + c);
            //}
        }
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
