package engine.decryptionManager.task;

import DTOS.Configuration.UserConfigurationDTO;
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
    BlockingDeque<String> candidateBlockingQueue;
    private long StartingTime;

    public Task(EnigmaMachine machine, List<String> positions, String toDecode, BlockingDeque<String> candidateBlockingQueue){
        this.machine = machine;
        this.positions = positions;
        this.toDecode = toDecode;
        this.candidateBlockingQueue = candidateBlockingQueue;
    }
    public void setMachinePositions(String positions){
        machine.setPositions(positions);
    }

    @Override
    public void run() {
        List<String> CandidateString = new ArrayList<>();
        long TaskTime = System.nanoTime();
        for (String position: positions) {
            decipher(position);
        }
    }
    public void runTry(){
        for (String position: positions) {
            decipher(position);
        }
    }
    public void decipher(String position)
    {
         //System.out.println(position);
        machine.setPositions(position);
        String decryptionResult = machine.encodeString(toDecode);
        List<String> words = splitDecryptionToWords(decryptionResult);
        if(Dictionary.isWordsInDictionary(words)) {
            machine.setPositions(position);
            updateCandidate(decryptionResult);
        }
    }

    public List<String> splitDecryptionToWords(String decryptionResult){
        return Arrays.asList(decryptionResult.split(" "));
    }

    // TODO: 9/14/2022 maybe update in after execute 
    public void updateCandidate(String words){
        try {
            String threadName = Thread.currentThread().getName();
            System.out.println("-------------------------------------------------------------------------------------------");
            UserConfigurationDTO configurationDTO = new UserConfigurationDTO(machine);
            candidateBlockingQueue.put(threadName+ ":" +words);

            System.out.println(configurationDTO.getCodeConfigurationString());
            System.out.println(words);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
