package engima;

import com.sun.xml.internal.bind.v2.TODO;

import java.util.*;

public class Reflector {
    Map<Integer,Integer> mapInputOutput;
    // TODO: 8/2/2022  change id from string to enum
    String id;
    public Reflector(String id, List <pairOfData> setPairs){
        if(setPairs.size()<1)
            throw new ExceptionInInitializerError("A reflector cannot contain zero pairs");
        else {
            setDictionary(setPairs);
            this.id = id;
        }

    }
    private void setDictionary(List <pairOfData> setPairs) {
        mapInputOutput = new HashMap<>();
        for (pairOfData pair : setPairs) {
            int input = Integer.parseInt((String.valueOf(pair.getRight())));
            int output = Integer.parseInt((String.valueOf(pair.getLeft())));
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
