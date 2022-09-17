package engine.decryptionManager;

import DTOS.decryptionManager.DecryptionManagerDTO;
import JavaFX.BruteForce.BruteForceController;
import UIAdapter.UIAdapter;
import engine.decryptionManager.Agents.Agents;
import engine.decryptionManager.dictionary.Dictionary;
import engine.decryptionManager.dictionary.Trie;
import engine.decryptionManager.task.TasksManager;
import engine.enigma.Machine.EnigmaMachine;
import engine.enigma.keyboard.Keyboard;

import java.math.BigInteger;

public class DM {




    // TODO: 9/10/2022 change from agents to tasks
    public static enum DifficultyLevel{
        EASY, MEDIUM, HARD, IMPOSSIBLE
    }
    private Agents agents;
    private int maxAgentAmount;
    private Dictionary dictionary;
    private int possibleAmountOfCodes;

    private int missionSize;
    private EnigmaMachine machine;
    private int difficulty;
    private TasksManager tasksCreator;
    public DM(Dictionary dictionary, Agents agents, int maxAgentsAmount, EnigmaMachine machine){
        this.dictionary = dictionary;
        this.agents = agents;
        this.maxAgentAmount = maxAgentsAmount;
        this.machine = machine;

    }
    public void pauseCurrentTask() {
        tasksCreator.pauseCurrentTask();
    }
    public void resumeCurrentTask() {
        tasksCreator.resumeCurrentTask();
    }
    public void DecipherMessage(DecryptionManagerDTO decryptionManagerDTO, BruteForceController bruteForceController, Runnable onFinish){

        this.missionSize = decryptionManagerDTO.getMissionSize();
        // TODO: 9/16/2022 add check if agents amount ok
        if(decryptionManagerDTO.getAmountOfAgentsForProcess()>maxAgentAmount)
            throw new RuntimeException();
        this.tasksCreator = new TasksManager(decryptionManagerDTO,machine.clone());
        bruteForceController.bindTaskToUIComponents(tasksCreator, onFinish);
        new Thread(tasksCreator,"Manager tasks thread ").start();
    }
    public boolean isDictionaryContainString(String str){
        return dictionary.isDictionaryContainString(str);


    }
    public void cancelCurrentTask(){
        System.out.println("cancal!");

        tasksCreator.cancel();
    }
    public String cleanStringFromExcludeChars(String words){
        return dictionary.cleanStringFromExcludeChars(words);
    }
    public Trie getTrieFromDictionary(){
        return dictionary.createTrieFromDictionary();
    }


    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public long getPossibleAmountOfCodes() {
        return possibleAmountOfCodes;
    }




    public void setPossibleAmountOfCodes(int possibleAmountOfCodes) {
        this.possibleAmountOfCodes = possibleAmountOfCodes;
    }

    public EnigmaMachine getMachine() {
        return machine;
    }

    public void setMachine(EnigmaMachine machine) {
        this.machine = machine;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getMissionSize() {
        return missionSize;
    }

    public void setMissionSize(int missionSize) {
        this.missionSize = missionSize;
    }

    public int getAmountOfAgents() {
        return maxAgentAmount;
    }
    public  double calculateAmountOfTasks(int missionSize, DifficultyLevel level) {
        double amountOfMission = 0;
        switch (level) {
            case EASY:
                amountOfMission = calculateAmountOfTasksEasyLevel(missionSize);
                break;
            case MEDIUM:
                amountOfMission = calculateAmountOfTasksMediumLevel(missionSize);
                break;
            case HARD:
                amountOfMission = calculateAmountOfTasksHardLevel(missionSize);
                break;
            case IMPOSSIBLE:
                amountOfMission = calculateAmountOfTasksImpossibleLevel(missionSize);
                break;
        }
        return amountOfMission;
    }
    //calculate the amount of options to choose k rotors from n rotors in machine
    private double calculateAmountOfTasksImpossibleLevel(int missionSize){
        BigInteger ret = BigInteger.ONE;
        int amountOfRotors = machine.getAmountOfRotors();
        int chosenAmount = machine.getRotorsAmountInUse();
        for (int k = 0; k < chosenAmount; k++) {
            ret = ret.multiply(BigInteger.valueOf(amountOfRotors-k))
                    .divide(BigInteger.valueOf(k+1));
        }
        return ret.doubleValue() * calculateAmountOfTasksHardLevel(missionSize);
    }

    //calculate amountOfRotors factorial
    private double calculateAmountOfTasksHardLevel(int missionSize) {
        int factorial =1;
        for (int i = 1; i <= machine.getRotorsAmountInUse() ; i++) {
            factorial *=i;
        }
        return factorial* calculateAmountOfTasksMediumLevel(missionSize);
    }

    private double calculateAmountOfTasksMediumLevel(int missionSize){
        return calculateAmountOfTasksEasyLevel(missionSize)* machine.getReflectorsAmount();
    }
    private double calculateAmountOfTasksEasyLevel(int missionSize){
        int amountOfTasks = Keyboard.alphabet.length();
        int exponent = machine.getRotorsAmountInUse();
        double numOfTask = Math.pow(amountOfTasks,exponent);
        return numOfTask/missionSize;
    }


    public void setAgents(Agents agents) {
        this.agents = agents;
    }

}
