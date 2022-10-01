package JavaFX.BruteForce;
import DTOS.decryptionManager.DecryptionManagerDTO;
import JavaFX.codeConfiguration.codeConfigurationController;
import JavaFX.mainPage.MainPageController;
import UIAdapter.UIAdapter;
import engine.api.ApiEnigma;
import engine.decryptionManager.DM;
import engine.decryptionManager.dictionary.Trie;
import engine.decryptionManager.task.TasksManager;
import engine.decryptionManager.task.TimeToCalc;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import UIAdapter.UIAdapterImpJavaFX;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

public class BruteForceController {

    @FXML
    private BorderPane bruteForceBorderPane;
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

    /*@FXML
    private ScrollPane scrollPaneEncryptDecrypt;*/

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
    private TextArea TextAreaCandidates;

    @FXML
    private AnchorPane operationButtons;

    @FXML
    private Label averageTimeLabel;
    @FXML
    private Label ProcessTimeLabel;

    @FXML
    private Button clearCandidateButton;

    private List<SimpleStringProperty> DictionaryListView;

    private SimpleStringProperty encryptionResultProperty;
    private SimpleStringProperty codeConfiguration;
    private SimpleBooleanProperty isBruteForceProcess;
    private SimpleBooleanProperty isConfig;
    private SimpleStringProperty encryptionTextFiled;

    private SimpleDoubleProperty missionAmount;
    private MainPageController mainPageController;
    private ApiEnigma api;

    private Double missionSize;
    private Trie trieAutoComplete;
    private int amountOfAgentsForProcess;
    private String decryptedMessage;
    private UIAdapter uiAdapter;
    private SimpleBooleanProperty afterProcess;
    private SimpleLongProperty processTime;
    private SimpleDoubleProperty averageMissionTime;
    public BruteForceController() {
        this.encryptionResultProperty = new SimpleStringProperty("");
        this.isBruteForceProcess = new SimpleBooleanProperty();
        this.missionAmount = new SimpleDoubleProperty(1);
        this.codeConfiguration = new SimpleStringProperty();
        this.encryptionTextFiled = new SimpleStringProperty("");
        this.isConfig = new SimpleBooleanProperty();
        this.isBruteForceProcess = new SimpleBooleanProperty(false);
        this.afterProcess = new SimpleBooleanProperty(false);
        this.processTime = new SimpleLongProperty();
        this.averageMissionTime = new SimpleDoubleProperty();
        this.SpinnerMissionSize = new Spinner<>();
        // TODO: 9/16/2022 make it property
        missionSize = 1.0;
        amountOfAgentsForProcess = 1;
    }

    public void setMainPageController(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
        this.isConfig.bind(mainPageController.isConfigProperty());
    }

    public void setApi(ApiEnigma api) {
        this.api = api;
    }


    @FXML
    private void initialize() {
        bruteForceBorderPane.disableProperty().bind(isConfig.not());
        setEncryptionBinding();
        setDictionaryBinding();
        setBruteForceConfigurationBinding();
        setBruteForceProcessBinding();
        setListenerBruteForceButtons();
        this.uiAdapter = createUIAdapter();
    }

