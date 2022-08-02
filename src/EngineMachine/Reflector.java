package EngineMachine;

import java.util.*;

public class Reflector {
    Map<Integer,Integer> mapInputOutput;
    public Reflector(List <pairOfData> setPairs){
        if(setPairs.size()<1)
            throw new ExceptionInInitializerError("A reflector cannot contain zero pairs");
        else
            setDictionary(setPairs);


    }
    private void setDictionary(List <pairOfData> setPairs) {
        mapInputOutput = new HashMap<>();
        for (pairOfData pair : setPairs) {
            int input = Integer.parseInt((String.valueOf(pair.getInput())));
            int output = Integer.parseInt((String.valueOf(pair.getOutput())));
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
