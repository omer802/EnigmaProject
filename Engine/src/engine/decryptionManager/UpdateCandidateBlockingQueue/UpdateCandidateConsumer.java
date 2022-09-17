package engine.decryptionManager.UpdateCandidateBlockingQueue;

import UIAdapter.UIAdapter;
import engine.decryptionManager.task.AgentCandidatesList;

import java.util.List;
import java.util.concurrent.BlockingDeque;

public class UpdateCandidateConsumer implements Runnable {


    BlockingDeque<AgentCandidatesList> blockingDeque;
    UIAdapter uiAdapter;
    static boolean isRunning;
    public UpdateCandidateConsumer(BlockingDeque<AgentCandidatesList> blockingDeque, UIAdapter uiAdapter){
        this.blockingDeque = blockingDeque;
        this.uiAdapter = uiAdapter;
        this.isRunning = true;

    }

    @Override
    public void run() {
        try {
            final String threadName = Thread.currentThread().getName();
            AgentCandidatesList AgentCandidateList;
            while (isRunning) {
                //System.out.println("Thread " + threadName + " is about to consume item");
                AgentCandidateList = blockingDeque.take();

                synchronized (uiAdapter) {
                    uiAdapter.AddCandidateStringForDecoding(AgentCandidateList);
                }
               // System.out.println("Thread " + threadName + " consumed item: " + AgentCandidateList.getCandidates()+":"+ AgentCandidateList.getAgentName());
            }
                System.out.println("consumer done********************************************");

        } catch (InterruptedException e) {
            System.out.println("Was interrupted !");
        }
    }
    public void finish() {
        isRunning = false;

    }
}
