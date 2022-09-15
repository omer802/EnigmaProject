package JavaFX.EncryptDecrypt;

import DTOS.ConfigrationsPropertyAdapter.UserConfigurationDTOAdapter;
import JavaFX.codeConfiguration.codeConfigurationController;
import JavaFX.mainPage.MainPageController;
import engine.api.ApiEnigma;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import sun.applet.Main;

public class EncryptDecryptController {
    @FXML
    private ToggleButton automaticModeButton;

    @FXML
    private ToggleButton ManuelModeButton;

    @FXML
    private TextField encryptedMessege;

    @FXML
    private Label EncryptDecryptResultLabel;

    @FXML
    private Label HistoryAndStatistics;
    @FXML
    private Button ProcessButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button doneButton;
    @FXML
    private FlowPane buttonsEncryptionFlowPane;
    @FXML
    private Button resetButton;

    @FXML
    private ScrollPane EncryptDecryptCodeConfiguration;

    @FXML
    private codeConfigurationController EncryptDecryptCodeConfigurationController;
    private MainPageController mainPageController;
    private SimpleBooleanProperty isAutomaticMode;
    private String  ManuelEncryptionString;
    private ApiEnigma api;

    private StringProperty statistics;
    private SimpleIntegerProperty encryptedMessagesAmount;
    private SimpleBooleanProperty haveCodeConfiguration;


    private long startingEncryptionTime;

    public EncryptDecryptController(){
        this.isAutomaticMode = new SimpleBooleanProperty(true);
        this.ManuelEncryptionString = new String();
        this.statistics = new SimpleStringProperty();
        this.encryptedMessagesAmount = new SimpleIntegerProperty();
        this.haveCodeConfiguration = new SimpleBooleanProperty();

    }
    @FXML
    private void initialize(){

       makeToggleGroupAlwaysSelect();
        isAutomaticMode.bind(automaticModeButton.selectedProperty());
        ProcessButton.disableProperty().bind(isAutomaticMode.not());

        clearButton.disableProperty().bind(isAutomaticMode.not());
        doneButton.disableProperty().bind(isAutomaticMode);
        HistoryAndStatistics.textProperty().bind(statistics);
        automaticModeButton.disableProperty().bind(haveCodeConfiguration.not());
        ManuelModeButton.disableProperty().bind(haveCodeConfiguration.not());
        buttonsEncryptionFlowPane.disableProperty().bind(haveCodeConfiguration.not());
        encryptedMessege.disableProperty().bind(haveCodeConfiguration.not());

    }

    public void makeToggleGroupAlwaysSelect(){
        ToggleGroup toggleGroup = automaticModeButton.getToggleGroup();
        toggleGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });
    }
    public void setCodeConfigurationController(codeConfigurationController codeConfiguration){
        this.EncryptDecryptCodeConfigurationController.BindCodeConfiguration(codeConfiguration);
    }
    public void setMainPageController(MainPageController mainPageController){
        this.mainPageController = mainPageController;
        this.haveCodeConfiguration.bind(mainPageController.isConfigProperty());
    }

    public void setApi(ApiEnigma api) {
        this.api = api;
    }
    @FXML
    public void ApplyAutomaticMode(ActionEvent event) {
        //isAutomaticMode.set(true);
    }

    @FXML
    public void ApplyManuelMode(ActionEvent event) {
        //isAutomaticMode.set(false);
    }

    @FXML
    public void EncrypteFullMessage(ActionEvent event) {
        String toEncrypt = encryptedMessege.getText();
        encryptFullMessageNoneAction(toEncrypt);
        }

    public String encryptFullMessageNoneAction(String toEncrypt) {
        toEncrypt = toEncrypt.toUpperCase();
        boolean isStringValid = api.validateStringToEncrypt(toEncrypt);
        if (!isStringValid)
            System.out.println("Some of the letters you entered are not from the alphabet");
        else {
            if (toEncrypt.length() > 0) {
                String encryptedString = api.dataEncryption(toEncrypt);
                EncryptDecryptResultLabel.setText(encryptedString);
                return encryptedString;
            }
        }
        return null;

    }

    @FXML
    void clearTextAndProcessNewMessage(ActionEvent event) {
        encryptedMessege.setText("");
        EncryptDecryptResultLabel.setText("");
    }

    @FXML
    void resetPositions(ActionEvent event) {
        mainPageController.resetPosition();
        initiateEncryptionTextField();

    }
    public void setStatistics(){
        api.setStatisticsProperty(statistics);
    }

    @FXML
    void EncryptCharacter(KeyEvent event) {
        if(encryptedMessege.getText().length()==0)
            startingEncryptionTime = System.nanoTime();
        if (!isAutomaticMode.getValue()) {
            String toEncrypt = event.getCharacter();
            toEncrypt = toEncrypt.toUpperCase();
            boolean isCharacterValid = api.validateStringToEncrypt(toEncrypt);
            if (!isCharacterValid)
                System.out.println("Not valid arugemnt");
            else {
                ManuelEncryptionString += api.encryptChar(toEncrypt.charAt(0));
                EncryptDecryptResultLabel.setText(ManuelEncryptionString);
                IncrementAmountOfMessageDecrypted();
            }
        }
    }



    @FXML
    private void updateStatisticsAndClearText(ActionEvent event) {
        long timeToEncrypt =  System.nanoTime() - startingEncryptionTime ;
        api.updateStatistics(encryptedMessege.getText(),ManuelEncryptionString,timeToEncrypt);
        initiateEncryptionTextField();
        IncrementAmountOfMessageDecrypted();
    }
    private void IncrementAmountOfMessageDecrypted(){
        mainPageController.IncrementAmountOfMessageDecrypted();
    }
    private void updateNumOfEncryptedMessage(){

    }

    private void initiateEncryptionTextField(){
        ManuelEncryptionString = new String();
        EncryptDecryptResultLabel.setText("");
        encryptedMessege.setText("");
    }

}