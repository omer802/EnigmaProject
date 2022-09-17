package JavaFX.BruteForce;
import DTOS.decryptionManager.DecryptionManagerDTO;
import JavaFX.codeConfiguration.codeConfigurationController;
import JavaFX.mainPage.MainPageController;
import UIAdapter.UIAdapter;
import engine.api.ApiEnigma;
import engine.decryptionManager.DM;
import engine.decryptionManager.dictionary.Trie;
import engine.decryptionManager.task.TasksManager;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import UIAdapter.UIAdapterImpJavaFX;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


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
    /*@FXML
    private TableView<AgentCandidatesList> CandidateStringTableView;

    @FXML
    private TableColumn<AgentCandidatesList, String> agentColumn;

    @FXML
    private TableColumn<AgentCandidatesList, List<String>> candidateStringColumn;

    @FXML
    private TableColumn<AgentCandidatesList, Long> MissionTimeColumn;

    @FXML
    private TableColumn<AgentCandidatesList, List<String>> codeColumn;*/
    @FXML
    private TextArea TextAreaCandidates;

    @FXML
    private AnchorPane operationButtons;

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

    private int missionSize;
    private Trie trieAutoComplete;
    private int amountOfAgentsForProcess;
    private String decryptedMessage;
    private UIAdapter uiAdapter;

    public BruteForceController() {
        this.encryptionResultProperty = new SimpleStringProperty("");
        this.isBruteForceProcess = new SimpleBooleanProperty();
        this.missionAmount = new SimpleDoubleProperty();
        this.codeConfiguration = new SimpleStringProperty();
        this.encryptionTextFiled = new SimpleStringProperty("");
        this.isConfig = new SimpleBooleanProperty();
        this.isBruteForceProcess = new SimpleBooleanProperty(false);
        // TODO: 9/16/2022 make it property
        missionSize = 1;
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
                agentCandidatesList ->{
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(agentCandidatesList.getAgentName()+"\n");
                    System.out.println(agentCandidatesList.getCandidates());
                    System.out.println("sizeee is !!!**********"+agentCandidatesList.getCandidates().size());
                    for (int i = 0; i < agentCandidatesList.getCandidates().size() ; i++) {
                        stringBuilder.append("\t"+agentCandidatesList.getCandidates().get(i)+" ->");
                        stringBuilder.append(agentCandidatesList.getConfigurationList().get(i));
                        stringBuilder.append("\n");
                    }
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
            System.out.println("words not in dic");
        }
    }
    @FXML
    void selectLevelListener(ActionEvent event) {
        if(LevelComboBox.getItems().size()>0) {
            DM.DifficultyLevel level = DM.DifficultyLevel.valueOf(LevelComboBox.getSelectionModel().getSelectedItem());
            int missionSizeValue = SpinnerMissionSize.getValue();
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
            encryptedMessage.appendText(" "+ currentItemSelected);
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
        if(uiAdapter.getState()== UIAdapterImpJavaFX.RunningState.IN_PROCESS)
        {
            stopTask();
        }
        TextAreaCandidates.setText("");
        encryptedMessage.setText("");
        encryptionResultProperty.set("");

        taskProgressBar.progressProperty().set(0);
        progressPercentLabel.textProperty().set("");

    }
    public void setBruteForceComponent(){

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
        uiAdapter.resumeTask();
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
        onTaskFinished(Optional.ofNullable( () -> {
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
        uiAdapter.resumeTask();
        api.resumeCurrentTask();
    }


    public void setEncryptionBinding(){
        EncryptDecryptResultLabel.textProperty().bind(Bindings.format("%s", encryptionResultProperty));
        codeConfigurationLabel.textProperty().bind(Bindings.format("%s", codeConfiguration));
        /*resetCodeButton.disableProperty().bind(isConfig.not());
        ProcessButton.disableProperty().bind(isConfig.not());
        clearButton.disableProperty().bind(isConfig.not());*/
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
       /* taskProgressBar.disableProperty().bind(isConfig.not());
        TextAreaCandidates.disableProperty().bind(isConfig.not());
        clearCandidateButton.disableProperty().bind(isConfig.not());*/
        operationButtons.setDisable(true);
        ProcessButton.disableProperty().bind(Bindings.isEmpty(encryptedMessage.textProperty()));
        startButton.disableProperty().bind(isBruteForceProcess);
        stopButton.disableProperty().bind(isBruteForceProcess.not());
        PauseButton.disableProperty().bind(isBruteForceProcess.not());
        ResumeButton.disableProperty().bind(isBruteForceProcess.not());
    }
    public void setListenerBruteForceButtons(){
        SliderAgentsAmount.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                amountOfAgentsForProcess = (int) SliderAgentsAmount.getValue();
            }
        });

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,1000);
        valueFactory.setValue(1);
        SpinnerMissionSize.setValueFactory(valueFactory);
        SpinnerMissionSize.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                missionSize = newValue;
                DM.DifficultyLevel level = DM.DifficultyLevel.valueOf(LevelComboBox.getSelectionModel().getSelectedItem());
                double amount = api.calculateAmountOfTasks(newValue,level);
                missionAmount.set(amount);
            }
        });
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
        uiAdapter.stopTask();
        this.progressPercentLabel.textProperty().unbind();
        this.taskProgressBar.progressProperty().unbind();
        onFinish.ifPresent(Runnable::run);
    }
}




