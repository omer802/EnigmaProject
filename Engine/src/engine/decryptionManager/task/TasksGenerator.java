package engine.decryptionManager.task;

import UIAdapter.UIAdapter;
import engine.decryptionManager.DM;
import engine.decryptionManager.MathematicalCalculations.RotorsPermuter;
import engine.decryptionManager.MathematicalCalculations.CodeGenerator;
import engine.decryptionManager.ThreadFactory.ThreadFactoryBuilder;
import engine.decryptionManager.UpdateCandidateBlockingQueue.UpdateCandidateConsumer;
import engine.enigma.Machine.EnigmaMachine;
import engine.enigma.keyboard.Keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


public class TasksGenerator implements Runnable{
    private int missionSize;

    // TODO: 9/11/2022 change difficulty to enum
    private DM.DifficultyLevel difficulty;
    private String messageToDecode;
    private int possibleAmountOfCodes;
    private EnigmaMachine machine;
    private ThreadPoolExecutor executor;
    private BlockingQueue<Runnable> blockingQueue;
    private int agentsAmount;
    private int positionLength;
    UIAdapter uiAdapter;

    BlockingDeque<String> candidateBlockingQueue;
    private Thread blockingConsumer;


    public TasksGenerator(String messageToDecode, int missionSize, DM.DifficultyLevel difficulty, int agentsAmount,
                          EnigmaMachine machine, UIAdapter uiAdapter){
        this.missionSize = missionSize;
        this.difficulty = difficulty;
        this.agentsAmount = agentsAmount;
        this.machine = machine;
        this.positionLength = machine.getRotorsAmountInUse();
        this.uiAdapter = uiAdapter;
        candidateBlockingQueue = new LinkedBlockingDeque<>();

        setThreadPool(agentsAmount);
        setMessageToDecode(messageToDecode);
    }


    public void setMessageToDecode(String messageToDecode){
        this.messageToDecode = messageToDecode;
    }
    public void setThreadPool(int agentsAmount){
        this.blockingQueue =
                new LinkedBlockingQueue<Runnable>(1000);

        ThreadFactory customThreadFactory = new ThreadFactoryBuilder()
                .setNamePrefix("Agent")
                .setPriority(Thread.MAX_PRIORITY)
                //set Uncaught exception to get the thread how throw UncaughtException
                .setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        System.err.println(String.format(
                                "Thread %s threw exception - %s", t.getName(),
                                e.getMessage()));

                    }
                }).build();
        this.executor =
                new ThreadPoolExecutor(agentsAmount, agentsAmount, 1000, TimeUnit.MINUTES,
                        this.blockingQueue, customThreadFactory);
    }
    @Override
    public void run() {
        CodeGenerator codeGenerator = new CodeGenerator(positionLength);
        executor.prestartAllCoreThreads();
        blockingConsumer  = new Thread(new UpdateCandidateConsumer(candidateBlockingQueue, uiAdapter),
                "Candidate consumer thread");
        blockingConsumer.start();
        try {
            switch (difficulty) {
                case EASY:
                    easyDifficultyLevel(codeGenerator);
                    break;
                case MEDIUM:
                    mediumDifficultyLevel(codeGenerator);
                    break;
                case HARD:
                    hardDifficultyLevel(codeGenerator);
                    break;
                case IMPOSSIBLE:
                    impossible(codeGenerator);
                    break;
            }
            executor.shutdown();
            executor.awaitTermination(15, TimeUnit.MINUTES);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void impossible(CodeGenerator codeGenerator) throws InterruptedException {
        List<String> possibleRotors = machine.getPossibleRotors();
        List<int[]> possibleRotorsIndex = codeGenerator.generateNChooseK(possibleRotors.size(),machine.getRotorsAmountInUse());
        for (int[] indexArray: possibleRotorsIndex) {
            List<String> p = getRotorsByIndexList(indexArray,possibleRotors);
            machine.setChosenRotors(p);
            hardDifficultyLevel(codeGenerator);

        }

    }
    //in this level we dont have the reflector, position, and location of rotors in the slots
    private void hardDifficultyLevel(CodeGenerator codeGenerator) throws InterruptedException {
        List <String> chosenRotors = machine.getChosenRotors();
        RotorsPermuter permuter = new RotorsPermuter(chosenRotors.size());
        int[] indexList = permuter.getNext();
        while (indexList!=null){
            List<String> chosenRotorsNewOrder = getRotorsByIndexList(indexList,chosenRotors);
            System.out.println(chosenRotorsNewOrder);
            machine.setChosenRotors(chosenRotorsNewOrder);
            indexList = permuter.getNext();
            mediumDifficultyLevel(codeGenerator);
        }
    }
    private List<String> getRotorsByIndexList(int[] indexList, List<String> chosenRotors){
        List<String> chosenRotorsToReturn = new ArrayList<>(chosenRotors.size());
        for (int i = 0; i < indexList.length; i++) {
            chosenRotorsToReturn.add(i,chosenRotors.get(indexList[i]));
        }
        return chosenRotorsToReturn;
    }
    //in this level we dont have the reflector and the positions
    private void mediumDifficultyLevel(CodeGenerator codeGenerator) throws InterruptedException {
        List<String> reflectorList = machine.getPossibleReflectors();
        for (String reflector:
             reflectorList) {
            machine.setReflector(reflector);
            easyDifficultyLevel(codeGenerator);
        }
    }

    //in this level we dont have the positions
    private void easyDifficultyLevel(CodeGenerator codeGenerator) throws InterruptedException {
        double numOfTasks = calculateAmountOfTasksEasyLevel();
        int leakageSizeTask = ((int)numOfTasks) % missionSize;

        if(leakageSizeTask>0)
            generateTaskAndPushToBlockingQueue(codeGenerator,leakageSizeTask);

        for (int i = 0; i <numOfTasks ; i++)
            generateTaskAndPushToBlockingQueue(codeGenerator,missionSize);

    }
    private void generateTaskAndPushToBlockingQueue(CodeGenerator codeGenerator, int missionSize) throws InterruptedException {
        List<String> positionsList = codeGenerator.generateNextPositionsListForTask(missionSize);
        Task task = new Task(machine.clone(),positionsList,messageToDecode,candidateBlockingQueue);
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
