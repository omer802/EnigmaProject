package engine.decryptionManager.task;

import DTOS.decryptionManager.DecryptionManagerDTO;
import UIAdapter.UIAdapter;
import UIAdapter.UIAdapterImpJavaFX;
import engine.decryptionManager.CustomThreadPool.CustomThreadPoolExecutor;
import engine.decryptionManager.DM;
import engine.decryptionManager.MathCalculations.RotorsPermuter;
import engine.decryptionManager.MathCalculations.CodeGenerator;
import engine.decryptionManager.CustomThreadPool.ThreadFactoryBuilder;
import engine.decryptionManager.UpdateCandidateBlockingQueue.UpdateCandidateConsumer;
import engine.enigma.Machine.EnigmaMachine;
import engine.enigma.keyboard.Keyboard;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;


public class TasksManager extends Task<Boolean> {
    private Double missionSize;

    // TODO: 9/11/2022 change difficulty to enum
    private DM.DifficultyLevel difficulty;
    private String messageToDecode;
    private int possibleAmountOfCodes;
    private EnigmaMachine machine;
    private CustomThreadPoolExecutor executor;
    private BlockingQueue<Runnable> blockingQueue;
    private int agentsAmount;
    private int positionLength;
    UIAdapter uiAdapter;
    private double totalMissionAmount;
    private AtomicLong totalMissionAmountToSend;

    public TimeToCalc getTimeToCalc() {
        return timeToCalc;
    }

    TimeToCalc timeToCalc;
    private final int[] missionCount;

    BlockingDeque<AgentCandidatesList> candidateBlockingQueue;
    private Thread blockingConsumer;

    public TasksManager(DecryptionManagerDTO decryptionManagerDTO, EnigmaMachine machine, TimeToCalc timeToCalc) {
        this.missionSize = decryptionManagerDTO.getMissionSize();
        this.difficulty = decryptionManagerDTO.getLevel();
        this.agentsAmount = decryptionManagerDTO.getAmountOfAgentsForProcess();
        this.positionLength = machine.getRotorsAmountInUse();
        this.missionCount = new int[1];
        this.missionCount[0] = 1;
        this.uiAdapter = decryptionManagerDTO.getUiAdapter();
        this.totalMissionAmount = decryptionManagerDTO.getMissionAmount();
        setMessageToDecipher(decryptionManagerDTO.getMessageToDecipher());
        candidateBlockingQueue = new LinkedBlockingDeque<>();
        setThreadPool(agentsAmount);
        this.machine = machine;
        this.timeToCalc = timeToCalc;
        this.totalMissionAmountToSend = new AtomicLong((long) totalMissionAmount);

    }

    @Override
    protected Boolean call() throws Exception {
        //wait(1000 );
        updateProgress(0, totalMissionAmount);
        CodeGenerator codeGenerator = new CodeGenerator(positionLength);
        executor.prestartAllCoreThreads();
        UpdateCandidateConsumer candidateConsumer = new UpdateCandidateConsumer(candidateBlockingQueue, uiAdapter);
        blockingConsumer = new Thread(candidateConsumer, "AgentCandidatesList consumer thread");
        blockingConsumer.start();
        try {
            generateMissionByLevel(difficulty, codeGenerator);

        } catch (InterruptedException e) {
        } finally {
            synchronized (uiAdapter) {
                timeToCalc.updateTotalTasksTime();
                uiAdapter.notifyAll();
            }
            executor.shutdown();
            executor.awaitTermination(15, TimeUnit.MINUTES);


            candidateConsumer.finish();
            synchronized (timeToCalc) {
                timeToCalc.notifyAll();
                return Boolean.TRUE;
            }
        }
    }



    private void generateMissionByLevel(DM.DifficultyLevel difficulty, CodeGenerator codeGenerator) throws InterruptedException {
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
    }


    public void setMessageToDecipher(String messageToDecipher){
        this.messageToDecode = messageToDecipher;
    }
    public void setThreadPool(int agentsAmount){
        this.blockingQueue =
                new LinkedBlockingQueue<Runnable>(1000);

        ThreadFactory customThreadFactory = new ThreadFactoryBuilder()
                .setNamePrefix("Agent")
                .setPriority(Thread.NORM_PRIORITY)
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
                new CustomThreadPoolExecutor(agentsAmount, agentsAmount, 1000, TimeUnit.MINUTES,
                        this.blockingQueue, customThreadFactory);
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
        double numOfTasks = calculateAmountOfCodes()/missionSize;
        double leakageSizeTask = ((int)calculateAmountOfCodes())  % missionSize;
        //System.out.println(numOfTasks);
        //System.out.println(leakageSizeTask);
        if(leakageSizeTask>0) {
            //System.out.println(leakageSizeTask);
            generateTaskAndPushToBlockingQueue(codeGenerator,leakageSizeTask);
            numOfTasks--;
        }


        for (int i = 0; i <numOfTasks ; i++)
            generateTaskAndPushToBlockingQueue(codeGenerator,missionSize);

    }

    private void generateTaskAndPushToBlockingQueue(CodeGenerator codeGenerator, Double missionSize) throws InterruptedException {
        List<String> positionsList = codeGenerator.generateNextPositionsListForTask(missionSize);
        MissionTask task = new MissionTask(machine.clone(),positionsList,messageToDecode,
                candidateBlockingQueue,uiAdapter,totalMissionAmount, timeToCalc,totalMissionAmountToSend,()-> updateProgress(missionCount[0]++, totalMissionAmount)
                );


        pushTaskToBlockingQueue(task);
    }
    private void pushTaskToBlockingQueue(MissionTask task) throws InterruptedException {
        if(uiAdapter.getState() != UIAdapterImpJavaFX.RunningState.STOP) {
           // updateProgress(missionCount[0]++, totalMissionAmount);
            blockingQueue.put(task);
        }
    }
    public void pauseCurrentTask()  {
        while (uiAdapter.getState() == UIAdapterImpJavaFX.RunningState.PAUSE) {
            synchronized (uiAdapter) {
                try {
                    uiAdapter.wait();
                } catch (InterruptedException e) {
                    System.out.println("interupt while pausing");
                }
            }
        }
        }



    public double calculateAmountOfCodes(){
        int alphabetSize = Keyboard.alphabet.length();
        int exponent = machine.getRotorsAmountInUse();
        double amountOfCodes = Math.pow(alphabetSize,exponent);

        return amountOfCodes;
    }


   /* public void pauseCurrentTask() {

          *//*  while (uiAdapter.getState() == UIAdapterImpJavaFX.RunningState.PAUSE) {
                synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    if (uiAdapter.getState() == UIAdapterImpJavaFX.RunningState.STOP) {
                        System.out.println("kill task from interption in pause task");
                        cancel();
                    } else if (uiAdapter.getState() == UIAdapterImpJavaFX.RunningState.IN_PROCESS) {
                        System.out.println(" bring alive process");
                        return;
                    }
                }
            }
        }*//*
    }*/


    public void resumeCurrentTask() {
            synchronized (uiAdapter) {
               // System.out.println("resume");
                uiAdapter.taskInProcess();
                uiAdapter.notifyAll();
            }
        }
}
