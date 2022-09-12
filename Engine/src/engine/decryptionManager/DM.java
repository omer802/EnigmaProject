package engine.decryptionManager;

import engine.decryptionManager.Agents.Agents;
import engine.decryptionManager.dictionary.Dictionary;
import engine.decryptionManager.task.tasksCreator;
import engine.enigma.Machine.EnigmaMachine;

public class DM {


    // TODO: 9/10/2022 change from agents to tasks
    private Agents agents;
    private int AgentsAmount;
    private Dictionary dictionary;
    private int possibleAmountOfCodes;

    private int missionSize;
    private EnigmaMachine machine;
    private int difficulty;
    private tasksCreator taskCreator;
    public DM(Dictionary dictionary, Agents agents, int agentsAmount){
        this.dictionary = dictionary;
        this.agents = agents;
        this.AgentsAmount = agentsAmount;

    }
    public void DecipherMessage(String messageToDecipher,int difficulty,int missionSize){
        this.missionSize = missionSize;
        tasksCreator tasksCreator =new tasksCreator(messageToDecipher,missionSize,difficulty,AgentsAmount,machine.clone());
        //tasksCreator.run();
        new Thread(tasksCreator,"tasksCreatorThread").start();
        //tasksCreator.start();
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

    public Agents getAgents() {
        return agents;
    }

    public void setAgents(Agents agents) {
        this.agents = agents;
    }

}
