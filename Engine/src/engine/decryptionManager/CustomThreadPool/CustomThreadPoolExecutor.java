package engine.decryptionManager.CustomThreadPool;

import engine.decryptionManager.task.AgentCandidatesList;
import engine.decryptionManager.task.Task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CustomThreadPoolExecutor extends ThreadPoolExecutor {

    public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
                                    long keepAliveTime, TimeUnit unit,
                                    BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,threadFactory);
    }



    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (t != null) {
            System.out.println("Perform exception handler logic");
        }
        //System.out.println("Perform afterExecute() logic");
        Task task = (Task)r;
        try {
            //System.out.println("Perform exception handler logic");
            AgentCandidatesList candidatesList = task.getCandidatesList();
            if(!candidatesList.isEmpty()) {
                System.out.println(Thread.currentThread().getName());
                System.out.println("add to ququqe");
                task.getCandidateBlockingQueue().put(candidatesList);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
