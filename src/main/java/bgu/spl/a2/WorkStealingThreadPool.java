package bgu.spl.a2;
import java.util.ArrayList;
import java.util.*;



/**
 * represents a work stealing thread pool - to understand what this class does
 * please refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class WorkStealingThreadPool {

    /**
     * creates a {@link WorkStealingThreadPool} which has nthreads
     * {@link Processor}s. Note, threads should not get started until calling to
     * the {@link #start()} method.
     *
     * Implementors note: you may not add other constructors to this class nor
     * you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param nthreads the number of threads that should be started by this
     * thread pool
     */

    private Processor[] processors;
    private ArrayList<Thread> threads;
    private VersionMonitor versionMonitor;
    private int nthreads;

    public WorkStealingThreadPool(int nthreads) {
        this.nthreads=nthreads;
        processors = new Processor[nthreads];
        versionMonitor = new VersionMonitor();
        threads = new ArrayList<Thread>();
        for(int i=0; i < this.nthreads; i++){
            processors[i]= new Processor(i, this);
            threads.add(new Thread(processors[i]));
        }
    }

    /**
     * submits a task to be executed by a processor belongs to this thread pool
     *
     * @param task the task to execute
     */
    public void submit(Task<?> task) {
        Random rnd = new Random();
        int randomProc = rnd.nextInt(this.nthreads);
        processors[randomProc].addTask(task);
    }

    /**
     * closes the thread pool - this method interrupts all the threads and wait
     * for them to stop - it is returns *only* when there are no live threads in
     * the queue.
     *
     * after calling this method - one should not use the queue anymore.
     *
     * @throws InterruptedException if the thread that shut down the threads is
     * interrupted
     * @throws UnsupportedOperationException if the thread that attempts to
     * shutdown the queue is itself a processor of this queue
     */
    public void shutdown() throws InterruptedException {
        for(Thread t1: threads){
            if(t1.getId()==t1.currentThread().getId())
                throw new UnsupportedOperationException("A Processor can't shutdown!");
        }
        for(Thread t1: threads){
            t1.interrupt();
        }
            for(Thread t1: threads){
                t1.join();
            }
    }

    /**
     * start the threads belongs to this thread pool
     */
    public void start() {
        for(Thread t1:threads){
            t1.start();
        }
    }

    /**
     * returns the VersionMonitor
     * @return versionMonitor
     */
    public VersionMonitor getVersionMonitor(){
        return versionMonitor;
    }

    /**
     * returns the processors array
     * @return processors
     */
    public Processor[] getProcessors(){
        return processors;
    }
}

