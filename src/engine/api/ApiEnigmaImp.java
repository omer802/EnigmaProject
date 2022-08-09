package engine.api;

import DTOS.MachineSpecification;
import enigma.Machine.EnigmaMachine;
import enigma.reflector.Reflectors;
import engine.LoadData.LoadData;
import engine.LoadData.LoadDataFromXml;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ApiEnigmaImp implements ApiEnigma {

    private EnigmaMachine enigmaMachine;

    public void readData(String filePath){
        readDataFromFile(filePath);
    }
    private void readDataFromFile(String filePath){

        if(filePath.endsWith(".xml")){
            LoadData dataFromXml = new LoadDataFromXml();
            enigmaMachine = dataFromXml.loadDataFromInput(filePath);
        }
       else{
           throw new IllegalArgumentException("Please insert .xml files only");
        }
    }
    public MachineSpecification showData(){
        MachineSpecification Machinespecification = new MachineSpecification(enigmaMachine);
        return Machinespecification;

    }

    // TODO: 8/7/2022 change to working with dto object
    // TODO: 8/7/2022 think of get all the steres to method in enigmaMachine and not in the api
    public void selectInitialCodeConfiguration(String configuration){
        enigmaMachine.setConfigFromUser();
        List<String> configurationList = getDataArrayInput(configuration);
        setRotors(configurationList.get(0));
        setPositonsOfRotors(configurationList.get(1));
        setReflector(configurationList.get(2));
        if(configurationList.size() == 4){// we got in the configuration plugBoard
            enigmaMachine.setPlugBoard(configurationList.get(3));
        }

    }
    private void setReflector(String chosenReflector){
        Reflectors.ReflectorEnum reflectorEnum = Reflectors.ReflectorEnum.valueOf(chosenReflector);
        enigmaMachine.getReflectors().SetChosenReflector(reflectorEnum);
    }
    private void setRotors(String chosenRotorsInput){
        List<String> chosenRotors = Arrays.stream(chosenRotorsInput.split(","))
                .collect(Collectors.toList());
        Collections.reverse(chosenRotors);
        enigmaMachine.getRotorsObject().setChosenRotorToUse(chosenRotors);
    }

    // TODO: 8/5/2022 check if number of position is the same as we need 
    // TODO: 8/5/2022 try to split with stream
    private void setPositonsOfRotors(String positonsArray){
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
    }

    // TODO: 8/6/2022 change to DTOS object
    public String dataEncryption(String data){
        String encryptedInformation = enigmaMachine.encodeString(data);
        return encryptedInformation;
    }
    public void systemReset(){
        enigmaMachine.getRotorsObject().returnRotorsToStartingPositions();
    }

    public void AutomaticallyInitialCodeConfiguration(){

    }
}
