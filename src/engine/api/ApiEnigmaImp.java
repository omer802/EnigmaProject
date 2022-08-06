package engine.api;

import engima.Machine.EnigmaMachine;
import engine.LoadData.LoadData;
import engine.LoadData.LoadDataFromXml;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ApiEnigmaImp implements ApiEnigma {

    EnigmaMachine enigmaMachine;

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
    public void showData(){

    }
    public void selectInitialCodeConfiguration(String configuration){
        List<String> configurationList = getDataArrayInput(configuration);
        setRotors(configurationList.get(0));
        setPositons(configurationList.get(1));

    }
    public void setRotors(String chosenRotorsInput){
        List<String> chosenRotors = Arrays.stream(chosenRotorsInput.split(","))
                .collect(Collectors.toList());
        enigmaMachine.getRotors().setChosenRotorsId(chosenRotors);
    }

    // TODO: 8/5/2022 check if number of position is the same as we need 
    // TODO: 8/5/2022 try to split with stream
    private void setPositons(String positonsArray){
       List<Integer> position = Arrays.stream(positonsArray.split(","))
                                                .map(Integer::valueOf)
                                                .collect(Collectors.toList());
       enigmaMachine.getRotors().setPositions(position);

        
    }
    // 1. positions of rotors
    // 2. positions of cycle
    // 3. chose reflector
    // 4. choose plugs. optimal.
    private List<String> getDataArrayInput(String configuration){
        String configTemp = configuration.replace("<","");
        String[] inputArray = configTemp.split(">");
        for (String str: inputArray) {
            configuration.replace(">","");

        }
        List<String> dataList =  Arrays.asList(inputArray);
        return dataList;
    }

}
