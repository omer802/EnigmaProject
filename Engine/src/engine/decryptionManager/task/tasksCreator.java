package engine.decryptionManager.task;

import engine.enigma.Machine.EnigmaMachine;
import engine.enigma.keyboard.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class tasksCreator implements Runnable{
    private int missionSize;

    // TODO: 9/11/2022 change difficulty to enum
    private int difficulty;
    private String messageToDecode;
    private int possibleAmountOfCodes;
    private EnigmaMachine machine;
    private ThreadPoolExecutor executor;
    private BlockingQueue<Runnable> blockingQueue;
    private List<Task> taskList;
    private int agentsAmount;
    private int positionLength;


    public tasksCreator(String messageToDecode,int missionSize, int difficulty, int agentsAmount,
                        EnigmaMachine machine){
        this.missionSize = missionSize;
        this.difficulty = difficulty;
        this.agentsAmount = agentsAmount;
        this.machine = machine;
        this.positionLength = machine.getRotorsAmountInUse();
        taskList = new ArrayList<>();
        setThreadPool(agentsAmount);
        setMessageToDecode(messageToDecode);
    }


    public void setMessageToDecode(String messageToDecode){
        this.messageToDecode = messageToDecode;
    }
    public void setThreadPool(int agentsAmount){
        this.blockingQueue =
                new LinkedBlockingQueue<Runnable>(1000);
        this.executor =
                new ThreadPoolExecutor(agentsAmount, agentsAmount, 1000, TimeUnit.MINUTES,
                        this.blockingQueue);
    }
    @Override
    public void run() {
        System.out.println(Thread.currentThread());
        CodeConfigurationGenerator codeGenerator = new CodeConfigurationGenerator(positionLength);
        executor.prestartAllCoreThreads();
        try {
            switch (difficulty) {
                case 0:
                        easyDifficultyLevel(codeGenerator);
                        break;
                case 1:
                    mediumDifficultyLevel(codeGenerator);


            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //in this level we dont know the reflector and the positions
    private void mediumDifficultyLevel(CodeConfigurationGenerator codeGenerator) throws InterruptedException {
        List<String> reflectorList = machine.getPossibleReflectors();
        for (String reflector:
             reflectorList) {
            machine.setReflector(reflector);
            easyDifficultyLevel(codeGenerator);
        }
    }

    //in this level we dont know the positions
    private void easyDifficultyLevel(CodeConfigurationGenerator codeGenerator) throws InterruptedException {
        double numOfTasks = calculateAmountOfTasksEasyLevel();
        int leakageSizeTask = ((int)numOfTasks) % missionSize;
        List<String> positions;

        if(leakageSizeTask>0)
            generateTaskAndPushToBlockingQueue(codeGenerator,leakageSizeTask);

        for (int i = 0; i <numOfTasks ; i++)
            generateTaskAndPushToBlockingQueue(codeGenerator,missionSize);

    }
    private void generateTaskAndPushToBlockingQueue(CodeConfigurationGenerator codeGenerator, int missionSize) throws InterruptedException {
        List<String> positionsList = codeGenerator.generateNextPositionsListForTask(missionSize);
        Task task = new Task(machine.clone(),positionsList,messageToDecode);
        //System.out.println("push to blocking queqe "+ task.positions.toString());
        pushTaskToBlockingQueue(task);
    }
    private void pushTaskToBlockingQueue(Task task) throws InterruptedException {
        blockingQueue.put(task);

    }

    public double calculateAmountOfTasksEasyLevel(){
        int amountOfTasks = Keyboard.alphabet.length();
        int exponent = machine.getRotorsAmountInUse();
        double numOfTask = Math.pow(amountOfTasks,exponent);
        return numOfTask/missionSize;
    }
}
