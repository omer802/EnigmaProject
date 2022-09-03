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

import java.io.*;
import java.util.*;

public class ApiEnigmaImp implements ApiEnigma {

    private EnigmaMachine enigmaMachine;

    FileConfigurationDTOAdapter fileConfigurationDTOAdapter;
    UserConfigurationDTOAdapter userConfigurationDTOAdapter;
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

    }




    public String dataEncryption(String data){
        String encodeInformation = enigmaMachine.encodeString(data);
        return encodeInformation;
    }

    public void systemReset(){
        enigmaMachine.getRotorsObject().returnRotorsToStartingPositions();
    }

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
        return Keyboard.isStringInRange(stringToEncrypt);
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
        this.userConfigurationDTOAdapter = DTOPropertiesToConfig;
    }

}
