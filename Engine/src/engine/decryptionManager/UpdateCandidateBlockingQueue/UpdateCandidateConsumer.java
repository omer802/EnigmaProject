package engine.decryptionManager.UpdateCandidateBlockingQueue;

import UIAdapter.UIAdapter;

import java.util.concurrent.BlockingDeque;

public class UpdateCandidateConsumer implements Runnable {
    BlockingDeque<String> blockingDeque;
    UIAdapter uiAdapter;
    boolean isRunning;
    public UpdateCandidateConsumer(BlockingDeque blockingDeque, UIAdapter uiAdapter){
        this.blockingDeque = blockingDeque;
        this.uiAdapter = uiAdapter;
        this.isRunning = true;

    }

    @Override
    public void run() {
        try {
            final String threadName = Thread.currentThread().getName();
            String item;
            while (isRunning) {
                System.out.println("Thread " + threadName + " is about to consume item");
                synchronized (uiAdapter) {
                    item = blockingDeque.take();
                    uiAdapter.AddCandidateStringForDecoding(item);
                }
                System.out.println("Thread " + threadName + " consumed item: " + item);
            }
        } catch (InterruptedException e) {
            System.out.println("Was interrupted !");
        }
    }
    public void finish() {
        isRunning = false;

    }
}
