package UIAdapter;

import engine.decryptionManager.task.Task;
import javafx.application.Platform;

import java.util.function.Consumer;

public class UIAdapterImpJavaFX implements UIAdapter{
    private Consumer<String> updateCandidateStrings;

    public UIAdapterImpJavaFX(Consumer<String> updateCandidateString){
        this.updateCandidateStrings = updateCandidateString;
    }
    public void AddCandidateStringForDecoding(String Candidate){
        Platform.runLater(
        () -> {
            updateCandidateStrings.accept(Candidate);
        }
        );

    }
}