    public UIAdapterImpJavaFX createUIAdapter(){
        return new UIAdapterImpJavaFX(
                agentCandidatesList -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(agentCandidatesList.getAgentName()+"\n");
                    for (int i = 0; i < agentCandidatesList.getCandidates().size() ; i++) {
                        stringBuilder.append("\t"+agentCandidatesList.getCandidates().get(i)+" ->");
                        stringBuilder.append(agentCandidatesList.getConfigurationList().get(i));
                        stringBuilder.append("\n");
                    }
                    String pattern = "###,###,###";
                    DecimalFormat decimalFormat = new DecimalFormat(pattern);
                    String Format = decimalFormat.format(agentCandidatesList.getDuration());
                    stringBuilder.append("\t"+"Mission duration in nano seconds: "+ Format);
                    stringBuilder.append("\n");
                    TextAreaCandidates.appendText(stringBuilder.toString());

                });
    }


    @FXML
    void EncryptFullMessage(ActionEvent event) {
        String toEncrypt = encryptedMessage.getText();
        String cleanedToEncrypt = api.cleanStringFromExcludeChars(toEncrypt);
        if(api.isDictionaryContainString(cleanedToEncrypt))
        {
            String encryptionResult = mainPageController.encryptMessage(cleanedToEncrypt);
            encryptionResultProperty.set(encryptionResult);
            decryptedMessage = encryptionResult;
            operationButtons.setDisable(false);
        }
        else{
            mainPageController.alertShowException(new RuntimeException("The word you entered is not in the dictionary"));
        }
    }
    @FXML
    void selectLevelListener(ActionEvent event) {
        if(LevelComboBox.getItems().size()>0) {
            DM.DifficultyLevel level = DM.DifficultyLevel.valueOf(LevelComboBox.getSelectionModel().getSelectedItem());
            Integer missionSizeValue = SpinnerMissionSize.getValue();
            double amount = api.calculateAmountOfTasks(missionSizeValue, level);
            missionAmount.set(amount);
        }
    }

    @FXML
    void clearTextAndProcessNewMessage(ActionEvent event) {
        encryptedMessage.setText("");
        encryptionResultProperty.set("");
    }
    @FXML
    void AddWordToEncryptTextField(MouseEvent event) {
        if (event.getClickCount() == 2) {
            //Use ListView's getSelected Item
            String currentItemSelected = DictionaryListViewField.getSelectionModel()
                    .getSelectedItem();
            encryptedMessage.appendText(currentItemSelected+" ");
        }
    }

    @FXML
    void getWordsByPrefix(KeyEvent event) {
        String prefix = SearchField.getText();
        prefix = prefix.toUpperCase();
        List<String> suggestWords = trieAutoComplete.suggest(prefix);
        DictionaryListViewField.getItems().clear();
        for (String word: suggestWords) {
            DictionaryListViewField.getItems().add(word);
        }
    }

    @FXML
     private void resetCodeButton(ActionEvent event) {
        mainPageController.resetPosition();

    }
    @FXML
    void clearCandidate(ActionEvent event) {

        TextAreaCandidates.setText("");
    }

    public void bindCodeConfiguration(codeConfigurationController controller) {
        codeConfiguration.bind(controller.fullConfigurationProperty());
    }
    public void fillDictionary(){
        SearchField.setText("");
        DictionaryListViewField.getItems().clear();
        List<String> allWordsInTrie = trieAutoComplete.suggest("");
        for (String word: allWordsInTrie) {
            DictionaryListViewField.getItems().add(word);
        }
    }
    private void initProcessComponent()
    {

        TextAreaCandidates.setText("");
        encryptedMessage.setText("");
        encryptionResultProperty.set("");

        taskProgressBar.progressProperty().set(0);
        progressPercentLabel.textProperty().set("");

    }
    public void setBruteForceComponent(){
        afterProcess.setValue(false);
        initProcessComponent();
        fetchTrie();
        fillDictionary();
        createAgentSlider();
        createLevelComboBox();
        SpinnerMissionSize.getValueFactory().setValue(1);
        operationButtons.setDisable(true);
    }
    public void createAgentSlider(){
        int maxAgentsAmount = api.getAmountOfAgents();
        SliderAgentsAmount.setMax(maxAgentsAmount);
    }
    public void fetchTrie(){
        trieAutoComplete = api.getTrieFromDictionary();

    }
    private void createLevelComboBox(){
        if(LevelComboBox.getItems().size()>0){
            LevelComboBox.getItems().clear();
        }
        for (int i = 0; i < DM.DifficultyLevel.values().length; i++) {
            LevelComboBox.getItems().add(String.valueOf(DM.DifficultyLevel.values()[i]));
        }
        LevelComboBox.getSelectionModel().select(0);

    }

    // TODO: 9/17/2022 translate all this paremters to deciper message to dto object
    @FXML
    void decipherEncryptedMessage(ActionEvent event) {
        afterProcess.setValue(false);
        TextAreaCandidates.setText("");
        uiAdapter.taskInProcess();
        isBruteForceProcess.set(true);
        DM.DifficultyLevel level = DM.DifficultyLevel.
                valueOf(LevelComboBox.getSelectionModel().getSelectedItem());

        DecryptionManagerDTO decryptionManagerDTO = new DecryptionManagerDTO (encryptionResultProperty.getValue(),level, missionSize,
                uiAdapter,amountOfAgentsForProcess, missionAmount.getValue());

        api.DecipherMessage(
                decryptionManagerDTO,
                () -> {
                   isBruteForceProcess.set(false);

                }
        );
    }
    @FXML
    void stopTaskButtonAction(ActionEvent event) {
        stopTask();
    }
    private void stopTask(){
        api.cancelCurrentTask();
        // TODO: 9/17/2022 fiugre out how to do that
        onTaskFinished(Optional.of( () -> {
                    isBruteForceProcess.set(false);

                }
        ));
    }
    @FXML
    void pauseTaskButtonAction(ActionEvent event) {
        uiAdapter.pauseTask();
        api.pauseCurrentTask();
    }
    @FXML
    void resumeTaskButtonAction(ActionEvent event) {
        synchronized (uiAdapter) {
            uiAdapter.resumeTask();
        }
        api.resumeCurrentTask();
    }


    public void setEncryptionBinding(){
        EncryptDecryptResultLabel.textProperty().bind(Bindings.format("%s", encryptionResultProperty));
        codeConfigurationLabel.textProperty().bind(Bindings.format("%s", codeConfiguration));
    }
    public void setDictionaryBinding(){
       /* DictionaryListViewField.disableProperty().bind(isConfig.not());
        SearchField.disableProperty().bind(isConfig.not());
        encryptedMessage.disableProperty().bind(isConfig.not());
        DictionaryListViewField.disableProperty().bind(isConfig.not());*/
    }
    public void setBruteForceConfigurationBinding(){
        /*SliderAgentsAmount.disableProperty().bind(isConfig.not());
        LevelComboBox.disableProperty().bind(isConfig.not());*/
        amountOfMissionLabel.textProperty().bind(Bindings.format("%,.0f", missionAmount));
        amountOfMissionLabel.visibleProperty().bind(isConfig);
       // amountOfMissionLabel.disableProperty().bind(isConfig.not());
       // SpinnerMissionSize.disableProperty().bind(isConfig.not());
    }
    public void setBruteForceProcessBinding(){
        operationButtons.setDisable(true);
        ProcessButton.disableProperty().bind(Bindings.isEmpty(encryptedMessage.textProperty()));
        startButton.disableProperty().bind(isBruteForceProcess);
        stopButton.disableProperty().bind(isBruteForceProcess.not());
        PauseButton.disableProperty().bind(isBruteForceProcess.not());
        ResumeButton.disableProperty().bind(isBruteForceProcess.not());

        averageTimeLabel.visibleProperty().bind(afterProcess);
        averageTimeLabel.textProperty().bind(Bindings.format("%,.0f", averageMissionTime).concat(" (Nano seconds)"));
        //averageTimeLabel.textProperty().bind(Bindings.concat("(Millis)"));
        ProcessTimeLabel.visibleProperty().bind(afterProcess);
        ProcessTimeLabel.textProperty().bind(Bindings.format("%,d",processTime).concat("(Millis seconds)"));
        //ProcessButton.textProperty().bind(Bindings.format("%,.0f", processTime));

    }
    public void setListenerBruteForceButtons(){
        SliderAgentsAmount.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                amountOfAgentsForProcess = (int) SliderAgentsAmount.getValue();
            }
        });

        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,Integer.MAX_VALUE);
        valueFactory.setValue(1);
        SpinnerMissionSize.setValueFactory(valueFactory);
        String str = new String();
        SpinnerMissionSize.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {

                missionSize = newValue.doubleValue();
                DM.DifficultyLevel level = DM.DifficultyLevel.valueOf(LevelComboBox.getSelectionModel().getSelectedItem());
                double amount = api.calculateAmountOfTasks(newValue,level);
                missionAmount.set(amount);
            }
        });


        EventHandler<KeyEvent> enterKeyEventHandler;

        enterKeyEventHandler = new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {

                // handle users "enter key event"
                if (event.getCode() == KeyCode.ENTER) {

                    try {
                        // yes, using exception for control is a bad solution ;-)
                        Integer.parseInt(SpinnerMissionSize.getEditor().textProperty().get());
                    }
                    catch (NumberFormatException e) {

                        // show message to user: "only numbers allowed"

                        // reset editor to INITAL_VALUE
                        SpinnerMissionSize.getEditor().textProperty().set("1");
                    }
                }
            }

        };
        SpinnerMissionSize.getEditor().addEventHandler(KeyEvent.KEY_PRESSED, enterKeyEventHandler);


    }


    public void bindTaskToUIComponents(TasksManager tasksManager, Runnable onFinish) {
        taskProgressBar.progressProperty().bind(tasksManager.progressProperty());
        progressPercentLabel.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        tasksManager.progressProperty(),
                                        100)),
                        " %"));
        tasksManager.valueProperty().addListener((observable, oldValue, newValue) -> {
            onTaskFinished(Optional.ofNullable(onFinish));
        });
    }
    private void onTaskFinished(Optional<Runnable> onFinish){

        TimeToCalc timeToCalc = api.getTimeToCalc();
        synchronized (timeToCalc) {
            while (timeToCalc.getTotalTimeTaskManager() == 0) {
                try {
                    System.out.println(timeToCalc.getTotalTimeTaskManager()+" time task manger");
                    System.out.println("waiting in brute force control for update in time to calc ");

                    timeToCalc.wait();
                } catch (InterruptedException e) {
                    System.out.println("interupt at wait in finish task");
                }
            }
            afterProcess.set(true);
            System.out.println("stop waiting ");
        }
        averageMissionTime.setValue(timeToCalc.getAverageMissionTime());
        processTime.setValue(timeToCalc.getTotalTimeTaskManager());
        this.progressPercentLabel.textProperty().unbind();
        this.taskProgressBar.progressProperty().unbind();
        onFinish.ifPresent(Runnable::run);
    }
}




