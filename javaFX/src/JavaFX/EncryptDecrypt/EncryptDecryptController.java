package JavaFX.EncryptDecrypt;

import DTOS.ConfigrationsPropertyAdapter.UserConfigurationDTOAdapter;
import JavaFX.codeConfiguration.codeConfigurationController;
import engine.api.ApiEnigma;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

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
    private Button resetButton;

    @FXML
    private ScrollPane EncryptDecryptCodeConfiguration;

    @FXML
    private codeConfigurationController EncryptDecryptCodeConfigurationController;

    private SimpleBooleanProperty isAutomaticMode;
    private String  ManuelEncryptionString;
    private ApiEnigma api;

    private StringProperty statistics;

    private long startingEncryptionTime;

    public EncryptDecryptController(){
        isAutomaticMode = new SimpleBooleanProperty(true);
        ManuelEncryptionString = new String();
        statistics = new SimpleStringProperty();

    }
    @FXML
    private void initialize(){

       makeToggleGroupAlwaysSelect();
        isAutomaticMode.bind(automaticModeButton.selectedProperty());
        ProcessButton.disableProperty().bind(isAutomaticMode.not());
        clearButton.disableProperty().bind(isAutomaticMode.not());
        doneButton.disableProperty().bind(isAutomaticMode);
        HistoryAndStatistics.textProperty().bind(statistics);
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
    public void setApi(ApiEnigma api) {
        this.api = api;
    }
    @FXML
    void ApplyAutomaticMode(ActionEvent event) {
        //isAutomaticMode.set(true);
    }

    @FXML
    void ApplyManuelMode(ActionEvent event) {
        //isAutomaticMode.set(false);
    }

    @FXML
    void EncrypteFullMessage(ActionEvent event) {

        String toEncrypt = encryptedMessege.getText();
        toEncrypt = toEncrypt.toUpperCase();
        boolean isStringValid = api.validateStringToEncrypt(toEncrypt);
        if (!isStringValid)
            System.out.println("Some of the letters you entered are not from the alphabet");
        else{
            if(toEncrypt.length()>0) {
                String encryptedString = api.dataEncryption(toEncrypt);
                EncryptDecryptResultLabel.setText(encryptedString);
            }
        }

    }

    @FXML
    void clearTextAndProcessNewMessage(ActionEvent event) {
        encryptedMessege.setText("");
        EncryptDecryptResultLabel.setText("");
    }

    @FXML
    void resetPositions(ActionEvent event) {
        api.resetPositions();
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

            }
        }
    }



    @FXML
    private void updateStatisticsAndClearText(ActionEvent event) {
        long timeToEncrypt =  System.nanoTime()- startingEncryptionTime ;
        api.updateStatistics(encryptedMessege.getText(),ManuelEncryptionString,timeToEncrypt);
        initiateEncryptionTextField();
    }

    private void initiateEncryptionTextField(){
        ManuelEncryptionString = new String();
        EncryptDecryptResultLabel.setText("");
        encryptedMessege.setText("");
    }

}