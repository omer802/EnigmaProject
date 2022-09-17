package engine.decryptionManager.task;

import DTOS.Configuration.UserConfigurationDTO;
import UIAdapter.UIAdapter;
import engine.decryptionManager.dictionary.Dictionary;
import engine.enigma.Machine.EnigmaMachine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingDeque;

public class Task implements Runnable{


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


    public Task(EnigmaMachine machine, List<String> positions,
                String toDecode, BlockingDeque<AgentCandidatesList> candidateBlockingQueue, UIAdapter uiAdapter){
        this.machine = machine;
        this.positions = positions;
        this.toDecode = toDecode;
        this.candidateBlockingQueue = candidateBlockingQueue;
        this.uiAdapter = uiAdapter;
    }

    public void setMachinePositions(String positions){
        machine.setPositions(positions);
    }

    @Override
    public void run() {

        this.StartingTime = System.nanoTime();
        String threadName = Thread.currentThread().getName();
        this.candidatesList = new AgentCandidatesList(StartingTime,threadName);
        for (String position: positions) {
            String decipherResult = decipher(position);
            if (decipherResult != null) {
                machine.setPositions(position);//return to the position that found the candidate
                updateCandidate(decipherResult);
                System.out.println("updateCandidate");
            }
        }
    }

    public String decipher(String position)
    {
         //System.out.println(position);
        machine.setPositions(position);
        String decryptionResult = machine.encodeString(toDecode);
        List<String> words = splitDecryptionToWords(decryptionResult);
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
        System.out.println("-------------------------------------------------------------------------------------------");
        UserConfigurationDTO configurationDTO = new UserConfigurationDTO(machine);
        // candidateBlockingQueue.put(threadName+ ":" +words);

        System.out.println(configurationDTO.getCodeConfigurationString());
        StringBuilder currConfig = configurationDTO.getCodeConfigurationString();
        System.out.println(words);
        //CandidateList.add(threadName+ ":" +words+":"+currConfig);
        candidatesList.addCandidate(words,currConfig.toString());

    }
}
