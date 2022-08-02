package EngineMachine;

import java.util.HashMap;
import java.util.Map;

public class PlugBoard {
    private Map<Character, Character> mapInputOutput;
    public int pairsInPlug;
    public PlugBoard(String connections) {
        pairsInPlug = connections.length();
        initialPlugBoard(connections);
    }
    public PlugBoard()
    {
        pairsInPlug = 0;
        mapInputOutput = new HashMap<>();
    }
    private void initialPlugBoard(String connections) {
        mapInputOutput = new HashMap<>();
        String[] characterPairs = connections.split(",");
        for (int i = 0; i < characterPairs.length; i++) {
            Character c1 = (Character)characterPairs[i].charAt(0);
            Character c2 = (Character)characterPairs[i].charAt(2);// skip on |
            mapInputOutput.put(c1, c2);
            mapInputOutput.put(c2, c1);
        }
    }

    @Override
    public String toString() {
        return "plugBoard{" +
                "mapInputOutput=" + mapInputOutput +
                '}';
    }
    public boolean isPluged(Character checkIfPluged)
    {
        return mapInputOutput.containsKey(checkIfPluged);

    }
    public char getPlugedValue(char getValue){
        return mapInputOutput.get(getValue);
    }
}
