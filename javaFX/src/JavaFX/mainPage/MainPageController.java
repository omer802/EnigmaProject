package JavaFX.mainPage;

import DTOS.ConfigrationsPropertyAdapter.FileConfigurationDTOAdapter;
import DTOS.ConfigrationsPropertyAdapter.UserConfigurationDTOAdapter;
import DTOS.Configuration.UserConfigurationDTO;
import DTOS.Validators.xmlFileValidatorDTO;
import JavaFX.UIComponent.PlugBoardUI;
import JavaFX.codeConfiguration.codeConfigurationController;
import engine.api.ApiEnigma;
import engine.enigma.keyboard.Keyboard;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class MainPageController {

    @FXML
    private TextField URLFileText;

    @FXML
    private HBox HboxRtorosID;

    @FXML
    private HBox HBoxlistOfRotorsButtons;

    @FXML
    private HBox HBoxListOfPositions;

    @FXML
    private HBox HBoxReflectorChoice;
    @FXML
    private CheckBox CheckBoxIsPluged;

    private Stage PrimaryStage;
    @FXML
    private Button setCodeButton;
    @FXML
    private FlowPane machineDetailsFlowPane;
    @FXML
    private FlowPane FlowPaneAlphabet;
    @FXML
    private Label InUseRotorsAmountLabel;

    @FXML
    private Label ReflectorsAmountLabel;

    @FXML
    private Label encryptedMessagesAmountLabel;

    @FXML
    private Label rotorsAmountLabel;
    @FXML
    private AnchorPane CodeAtMachineDetails;
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
    @FXML
    private Button randomCodeButton;

    @FXML
    private ScrollPane codeConfiguration;
    // TODO: 9/3/2022 change codeconfiguration to upercase 
    @FXML
    private codeConfigurationController codeConfigurationController;
    private SimpleStringProperty chosenRotors;
    private SimpleStringProperty NotchAndLetterAtPeekPaneStartingPosition;
    private SimpleStringProperty chosenReflector;
    private SimpleStringProperty plugBoardToShow;

    private ApiEnigma api;
    private String CurrentColor;
    // TODO: 9/1/2022 change the property to choicebox (maybe)!
    private List<ObjectProperty<String>> chosenRotorsList;
    private List<ObjectProperty<Character>> chosenPositions;
    private ObjectProperty<String> ReflectorOptions;
    private SimpleIntegerProperty rotorsAmount;
    private SimpleIntegerProperty rotorsInUseAmount;
    private SimpleIntegerProperty reflectorsAmount;
    private SimpleIntegerProperty encryptedMessagesAmount;

    private SimpleBooleanProperty haveCodeConfiguration;
    private SimpleBooleanProperty isFileSelected;

    private FileConfigurationDTOAdapter fileConfigurationDTOAdapter;
    public MainPageController(){
        this.chosenRotorsList = new ArrayList<>();
        this.chosenPositions = new ArrayList<>();
           this.rotorsAmount = new SimpleIntegerProperty(0);
       this.rotorsInUseAmount = new SimpleIntegerProperty(0);
       this.reflectorsAmount = new SimpleIntegerProperty(0);
        this.encryptedMessagesAmount = new SimpleIntegerProperty(0);
        this.haveCodeConfiguration = new SimpleBooleanProperty(false);
        this.isFileSelected = new SimpleBooleanProperty(false);
        this.chosenRotors = new SimpleStringProperty();
        this.NotchAndLetterAtPeekPaneStartingPosition = new SimpleStringProperty();
        this.chosenReflector = new SimpleStringProperty();
        this.plugBoardToShow = new SimpleStringProperty();
    }
    @FXML
    private void initialize(){
        rotorsAmountLabel.textProperty().bind(Bindings.format("%,d", rotorsAmount));
        InUseRotorsAmountLabel.textProperty().bind(Bindings.format("%,d", rotorsInUseAmount));
        ReflectorsAmountLabel.textProperty().bind(Bindings.format("%,d",reflectorsAmount));
        encryptedMessagesAmountLabel.textProperty().bind(Bindings.format("%,d",encryptedMessagesAmount));
        fileConfigurationDTOAdapter = new FileConfigurationDTOAdapter(rotorsAmount,rotorsInUseAmount ,reflectorsAmount,
                encryptedMessagesAmount);

        setCodeButton.disableProperty().bind(isFileSelected.not());
        machineDetailsFlowPane.disableProperty().bind(isFileSelected.not());
        CheckBoxIsPluged.disableProperty().bind(isFileSelected.not());
        //RandomCodeButton.disableProperty().bind(isFileSelected.not());
        //initiate property starting configuration
       //chosenRotorsLabel.textProperty().bind(Bindings.format("%s",chosenRotors));
       //positionsAndNotchLabel.textProperty().bind(Bindings.format("%s",NotchAndLetterAtPeekPaneStartingPosition));
       //chosenRotorsLabel.textProperty().bind(Bindings.format("%s",chosenReflector));
       //plugBoardConnectionsLabel.textProperty().bind(Bindings.format("%s",plugBoardToShow));
    }
    @FXML
    public void loadXmlFile(ActionEvent event) {
        // TODO: 9/1/2022 add alert window
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select machine file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(PrimaryStage);
        if (selectedFile == null)
            return;

        api.setDTOConfigurationAdapter(fileConfigurationDTOAdapter);
        String path = selectedFile.getAbsolutePath();
        xmlFileValidatorDTO validator = api.readDataJavaFx(path);
        if (validator.getListOfExceptions().size() > 0) {
            System.out.println("The following errors occurred while reading the file:\n");
            for (Exception e : validator.getListOfExceptions()) {
                System.out.println(e.getMessage());
            }
        } else {
            initOriginalConfiguration();
            setTextURL(path);
            createCodeMenu(api.getAmountOfRotors());
            isFileSelected.set(true);
            haveCodeConfiguration.set(false);
            CheckBoxIsPluged.setSelected(false);

        }
    }
    public void initOriginalConfiguration(){
        chosenReflectorLabel.setText("");
        plugBoardConnectionsLabel.setText("");
        positionsAndNotchLabel.setText("");
        chosenRotorsLabel.setText("");
        fullConfigurationLabel.setText("");
    }

    public void setPrimaryStage(Stage primaryStage) {
        PrimaryStage = primaryStage;
    }

    public void setApi(ApiEnigma api) {
        this.api = api;
    }

    // TODO: 9/2/2022 bind the setcode to if the plugboard is even 
    public void createCodeMenu(int countOfRotors) {
        makeEmptyLayouts();
        createLabels(countOfRotors);
        createRotorsButtons(countOfRotors);
        createPositionsButtons(countOfRotors);
        createReflectorChoiceBox();
    }
    public void makeEmptyLayouts(){
        if(codeConfigurationController.isConfig())
        codeConfigurationController.makeEmptyLayout();
        this.HboxRtorosID.getChildren().clear();
        this.HBoxlistOfRotorsButtons.getChildren().clear();
        this.HBoxListOfPositions.getChildren().clear();
        this.HBoxReflectorChoice.getChildren().clear();
        this.FlowPaneAlphabet.getChildren().clear();
        //codeConfigurationController.makeEmptyLayout();
    }

    // TODO: 9/2/2022 make it a property in initlize


    // TODO: 9/2/2022 think about make it property in intilize using dumy labels and rotor
    public void createLabels(int countOfRotors) {
        Label firstLabelRotor = new Label();
        firstLabelRotor.setText("Rotor " + countOfRotors);
        firstLabelRotor.setPadding(new Insets(0, 0, 0, 105));
        firstLabelRotor.setMinWidth(Region.USE_PREF_SIZE);
        HboxRtorosID.getChildren().add(firstLabelRotor);
        for (int i = countOfRotors - 1; i > 0; i--) {
            Label rotorID = new Label();
            rotorID.setText("Rotor " + i);
            rotorID.setMinWidth(Region.USE_PREF_SIZE);
            rotorID.setPrefWidth(46);
            HboxRtorosID.getChildren().add(rotorID);
        }
    }

    public void createRotorsButtons(int countOfRotors) {
        Label labelNumber = new Label();
        labelNumber.setText("Set Rotors");
        labelNumber.setPrefWidth(85);
        labelNumber.setMinWidth(Region.USE_PREF_SIZE);
        HBoxlistOfRotorsButtons.getChildren().add(labelNumber);
        List<String> possibleRotors = api.getPossibleRotors();
        addDynamicRotorsChoiceBoxesToHBox(HBoxlistOfRotorsButtons, countOfRotors, possibleRotors);
    }

    public void createPositionsButtons(int countOfRotors) {
        Label labelRings = new Label();
        labelRings.setText("Set Position");
        labelRings.setPrefWidth(85);
        labelRings.setPadding(new Insets(0, 6, 0, 0));
        labelRings.setMinWidth(Region.USE_PREF_SIZE);
        HBoxListOfPositions.getChildren().add(labelRings);
        List<Character> alphabet = adapterStringToCharList(Keyboard.alphabet);
        addDynamicPositionsChoiceBoxesToHBox(HBoxListOfPositions, countOfRotors, alphabet);
    }

    public static List<Character> adapterStringToCharList(String str) {
        List<Character> chars = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            chars.add(str.charAt(i));
        }
        return chars;
    }


    public void addDynamicRotorsChoiceBoxesToHBox(HBox addToHBox, int countOfRotors, List<String> addToList) {
        chosenRotorsList = new ArrayList<>();
        for (int i = 1; i <= countOfRotors; i++) {
            ChoiceBox<String> choiceBox = new ChoiceBox();
            choiceBox.setPrefHeight(25);
            choiceBox.setPrefWidth(61);
            choiceBox.setMinWidth(Region.USE_PREF_SIZE);
            choiceBox.getItems().addAll(addToList);
            choiceBox.getSelectionModel().selectFirst();
            ObjectProperty<String> chosenRotorProperty = choiceBox.valueProperty();
            chosenRotorsList.add(chosenRotorProperty);
            addToHBox.getChildren().add(choiceBox);
        }
    }

    // TODO: 9/1/2022 make generic type for this and for rotors!
    public void addDynamicPositionsChoiceBoxesToHBox(HBox addToHBox, int countOfRotors, List<Character> addToList) {
        chosenPositions = new ArrayList<>();
        for (int i = 1; i <= countOfRotors; i++) {
            ChoiceBox choiceBox = getChoiceBox(addToList);
            // TODO: 9/1/2022 chenge to choicebox
            ObjectProperty<Character> chosenRotorProperty = choiceBox.valueProperty();
            chosenPositions.add(chosenRotorProperty);
            addToHBox.getChildren().add(choiceBox);
        }
    }

    // TODO: 9/1/2022 make this also generic type
    public ChoiceBox<Character> getChoiceBox(List<Character> addToList) {
        ChoiceBox<Character> choiceBox = new ChoiceBox();
        choiceBox.setPrefHeight(25);
        choiceBox.setPrefWidth(61);
        choiceBox.setMinWidth(Region.USE_PREF_SIZE);
        choiceBox.getItems().addAll(addToList);
        choiceBox.getSelectionModel().selectFirst();
        return choiceBox;
    }

    // TODO: 9/1/2022 make add dynamic to generic
   /* public static <T> void addDynamicChoiceBoxListToHBox(HBox addToHBox, int countOfRotors, Collection<T> addToList) {
        for (int i = 1; i <= countOfRotors; i++) {
            ChoiceBox<T> choiceBox = new ChoiceBox();
            choiceBox.setPrefHeight(25);
            choiceBox.setPrefWidth(61);
            choiceBox.setMinWidth(Region.USE_PREF_SIZE);
            choiceBox.getItems().addAll(addToList);
            choiceBox.getSelectionModel().selectFirst();
            addToHBox.getChildren().add(choiceBox);
        }

    }*/

    public void createReflectorChoiceBox() {
        Label ReflectorLabel = new Label("Set Reflector");
        HBoxReflectorChoice.getChildren().add(ReflectorLabel);
        ChoiceBox choiceBox = new ChoiceBox();
        choiceBox.setPrefHeight(25);
        choiceBox.setPrefWidth(74);
        choiceBox.getItems().addAll(api.getPossibleReflectors());
        choiceBox.setValue(api.getPossibleReflectors().get(0));
        this.ReflectorOptions = choiceBox.valueProperty();
        HBoxReflectorChoice.getChildren().add(choiceBox);
    }

    public void setTextURL(String path) {
        URLFileText.setText(path);
    }

    @FXML
    void setCodeListener(ActionEvent event) {

        UserConfigurationDTO Specification = getUserConfigurationDTO();
        if(Specification == null)
            return;
        if(CheckBoxIsPluged.isSelected()) {// TODO: 9/1/2022 make it a property list
            //check if plugBoard initial succeed
            if(!setPlugBoard(Specification))
                return;
        }

        api.selectInitialCodeConfiguration(Specification);
        updateConfiguration();
    }
    @FXML
    public void generateRandomCode(ActionEvent event){
        UserConfigurationDTO Specification = api.AutomaticallyInitialCodeConfiguration();
        updateConfiguration();
    }
    public void updateConfiguration(){
        setOriginalConfiguration();
        setCurrentConfiguration();
        haveCodeConfiguration.set(true);
    }

    // TODO: 9/3/2022 merge current and original configuration to one function that effect each other
    public void setCurrentConfiguration(){
        UserConfigurationDTOAdapter currentConfig = codeConfigurationController.getConfigurationProperties();
        api.setCurrentConfigurationProperties(currentConfig);
        codeConfigurationController.setConfig(true);
    }
    public void setOriginalConfiguration(){
        UserConfigurationDTO originalConfigurationDTO = api.getOriginalConfiguration();
        StringBuilder originalConfiguration = api.getStringDataReceiveFromUser(originalConfigurationDTO);
        String[] build = originalConfiguration.toString().replace(">","")
                .split("<");
        System.out.println(build);
        chosenRotorsLabel.setText(build[1]);

        positionsAndNotchLabel.setText(build[2]);

        chosenReflectorLabel.setText(build[3]);
        // TODO: 9/3/2022 make it work with events 
        if(originalConfigurationDTO.isPlugged()){
            plugBoardConnectionsLabel.setText(originalConfigurationDTO.getPlugBoardConnectionsWithFormat());
        }
        else
            plugBoardConnectionsLabel.setText("");

        fullConfigurationLabel.setText(originalConfiguration.toString());
    }
    public UserConfigurationDTO getUserConfigurationDTO(){
        List<String> chosenRotors = chosenRotorsList.stream().map(p -> p.getValue()).collect(Collectors.toList());
        Collections.reverse(chosenRotors);
        if (api.isIdenticalRotors(chosenRotors)) {
            System.out.println("error accoured");
            return null;
        }
        String chosenPositions = getChosenPosition();
        String chosenReflector = this.ReflectorOptions.getValue();
        return new UserConfigurationDTO(chosenRotors, chosenPositions, chosenReflector);
    }

    public boolean setPlugBoard(UserConfigurationDTO Specification){
        String connections = PlugBoardUI.getConnections();
        if (PlugBoardUI.amountOfCharacterSelected % 2 != 0) {
            System.out.println("problem at plugboard");
            return false;
        }
        else{
            Specification.setPlugBoardConnections(connections);
            System.out.println(connections);
            return true;
        }
    }


    public String getChosenPosition() {
        String positionsToReturn = chosenPositions.stream().
                map(p -> p.getValue().
                        toString()).reduce("", String::concat);
        System.out.println(positionsToReturn);
        return positionsToReturn;
    }



       /* List<Node> rotorsList = HBoxlistOfRotorsButtons.getChildren();
        System.out.println(FlowPaneAlphabet.getChildren().get(1));
        //the first node in the hbox is label so we skip on him
       for (int i = 1; i < rotorsList.size() ; i++) {
            String str = (String)HBoxlistOfRotorsButtons.getChildren().get(1).
        }
        String plugBoardConnection = new String();*/


    // TODO: 9/1/2022 move some functions to plugBoardUI
    @FXML
    void addOrRemovePlugBoard(ActionEvent event) {
        if (CheckBoxIsPluged.isSelected()) {
            PlugBoardUI plugBoard = new PlugBoardUI();
            for (char ch : Keyboard.alphabet.toCharArray()) {
                ToggleButton alphabetChar = new ToggleButton();
                alphabetChar.setText(String.valueOf(ch));
                alphabetChar.setMnemonicParsing(false);
                alphabetChar.setOnAction(e -> {
                    if(alphabetChar.isSelected())
                        setColors(alphabetChar);
                    else
                        removeColorPairs(alphabetChar);
                    System.out.println(PlugBoardUI.amountOfCharacterSelected);
                });

                FlowPaneAlphabet.getChildren().add(alphabetChar);
            }

        } else {
            FlowPaneAlphabet.getChildren().clear();
            PlugBoardUI.amountOfCharacterSelected = 0;

        }

    }
    public void removeColorPairs(ToggleButton alphabetChar) {
        if (!PlugBoardUI.havePair(alphabetChar)) {
            alphabetChar.setStyle(null);
            PlugBoardUI.amountOfCharacterSelected--;
        } else {
            PlugBoardUI.removePair(alphabetChar);
            PlugBoardUI.amountOfCharacterSelected -= 2;
        }
    }
    public void setColors(ToggleButton alphabetChar) {
        PlugBoardUI.amountOfCharacterSelected++;
        if (PlugBoardUI.amountOfCharacterSelected % 2 != 0) {
            this.CurrentColor = ChangeColor();
            alphabetChar.setStyle("-fx-background-color: " + CurrentColor);
            PlugBoardUI.setCurrentToggleButtonPressedWaitForPair(alphabetChar);
        } else {
            alphabetChar.setStyle("-fx-background-color:" + CurrentColor);
            PlugBoardUI.addToPair(alphabetChar);
        }
    }

    public String ChangeColor(){
        Color color =  Color.rgb(generateRandomIntForRgb(),
                generateRandomIntForRgb(),generateRandomIntForRgb());

        return color.toString().replace("0x", "#");

    }
    public int generateRandomIntForRgb(){
        return (int) (Math.random()*254+1);
    }
}







