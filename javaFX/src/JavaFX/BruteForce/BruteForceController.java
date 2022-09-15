package JavaFX.BruteForce;
import JavaFX.codeConfiguration.codeConfigurationController;
import JavaFX.mainPage.MainPageController;
import engine.api.ApiEnigma;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.util.List;

public class BruteForceController {

    @FXML
    private Label codeConfigurationLabel;

    @FXML
    private Button resetCodeButton;

    @FXML
    private TextField encryptedMessage;

    @FXML
    private Button ProcessButton;

    @FXML
    private Button clearButton;

    @FXML
    private Label EncryptDecryptResultLabel;

    @FXML
    private TextField SearchField;

    @FXML
    private ListView<String> DictionaryListViewField;

    @FXML
    private Slider SliderAgentsAmount;

    @FXML
    private ComboBox<String> LevelComboBox;

    @FXML
    private Spinner<Integer> SpinnerMissionSize;

    @FXML
    private Label amountOfMissionLabel;

    @FXML
    private Label progressPercentLabel;

    @FXML
    private ProgressBar taskProgressBar;

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button PauseButton;

    @FXML
    private Button ResumeButton;
    @FXML
    private TableView<String> CandidateStringTableView;

    private List<SimpleStringProperty> DictionaryListView;

    private SimpleStringProperty encryptionResultProperty;
    private SimpleStringProperty codeConfiguration;
    private SimpleIntegerProperty maxAmountOfAgents;
    private SimpleBooleanProperty isBruteForceProcess;
    private SimpleBooleanProperty isConfig;
    private SimpleStringProperty encryptionTextFiled;

    private SimpleIntegerProperty amountOfMission;
    private MainPageController mainPageController;
    private ApiEnigma api;

    private int chosenAmountOfAgents;
    private int MissionSize;


    public BruteForceController(){
        this.encryptionResultProperty = new SimpleStringProperty("");
        this.maxAmountOfAgents = new SimpleIntegerProperty();
        this.isBruteForceProcess = new SimpleBooleanProperty();
        this.amountOfMission = new SimpleIntegerProperty();
        this.codeConfiguration = new SimpleStringProperty();
        this.encryptionTextFiled = new SimpleStringProperty("");
        this.isConfig = new SimpleBooleanProperty();
        this.isBruteForceProcess = new SimpleBooleanProperty(false);
    }
    public void setMainPageController(MainPageController mainPageController){
        this.mainPageController = mainPageController;
        this.isConfig.bind(mainPageController.isConfigProperty());
    }
    public void setApi(ApiEnigma api){
        this.api = api;
    }
    @FXML
    private void initialize(){
        EncryptDecryptResultLabel.textProperty().bind(Bindings.format("%s", encryptionResultProperty));
        codeConfigurationLabel.textProperty().bind(Bindings.format("%s",codeConfiguration));
        amountOfMissionLabel.textProperty().bind(Bindings.format("%,d", amountOfMission));

        resetCodeButton.disableProperty().bind(isConfig.not());
        ProcessButton.disableProperty().bind(isConfig.not());
        clearButton.disableProperty().bind(isConfig.not());

        SliderAgentsAmount.disableProperty().bind(isConfig.not());
        LevelComboBox.disableProperty().bind(isConfig.not());
        SpinnerMissionSize.disableProperty().bind(isConfig.not());
        taskProgressBar.disableProperty().bind(isConfig.not());
        DictionaryListViewField.disableProperty().bind(isConfig.not());
        CandidateStringTableView.disableProperty().bind(isConfig.not());
        SearchField.disableProperty().bind(isConfig.not());
        encryptedMessage.disableProperty().bind(isConfig.not());
        DictionaryListViewField.disableProperty().bind(isConfig.not());


        startButton.disableProperty().bind(isConfig.not());
        stopButton.disableProperty().bind(isBruteForceProcess.not());
        PauseButton.disableProperty().bind(isBruteForceProcess.not());
        ResumeButton.disableProperty().bind(isBruteForceProcess.not());

        CandidateStringTableView.setPlaceholder(new Label(""));


    }


    @FXML
    void EncryptFullMessage(ActionEvent event) {
        String toEncrypt = encryptedMessage.getText();
        String cleanedToEncrypt = api.cleanStringFromExcludeChars(toEncrypt);
        if(api.isDictionaryContainString(cleanedToEncrypt))
        {
            String encryptionResult = mainPageController.encryptMessage(cleanedToEncrypt);
            encryptionResultProperty.set(encryptionResult);
        }
        else{
            System.out.println("words not in dic");
        }
    }

    @FXML
    void clearTextAndProcessNewMessage(ActionEvent event) {
        encryptedMessage.setText("");
        encryptionResultProperty.set("");
    }

    @FXML
    void getWordsByPrefix(KeyEvent event) {

    }

    @FXML
     private void resetCodeButton(ActionEvent event) {
        mainPageController.resetPosition();

    }

    public void bindCodeConfiguration(codeConfigurationController controller) {
        codeConfiguration.bind(controller.fullConfigurationProperty());
    }
}




