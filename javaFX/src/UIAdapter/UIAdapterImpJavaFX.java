package UIAdapter;

import engine.decryptionManager.task.AgentCandidatesList;
import javafx.application.Platform;

import java.util.function.Consumer;

import static UIAdapter.UIAdapterImpJavaFX.RunningState.*;


public class UIAdapterImpJavaFX implements UIAdapter{
    public enum RunningState{
        STOP, PAUSE, IN_PROCESS, TO_RESUME
    }
    RunningState runningState;
    private Consumer<AgentCandidatesList> updateCandidateStrings;

    public UIAdapterImpJavaFX(Consumer<AgentCandidatesList> updateCandidateString){
        this.updateCandidateStrings = updateCandidateString;
        runningState = STOP;
    }
    public void AddCandidateStringForDecoding(AgentCandidatesList Candidate){
        Platform.runLater(
        () -> {// TODO: 9/16/2022 change this
            //System.out.println("update in uiAdapter"+ Candidate.getCandidates());
            updateCandidateStrings.accept(Candidate);
        }
        );

    }

    public RunningState getState(){
        return runningState;
    }
    public void stopTask(){
        runningState = STOP;
    }
    public void resumeTask(){
        runningState = TO_RESUME;
    }
    public void pauseTask(){
        runningState = PAUSE;
    }
    public void taskInProcess(){
        runningState = IN_PROCESS;
    }
}
