package engine.decryptionManager;

import UIAdapter.UIAdapter;
import com.sun.org.apache.xml.internal.utils.Trie;
import engine.decryptionManager.Agents.Agents;
import engine.decryptionManager.dictionary.Dictionary;
import engine.decryptionManager.task.TasksGenerator;
import engine.enigma.Machine.EnigmaMachine;

public class DM {


    // TODO: 9/10/2022 change from agents to tasks
    public enum DifficultyLevel{
        EASY, MEDIUM, HARD, IMPOSSIBLE
    }
    private Agents agents;
    private int AgentsAmount;
    private Dictionary dictionary;
    private int possibleAmountOfCodes;

    private int missionSize;
    private EnigmaMachine machine;
    private int difficulty;
    private TasksGenerator taskCreator;
    public DM(Dictionary dictionary, Agents agents, int agentsAmount){
        this.dictionary = dictionary;
        this.agents = agents;
        this.AgentsAmount = agentsAmount;

    }
    public void DecipherMessage(String messageToDecipher, DifficultyLevel difficulty, int missionSize, UIAdapter uiAdapter){
        this.missionSize = missionSize;
        TasksGenerator tasksCreator =new TasksGenerator(messageToDecipher,missionSize,difficulty,AgentsAmount,machine.clone(), uiAdapter);
        new Thread(tasksCreator,"tasksCreatorThread").start();
    }
    public boolean isDictionaryContainString(String str){
        return dictionary.isDictionaryContainString(str);

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

    public Agents getAgents() {
        return agents;
    }

    public void setAgents(Agents agents) {
        this.agents = agents;
    }

}
