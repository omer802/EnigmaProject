package engima.reflector;

import engima.reflector.inputOutputPair;

import java.util.*;

public class Reflector {
    Map<Integer,Integer> mapInputOutput;
    // TODO: 8/2/2022  change id from string to enum
    String id;
    public Reflector(String id, List <inputOutputPair> setPairs){
        if(setPairs.size()<1)
            throw new ExceptionInInitializerError("A reflector cannot contain zero pairs");
        else {
            setDictionary(setPairs);
            this.id = id;
        }

    }
    private void setDictionary(List <inputOutputPair> setPairs) {
        mapInputOutput = new HashMap<>();
        for (inputOutputPair pair : setPairs) {
            int input = pair.getInput();
            int output = pair.getOutput();
            mapInputOutput.put(input, output);
            mapInputOutput.put(output, input);
        }
    }
    public Integer reflect(int index){
        return mapInputOutput.get(index);
    }

    @Override
    public String toString() {
        return "reflector{" +
                "mapInputOutput=" + mapInputOutput +
                '}';
    }
}
