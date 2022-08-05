package engine.api;

import engima.Machine.EnigmaMachine;
import engine.LoadData.LoadData;
import engine.LoadData.LoadDataFromXml;

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
}
