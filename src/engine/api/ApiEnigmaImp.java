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

import java.util.*;

public class ApiEnigmaImp implements ApiEnigma {

    private EnigmaMachine enigmaMachine;




    private boolean haveConfigurationFromFile;
    public xmlFileValidatorDTO readData(String filePath){
        return readDataFromFile(filePath);
    }

    // TODO: 8/13/2022 change the possible reflector to method of engine.enigma and not api
    // TODO: 8/17/2022 change change the validator to work from here and not from the load data
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

    // TODO: 8/7/2022 change to working with dto object
    // TODO: 8/7/2022 think of get all the steres to method in enigmaMachine and not in the api
    public void selectInitialCodeConfiguration(UserConfigurationDTO configuration){
        // TODO: 8/9/2022 here we add check if we have a machine and configuration from file
        enigmaMachine.selectInitialCodeConfiguration(configuration);
        //enigmaMachine.setConfigFromUser();
        //List<String> configurationList = getDataArrayInput(configuration);
        //setRotors(configurationList.get(0));
        //setPositonsOfRotors(configurationList.get(1));
        //setReflector(configurationList.get(2));
        //if(configurationList.size() == 4){// we got in the configuration plugBoard
          //  enigmaMachine.setPlugBoard(configurationList.get(3));
        //}

    }
    /*private void setReflector(String chosenReflector){
        Reflectors.ReflectorEnum reflectorEnum = Reflectors.ReflectorEnum.valueOf(chosenReflector);
        enigmaMachine.getReflectorsObject().SetChosenReflector(reflectorEnum);
    }
    private void setRotors(String chosenRotorsInput){
        List<String> chosenRotors = Arrays.stream(chosenRotorsInput.split(","))
                .collect(Collectors.toList());
        Collections.reverse(chosenRotors);
        enigmaMachine.getRotorsObject().setChosenRotorToUse(chosenRotors);
    }*/

    // TODO: 8/5/2022 check if number of position is the same as we need 
    // TODO: 8/5/2022 try to split with stream
   /* private void setPositonsOfRotors(String positonsArray){
        enigmaMachine.getRotorsObject().setPositions(positonsArray);

    }
    private List<String> getDataArrayInput(String configuration){
        String configTemp = configuration.replace("<","");
        String[] inputArray = configTemp.split(">");
        for (String str: inputArray) {
            configuration.replace(">","");

        }
        List<String> dataList =  Arrays.asList(inputArray);
        return dataList;
    }*/

    // TODO: 8/6/2022 change to DTOS object
    public String dataEncryption(String data){
        String encodeInformation = enigmaMachine.encodeString(data);
        return encodeInformation;
    }

    // TODO: 8/11/2022 convert to enigmaMachine
    public void systemReset(){
        enigmaMachine.getRotorsObject().returnRotorsToStartingPositions();
    }

    public UserConfigurationDTO AutomaticallyInitialCodeConfiguration(){
        enigmaMachine.automaticInitialCodeConfiguration();
         return showDataReceivedFromUser();
    }

    // TODO: 8/11/2022 convert to enigmamachine
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
}
