package JavaFX.codeConfiguration;
import DTOS.ConfigrationsPropertyAdapter.UserConfigurationDTOAdapter;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class codeConfigurationController {

    @FXML
    private Label chosenReflectorLabel;

    @FXML
    private Label plugBoardConnectionsLabel;

    @FXML
    private Label positionsAndNotchLabel;

    @FXML
    private Label chosenRotorsLabel;

    @FXML
    private Label fullConfigurationLabel;


    private SimpleStringProperty chosenRotors;
    private SimpleStringProperty NotchAndLetterAtPeekPaneStartingPosition;
    private SimpleStringProperty chosenReflector;
    private SimpleStringProperty plugBoardToShow;


    public codeConfigurationController() {
        chosenRotors = new SimpleStringProperty();
        NotchAndLetterAtPeekPaneStartingPosition = new SimpleStringProperty();
        chosenReflector = new SimpleStringProperty();
        plugBoardToShow = new SimpleStringProperty();
    }

    @FXML
    private void initialize(){
        chosenRotorsLabel.textProperty().bind(Bindings.format("%s", chosenRotors));
        positionsAndNotchLabel.textProperty().bind(Bindings.format("%s", NotchAndLetterAtPeekPaneStartingPosition));
        chosenReflectorLabel.textProperty().bind(Bindings.format("%s",chosenReflector));
        plugBoardConnectionsLabel.textProperty().bind(Bindings.format("%s",plugBoardToShow));
    }
    public UserConfigurationDTOAdapter getConfigurationProperties(){
        return new UserConfigurationDTOAdapter(chosenRotors, NotchAndLetterAtPeekPaneStartingPosition,
                chosenReflector, plugBoardToShow);
    }
}



