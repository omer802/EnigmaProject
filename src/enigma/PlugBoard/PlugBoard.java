package enigma.PlugBoard;

import java.util.*;

public class PlugBoard {
    private Map<Character, Character> mapInputOutput;
    public int pairsInPlug;
    public String connections;
    public PlugBoard(String connections) {
        this.connections = connections;
        pairsInPlug = connections.length();
        initialPlugBoard(connections);
    }
    public PlugBoard()
    {
        pairsInPlug = 0;
        mapInputOutput = new HashMap<>();
    }

    // TODO: 8/7/2022 changing to without , and |
    private void initialPlugBoard(String connections) {
        mapInputOutput = new HashMap<>();
        String[] characterPairs = connections.split(",");
        this.connections = connections;
        for (int i = 0; i < characterPairs.length; i++) {
            Character c1 = (Character)characterPairs[i].charAt(0);
            Character c2 = (Character)characterPairs[i].charAt(2);// skip on |
            mapInputOutput.put(c1, c2);
            mapInputOutput.put(c2, c1);
        }
    }

    @Override
    public String toString() {
        List<String> lstStringOfConnections = new ArrayList<>();
        String stringOfConnections = new String();
        Set<Character> alreadyAdded = new HashSet<>();
        for (int i = 0; i < mapInputOutput.size(); i++) {
            Character pluggedChar = connections.charAt(i);
            if (!alreadyAdded.contains(pluggedChar)) {
                stringOfConnections += pluggedChar;
                stringOfConnections += "|";
                stringOfConnections += mapInputOutput.get(connections.charAt(i));
                lstStringOfConnections.add(stringOfConnections);
                alreadyAdded.add(pluggedChar);
                alreadyAdded.add(mapInputOutput.get(pluggedChar));
            }
        }
        String res = String.join(",",lstStringOfConnections);
        return res;
    }
    public boolean isPluged(Character checkIfPluged)
    {
        return mapInputOutput.containsKey(checkIfPluged);

    }
    public char getPlugedValue(char getValue){
        return mapInputOutput.get(getValue);
    }
}
