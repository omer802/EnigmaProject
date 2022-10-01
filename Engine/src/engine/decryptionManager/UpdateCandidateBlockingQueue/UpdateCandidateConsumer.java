package engine.decryptionManager.UpdateCandidateBlockingQueue;

import UIAdapter.UIAdapter;
import engine.decryptionManager.task.AgentCandidatesList;

import java.util.concurrent.BlockingDeque;

public class UpdateCandidateConsumer implements Runnable {



    BlockingDeque<AgentCandidatesList> blockingDeque;
    UIAdapter uiAdapter;

    public static void setRunning(boolean running) {
        UpdateCandidateConsumer.Running = running;
    }

    static boolean Running;
    public UpdateCandidateConsumer(BlockingDeque<AgentCandidatesList> blockingDeque, UIAdapter uiAdapter){
        this.blockingDeque = blockingDeque;
        this.uiAdapter = uiAdapter;
        this.Running = true;

    }

    @Override
    public void run() {
        try {
            final String threadName = Thread.currentThread().getName();
            AgentCandidatesList agentCandidateList;
            //System.out.println("***************starting working queque candidate***********"+ threadName);
            while (Running) {
                //System.out.println("Thread " + threadName + " is about to consume item");
                agentCandidateList = blockingDeque.take();
                if(agentCandidateList.isPoisonPill()){
                    break;
                }
                synchronized (uiAdapter) {
                    uiAdapter.AddCandidateStringForDecoding(agentCandidateList);
                }
                //System.out.println("Thread " + threadName + " consumed item: " + agentCandidateList.getCandidates()+":"+ agentCandidateList.getAgentName());
            }
                //System.out.println("consumer done********************************************");

        } catch (InterruptedException e) {
            System.out.println("Was interrupted !");
        }
    }
    public void finish() {
        AgentCandidatesList agentCandidatesList = new AgentCandidatesList();
        agentCandidatesList.setPoisonPill();
        try {
            blockingDeque.put(agentCandidatesList);
        } catch (InterruptedException e) {
            System.out.println("problem at poison pill");
        }
        Running = false;

    }
}
