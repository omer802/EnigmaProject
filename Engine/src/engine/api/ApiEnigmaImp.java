package engine.api;

import DTOS.FileConfigurationDTO;
import DTOS.UserConfigurationDTO;
import DTOS.MachineStatisticsDTO;
import DTOS.Validators.xmlFileValidatorDTO;
import engine.enigma.Machine.EnigmaMachine;
import engine.enigma.keyboard.Keyboard;
import engine.enigma.reflector.Reflectors;
import engine.LoadData.LoadData;
import engine.LoadData.LoadDataFromXml;

import java.io.*;
import java.util.*;

public class ApiEnigmaImp implements ApiEnigma {

    private EnigmaMachine enigmaMachine;




    private boolean haveConfigurationFromFile;
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
    public UserConfigurationDTO showDataReceivedFromUser(){
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
         return showDataReceivedFromUser();
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
        if(chosenRotorsList.stream().distinct().count() != chosenRotorsList.size())
            RotorsErrors.add("Error: You have entered more than one identical rotor. You have to insert " + getAmountOfRotors()+" rotors from: "+ getPossibleRotors());
        return RotorsErrors;
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
        return EnigmaMachine.theNumberOfStringsEncrypted>0;
    }
    public UserConfigurationDTO getCurrentConfiguration(){
        return enigmaMachine.getCurrentConfiguration();
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
}
