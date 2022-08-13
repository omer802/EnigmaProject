package engine.api;

import DTOS.MachineSpecificationFromFile;
import DTOS.MachineSpecificationFromUser;
import enigma.Machine.EnigmaMachine;
import enigma.keyboard.Keyboard;
import enigma.reflector.Reflector;
import enigma.reflector.Reflectors;
import engine.LoadData.LoadData;
import engine.LoadData.LoadDataFromXml;
import sun.misc.ExtensionInstallationException;

import java.util.*;
import java.util.stream.Collectors;

public class ApiEnigmaImp implements ApiEnigma {

    private EnigmaMachine enigmaMachine;




    private boolean haveConfigurationFromFile;
    public void readData(String filePath){
        readDataFromFile(filePath);
    }

    // TODO: 8/13/2022 change the possible reflector to method of enigma and not api 
    private void readDataFromFile(String filePath){

        if(filePath.endsWith(".xml")){
            LoadData dataFromXml = new LoadDataFromXml();
            enigmaMachine = dataFromXml.loadDataFromInput(filePath);
            haveConfigurationFromFile = true;
        }
       else{
           throw new IllegalArgumentException("Please insert .xml files only");
        }
    }

    public List<String> getPossibleReflectors(){

        return enigmaMachine.getPossibleReflectors();
    }
    public boolean isReflectorValid(String chosenReflector){
        if(Integer.parseInt(chosenReflector)>5|| Integer.parseInt(chosenReflector)<1)
            throw new NumberFormatException("The input is out of range");
        int chosenReflectorInteger = Integer.parseInt(chosenReflector);
        return getPossibleReflectors().contains(Reflectors.IntegerToReflectorString(chosenReflectorInteger));
    }
    public MachineSpecificationFromFile showDataReceivedFromFile(){
        MachineSpecificationFromFile Machinespecification = new MachineSpecificationFromFile(enigmaMachine);
        return Machinespecification;
    }
    public MachineSpecificationFromUser showDataReceivedFromUser(){
        MachineSpecificationFromUser Machinespecification =  new MachineSpecificationFromUser(enigmaMachine);
        return Machinespecification;
    }

    // TODO: 8/7/2022 change to working with dto object
    // TODO: 8/7/2022 think of get all the steres to method in enigmaMachine and not in the api
    public void selectInitialCodeConfiguration(MachineSpecificationFromUser configuration){
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
        String encryptedInformation = enigmaMachine.encodeString(data);
        return encryptedInformation;
    }

    // TODO: 8/11/2022 convert to enigmaMachine
    public void systemReset(){
        enigmaMachine.getRotorsObject().returnRotorsToStartingPositions();
    }

    public void AutomaticallyInitialCodeConfiguration(){

    }

    // TODO: 8/11/2022 convert to enigmamachine
    public List<String> getPossibleRotors(){
       return enigmaMachine.getPossibleRotors();
    }
    public int getAmountOfRotors(){
        return enigmaMachine.getAmountPossibleRotors();
    }
    public boolean isLegalRotors(String chosenRotorsInputStr)  {
        List<String> chosenRotorsList = cleanStringAndReturnList(chosenRotorsInputStr);
        List<String> possibleRotors = getPossibleRotors();
        if(chosenRotorsList.size()!= getAmountOfRotors())
            throw new ExceptionInInitializerError("The amount of rotors does not correspond to the amount of rotors" +
                    " that exist in the system" + "You have to insert "+ getAmountOfRotors()+" rotors");

        if(!possibleRotors.containsAll(chosenRotorsList))
            throw new ExceptionInInitializerError("The rotors you are trying to insert is out of range.");
        //check if there is no reption between objects
        if(chosenRotorsList.stream().distinct().count() != chosenRotorsList.size())
            throw new ExceptionInInitializerError("You have entered more than one identical rotor. ");
        return true;
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
    public MachineSpecificationFromUser getFirstConfig(){
        return enigmaMachine.getFirstConfiguration();
    }

}
