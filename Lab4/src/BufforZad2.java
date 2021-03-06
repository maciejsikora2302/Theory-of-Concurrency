import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BufforZad2 implements GeneralBuffor{
    private int size;
    private int free;
    private int taken;
    private Lock lock = new ReentrantLock();
    private Condition producerLock = lock.newCondition();
    private Condition consumerLock = lock.newCondition();
    private boolean print;
    public BufforZad2(int size, boolean print){
        this.size = 2*size;
        this.free = 2*size;
        this.taken = 0;
        this.print = print;
    }

    public boolean put(int howMuch) throws InterruptedException {
        lock.lock();
        while(this.free <= howMuch){
            producerLock.await();
        }
        if(print) System.out.printf("Producer was able to put %d, and before he did there was %d taken and %d free\n", howMuch, taken, free);
        this.taken += howMuch;
        this.free -= howMuch;
        consumerLock.signalAll();
        lock.unlock();
        return true;
    }

    public boolean get(int howMuch) throws InterruptedException {
        lock.lock();
        while (this.taken <= howMuch){
            consumerLock.await();
        }
        if(print) System.out.printf("Consumer was able to get %d, and before he did there was %d taken and %d free\n", howMuch, taken, free);
        this.taken -= howMuch;
        this.free += howMuch;
        producerLock.signalAll();
        lock.unlock();
        return true;
    }
}
