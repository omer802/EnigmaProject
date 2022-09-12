package engine.decryptionManager.task;

import engine.decryptionManager.dictionary.Dictionary;
import engine.enigma.Machine.EnigmaMachine;

import java.util.Arrays;
import java.util.List;

public class Task implements Runnable{

    private EnigmaMachine machine;
    // TODO: 9/11/2022 return positions to private 
    public List<String> positions;
    private String toDecode;

    public Task(EnigmaMachine machine, List<String> positions, String toDecode){
        this.machine = machine;
        this.positions = positions;
        this.toDecode = toDecode;
    }
    public void setMachinePositions(String positions){
        machine.setPositions(positions);
    }

    @Override
    public void run() {
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
            updateCandidate(words);
            System.out.println(position);
        }
    }

    public List<String> splitDecryptionToWords(String decryptionResult){
        return Arrays.asList(decryptionResult.split(" "));
    }
    public void updateCandidate(List<String> words){
        System.out.println(Thread.currentThread().getId());
        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println(words);

    }
}
