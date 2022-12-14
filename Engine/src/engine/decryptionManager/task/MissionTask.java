package engine.decryptionManager.task;

import DTOS.Configuration.UserConfigurationDTO;
import UIAdapter.UIAdapter;
import UIAdapter.UIAdapterImpJavaFX;
import engine.decryptionManager.dictionary.Dictionary;
import engine.enigma.Machine.EnigmaMachine;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class MissionTask implements Runnable{


    private final double totalMissionAmount;
    private final TimeToCalc timeToCalc;

    public volatile static AtomicBoolean finish;
    private volatile static AtomicLong missionAmountLeft;
    private EnigmaMachine machine;
    // TODO: 9/11/2022 return positions to private 
    public List<String> positions;
    private String toDecode;

    public BlockingDeque<AgentCandidatesList> getCandidateBlockingQueue() {
        return candidateBlockingQueue;
    }

    BlockingDeque<AgentCandidatesList> candidateBlockingQueue;
    private long StartingTime;


    public AgentCandidatesList getCandidatesList() {
        return candidatesList;
    }

    public AgentCandidatesList candidatesList;
    private UIAdapter uiAdapter;
    Runnable updateProgress;


    public MissionTask(EnigmaMachine machine, List<String> positions,
                       String toDecode, BlockingDeque<AgentCandidatesList> candidateBlockingQueue,
                       UIAdapter uiAdapter, double totalMissionAmount, TimeToCalc timeToCalc,
                       AtomicLong totalMissionAmountToSend, Runnable updateProgress){
        this.machine = machine;
        this.positions = positions;
        this.toDecode = toDecode;
        this.candidateBlockingQueue = candidateBlockingQueue;
        this.uiAdapter = uiAdapter;
        this.totalMissionAmount = totalMissionAmount;
        this.timeToCalc = timeToCalc;
        this.updateProgress = updateProgress;
        finish = new AtomicBoolean(false);
        missionAmountLeft = totalMissionAmountToSend;
    }

    public void setMachinePositions(String positions){
        machine.setPositions(positions);
    }

    @Override
    public void run() {
        this.StartingTime = System.nanoTime();
        String threadName = Thread.currentThread().getName();
        this.candidatesList = new AgentCandidatesList(StartingTime, threadName);
        exectuteMission(positions);
        if (!candidatesList.isEmpty()) {
            candidatesList.setDuration();
        }
        updateTime();


    }

    private void updateTime(){
        synchronized (timeToCalc){
            updateProgress.run();
            long totalTime = System.nanoTime() - StartingTime;
            timeToCalc.addTimeToAverageMissionTime(totalTime);
            timeToCalc.updateTotalTasksTime();
        }
    }

    private void exectuteMission(List<String> positions) {
        for (String position : positions){
            //processPosition(position);
            switch (uiAdapter.getState()) {
                case IN_PROCESS:
                    processPosition(position);
                    break;
                case STOP:
                    return;
                case PAUSE:
                    synchronized (uiAdapter) {
                        while (uiAdapter.getState() == UIAdapterImpJavaFX.RunningState.PAUSE) {
                            try {
                                //System.out.println("waiting.....");
                                uiAdapter.wait();
                            } catch (InterruptedException e) {
                                System.out.println("interupt at wait in Mission task");
                            }
                        }
                    }

            }
        }
    }

    private void processPosition(String position) {
        String decipherResult = decipher(position);
        if (decipherResult != null) {
            machine.setPositions(position);//return to the position that found the candidate
            updateCandidate(decipherResult);
        }
    }

    public String decipher(String position)
    {
        machine.setPositions(position);
        String decryptionResult = machine.encodeString(toDecode);
        List<String> words = splitDecryptionToWords(decryptionResult);
        //System.out.println(position);
        if(Dictionary.isWordsInDictionary(words)) {
            return decryptionResult;
        }
        else
            return null;
    }

    public List<String> splitDecryptionToWords(String decryptionResult){
        return Arrays.asList(decryptionResult.split(" "));
    }

    // TODO: 9/14/2022 maybe update in after execute 
    public void updateCandidate(String words) {
        String threadName = Thread.currentThread().getName();
        //System.out.println("-------------------------------------------------------------------------------------------");
        UserConfigurationDTO configurationDTO = new UserConfigurationDTO(machine);
        // candidateBlockingQueue.put(threadName+ ":" +words);

       // System.out.println(configurationDTO.getCodeConfigurationString());
        StringBuilder currConfig = configurationDTO.getCodeConfigurationString();
        //System.out.println(words);
        candidatesList.addCandidate(words,currConfig.toString());
    }
}
