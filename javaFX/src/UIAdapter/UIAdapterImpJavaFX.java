package UIAdapter;

import engine.decryptionManager.task.AgentCandidatesList;
import engine.decryptionManager.task.Task;
import javafx.application.Platform;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;


public class UIAdapterImpJavaFX implements UIAdapter{
    public static enum RunningState{
        STOP, PAUSE, RESUME, IN_PROCESS
    }
    private Consumer<AgentCandidatesList> updateCandidateStrings;

    public UIAdapterImpJavaFX(Consumer<AgentCandidatesList> updateCandidateString){
        this.updateCandidateStrings = updateCandidateString;
    }
    public void AddCandidateStringForDecoding(AgentCandidatesList Candidate){
        Platform.runLater(
        () -> {// TODO: 9/16/2022 change this
            System.out.println("update in uiAdapter"+ Candidate.getCandidates());
            updateCandidateStrings.accept(Candidate);
        }
        );

    }
}
