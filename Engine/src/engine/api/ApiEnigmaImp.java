package engine.api;

import DTOS.ConfigrationsPropertyAdapter.FileConfigurationDTOAdapter;
import DTOS.ConfigrationsPropertyAdapter.UserConfigurationDTOAdapter;
import DTOS.Configuration.FileConfigurationDTO;
import DTOS.Configuration.UserConfigurationDTO;
import DTOS.Statistics.MachineStatisticsDTO;
import DTOS.Validators.xmlFileValidatorDTO;
import engine.enigma.Machine.EnigmaMachine;
import engine.enigma.Machine.NotchAndLetterAtPeekPane;
import engine.enigma.keyboard.Keyboard;
import engine.enigma.reflector.Reflectors;
import engine.LoadData.LoadData;
import engine.LoadData.LoadDataFromXml;
import engine.enigma.statistics.ConfigurationAndEncryption;
import engine.enigma.statistics.EncryptionData;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class ApiEnigmaImp implements ApiEnigma {

    private EnigmaMachine enigmaMachine;

    FileConfigurationDTOAdapter fileConfigurationDTOAdapter;
    UserConfigurationDTOAdapter userConfigurationDTOAdapter;
    StringProperty statistics;
    private boolean haveConfigurationFromFile;

    public void setDTOConfigurationAdapter(FileConfigurationDTOAdapter fileConfigurationDTOAdapter){
        this.fileConfigurationDTOAdapter = fileConfigurationDTOAdapter;
    }
    public xmlFileValidatorDTO readDataJavaFx(String filePath){
        xmlFileValidatorDTO validator = readData(filePath);
        if(validator.getListOfExceptions().size() == 0) {
            FileConfigurationDTO fileConfigurationDTO = showDataReceivedFromFile();
            fileConfigurationDTOAdapter.setDataFromFileDTO(fileConfigurationDTO);
        }
        return validator;


    }
    public xmlFileValidatorDTO readData(String filePath){
        return readDataFromFile(filePath);
    }

    private xmlFileValidatorDTO readDataFromFile(String filePath){
        xmlFileValidatorDTO validator = new xmlFileValidatorDTO(filePath);
        if(validator.getListOfExceptions().size()==0){
            LoadData dataFromXml = new LoadDataFromXml();
            EnigmaMachine tempEnigmaMachine = dataFromXml.loadDataFromInput(filePath,validator);
            if(validator.getListOfExceptions().size()== 0 && tempEnigmaMachine!=null) {
                haveConfigurationFromFile = true;
                this.enigmaMachine = tempEnigmaMachine;
            }
        }
       return validator;
    }

    public List<String> getPossibleReflectors(){

        return enigmaMachine.getPossibleReflectors();
    }
    public List<String> isReflectorValid(String chosenReflector){
        List<String> ReflectorErrors = new ArrayList<>();
        if(!chosenReflector.matches("[0-9]+"))
           ReflectorErrors.add("Error: It is not possible to insert an input that is not a number. " +
                   "you need to select ");
        else {
            int chosenReflectorInteger = Integer.parseInt(chosenReflector);
            if (!getPossibleReflectors().contains(Reflectors.IntegerToReflectorString(chosenReflectorInteger)))
                ReflectorErrors.add("Error: There is no reflector in the system with the entered id");
        }
        return ReflectorErrors;
    }
    public FileConfigurationDTO showDataReceivedFromFile(){
        FileConfigurationDTO Machinespecification = new FileConfigurationDTO(enigmaMachine);
        return Machinespecification;
    }
    public UserConfigurationDTO getCurrentConfiguration(){
        UserConfigurationDTO Machinespecification =  new UserConfigurationDTO(enigmaMachine);
        return Machinespecification;
    }

    public void selectInitialCodeConfiguration(UserConfigurationDTO configuration){
        enigmaMachine.selectInitialCodeConfiguration(configuration);
        updateStatisticsProperty();

    }

    public String dataEncryption(String data){
        String encodeInformation = enigmaMachine.encodeString(data);
        UserConfigurationDTO config = getCurrentConfiguration();
        UpdateCode(config);
        updateStatisticsProperty();
        System.out.println("-----------------");
        System.out.println(statistics.getValue());
        return encodeInformation;

    }
    public void updateStatistics(String input, String output, long processingTime){
        enigmaMachine.addEncryptionToStatistics(input,output,processingTime);
        updateStatisticsProperty();
    }
    public Character encryptChar(char ch){
        Character toReturnChar = enigmaMachine.encodeChar(ch);
        UserConfigurationDTO config = getCurrentConfiguration();
        UpdateCode(config);
        updateStatisticsProperty();
        return toReturnChar;
    }

    public void resetPositions(){
        enigmaMachine.getRotorsObject().returnRotorsToStartingPositions();
        UserConfigurationDTO config = getCurrentConfiguration();
        UpdateCode(config);
    }

    // TODO: 9/3/2022 update code
    public UserConfigurationDTO AutomaticallyInitialCodeConfiguration(){
        enigmaMachine.automaticInitialCodeConfiguration();
         return getCurrentConfiguration();
         //public void updatescrene
    }

    public List<String> getPossibleRotors(){
       return enigmaMachine.getPossibleRotors();
    }
    public int getAmountOfRotors(){
        return enigmaMachine.getRotorsAmountInUse();
    }
    public List<String> isLegalRotors(String chosenRotorsInputStr)  {
        List<String> RotorsErrors = new ArrayList<>();
        List<String> chosenRotorsList = cleanStringAndReturnList(chosenRotorsInputStr);
        List<String> possibleRotors = getPossibleRotors();
        if(chosenRotorsList.size()!= getAmountOfRotors())
            RotorsErrors.add("Error: The amount of rotors does not correspond to the amount of rotors" +
                    " that exist in the system." + " You have to insert "+ getAmountOfRotors()+" rotors from: "+ getPossibleRotors());

        if(!possibleRotors.containsAll(chosenRotorsList))
            RotorsErrors.add("Error: The rotors you are trying to insert is out of range. You have to insert " + getAmountOfRotors()+" rotors from: "+ getPossibleRotors());
        // TODO: 9/1/2022 check if working with console
        if(isIdenticalRotors(chosenRotorsList))
            RotorsErrors.add("Error: You have entered more than one identical rotor. You have to insert " + getAmountOfRotors()+" rotors from: "+ getPossibleRotors());
        return RotorsErrors;
    }
    public boolean isIdenticalRotors(List<String> chosenRotorsList){
        return chosenRotorsList.stream().distinct().count() != chosenRotorsList.size();

    }
    public List<String> cleanStringAndReturnList(String chosenRotorsInputStr){
        //Clearing spaces  and tabs from the user if entered
        String chosenRotors = chosenRotorsInputStr.replaceAll(" ", "");
        chosenRotors = chosenRotors.replaceAll("\t", "");
        List<String> chosenRotorsInput =  Arrays.asList(chosenRotors.split(","));
        return chosenRotorsInput;
    }
    public boolean isConfigFromUser() {
        return haveConfigurationFromFile;
    }
    public boolean isRotorsPositionsInRange(String positions){
        return Keyboard.isStringInRange(positions);
    }

    public boolean alreadyEncryptedMessages(){
        return EnigmaMachine.getTheNumberOfStringsEncrypted()>0;
    }
    public UserConfigurationDTO getOriginalConfiguration(){
        return enigmaMachine.getCurrentOriginalConfiguration();
    }

    public MachineStatisticsDTO getStatistics(){
        return enigmaMachine.getStatistics();
    }
    public boolean haveConfigFromUser(){
        return enigmaMachine.isConfigFromUser();
    }
    public boolean haveConfigFromFile(){
        return enigmaMachine.isConfigFromFile();
    }

    public void saveMachineStateToFile(String filePath) throws IOException {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(filePath))) {
            out.writeObject(this.enigmaMachine);
            out.flush();
        }
    }
    public void loadMachineStateFromFIle(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(filePath))) {
            // we know that we read the enigmaMachine we saved
            this.enigmaMachine =
                    (EnigmaMachine) in.readObject();
        }
    }
    public boolean validateStringToEncrypt(String stringToEncrypt) {
        return (Keyboard.isStringInRange(stringToEncrypt));
    }


    public  StringBuilder getStringDataReceiveFromUser(UserConfigurationDTO machineConfigUser){
        StringBuilder stringBuilder = new StringBuilder();
        List<String> rotorsWithOrder = machineConfigUser.getChosenRotorsWithOrder();
        getChosenRotorsWithOrder(rotorsWithOrder, stringBuilder);

        List<NotchAndLetterAtPeekPane> currentPositionsAndNotch = machineConfigUser.getNotchAndLetterPair();
        getCurrentPositionsWithDistanceFromNotch(currentPositionsAndNotch, stringBuilder);

        stringBuilder.append("<"+machineConfigUser.getChosenReflector()+">");
        if(machineConfigUser.isPlugged()&&machineConfigUser.getPlugBoardConnectionsWithFormat().length()>0)
            stringBuilder.append("<" + machineConfigUser.getPlugBoardConnectionsWithFormat()+ ">");
        return stringBuilder;
    }
    public void getChosenRotorsWithOrder( List<String> rotorsWithOrder, StringBuilder stringBuilderInput){
        boolean isFirst = true;
        stringBuilderInput.append("<");
        for (String rotorID: rotorsWithOrder) {
            if(!isFirst)
                stringBuilderInput.append(",");
            stringBuilderInput.append(rotorID);
            isFirst = false;
        }
        stringBuilderInput.append(">");
    }
    public void getCurrentPositionsWithDistanceFromNotch(List<NotchAndLetterAtPeekPane> chosenRotors, StringBuilder stringBuilderInput){
        //reverse the order of rotors beacuse in the machine we work from right to left and the ui show from left to right
        //Collections.reverse(chosenRotors);
        boolean isFirst = true;
        stringBuilderInput.append("<");
        for (NotchAndLetterAtPeekPane pair: chosenRotors)
            stringBuilderInput.append(pair.toString());
        stringBuilderInput.append( ">");
    }
    public void setCurrentConfigurationProperties(UserConfigurationDTOAdapter DTOPropertiesToConfig){
        userConfigurationDTOAdapter = DTOPropertiesToConfig;
        UserConfigurationDTO originalConfigurationDTO = getOriginalConfiguration();
        setUserConfigurationDTO(originalConfigurationDTO);
    }
    public void UpdateCode(UserConfigurationDTO originalConfigurationDTO) {
        StringBuilder originalConfiguration = getStringDataReceiveFromUser(originalConfigurationDTO);
        String[] configurationArray = originalConfiguration.toString().replace(">", "")
                .split("<");
        if (userConfigurationDTOAdapter != null) {
            userConfigurationDTOAdapter.setNotchAndLetterAtPeekPaneStartingPosition(configurationArray[2]);
            userConfigurationDTOAdapter.setFullConfiguration(originalConfiguration.toString());
        }
    }
    public void setUserConfigurationDTO(UserConfigurationDTO originalConfigurationDTO){
        StringBuilder originalConfiguration = getStringDataReceiveFromUser(originalConfigurationDTO);
        String[] configurationArray = originalConfiguration.toString().replace(">","")
                .split("<");
        userConfigurationDTOAdapter.setChosenRotors(configurationArray[1]);
        userConfigurationDTOAdapter.setNotchAndLetterAtPeekPaneStartingPosition(configurationArray[2]);
        userConfigurationDTOAdapter.setChosenReflector(configurationArray[3]);
        // TODO: 9/3/2022 make it work with events
        if(enigmaMachine.isPluged()){
            userConfigurationDTOAdapter.setPlugBoardToShow(originalConfigurationDTO.getPlugBoardConnectionsWithFormat());
        }
        else
            userConfigurationDTOAdapter.setPlugBoardToShow("");

        userConfigurationDTOAdapter.setFullConfiguration(originalConfiguration.toString());
      // dataEncryption("AAA");
    }

    public void setStatisticsProperty(StringProperty statisticsProperty){
        this.statistics = new SimpleStringProperty();
       statisticsProperty.bind(Bindings.format("%s",(statistics)));
    }
    private void updateStatisticsProperty(){
        this.statistics.setValue(PrintStatistic().toString());
    }

    public StringBuilder PrintStatistic() {
        String pattern = "###,###,###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        MachineStatisticsDTO statisticsToShow = getStatistics();
        StringBuilder sb = new StringBuilder();
        sb.append("History and statistics:\n");
        sb.append("the code configurations performed in the system:\n");
        for (ConfigurationAndEncryption configuration: statisticsToShow.getConfigurations())
            sb.append("\t"+getStringDataReceiveFromUser(configuration.getConfiguration())+"\n");
        sb.append("\n");
        if(statisticsToShow.haveEncoded()) {
            sb.append("Configurations used by the machine and the encrypted messages in each configuration:\n");
            for (ConfigurationAndEncryption configuration : statisticsToShow.getConfigurationsInUse()) {
                UserConfigurationDTO config = configuration.getConfiguration();
                sb.append("In configuration:" + getStringDataReceiveFromUser(config) + " The following messages have been encrypted: \n");

                for (EncryptionData data : configuration.getEncryptionDataList()) {
                    String Format = decimalFormat.format(data.getProcessingTime());
                    sb.append("\t#.<" + data.getInput().toUpperCase() + ">" + "--> " + "<" + data.getOutput().toUpperCase() + ">" + " Encryption time in nano seconds: " + Format + "\n");
                }
            }
        }
        else
            sb.append("The machine has not yet encrypted messages");
        return sb;
    }


}
