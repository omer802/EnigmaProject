package engine.decryptionManager.task;

import engine.enigma.keyboard.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class CodeConfigurationGenerator {
    private List<String> positionsToSend;
    private String currentPosition;
    private List<Integer> positionsIndex;
    private int positionLength;
    public CodeConfigurationGenerator(int positionLength){
        this.positionLength = positionLength;
        initPositions();
    }
    private void initPositions(){
        currentPosition = new String();
        positionsIndex = new ArrayList<>();
        for (int i = 0; i < positionLength; i++) {
            currentPosition+= Keyboard.getCharFromAlphabet(0);
            positionsIndex.add(0);

        }
    }
    public List<String> generateNextPositionsListForTask(int taskSize){
        List<String> positionsList = new ArrayList<>();

        for (int i = 0; i < taskSize ; i++) {
            positionsList.add(getAdvancedPosition());
        }
        return positionsList;
    }
    private String getAdvancedPosition(){
        String advancedPosition = new String();
        for (int i =0; i <positionLength; i++) {
            int indexInKeyBoard = positionsIndex.get(i)%Keyboard.keyboardSize();
            advancedPosition+= Keyboard.getCharFromAlphabet(indexInKeyBoard);
        }
        advanceIndexPositionsList();
        return advancedPosition;
    }
    private void advanceIndexPositionsList(){
        boolean isFirstPosition = true;
        boolean isFirstIteration = true;
        for (int i = positionLength -1; i >= 1; i--) {
            int positionIndex = positionsIndex.get(i);
            if(isFirstPosition) {
                positionIndex = (positionIndex + 1) % Keyboard.keyboardSize();
                positionsIndex.set(i, positionIndex);
                isFirstPosition = false;
            }
            //if covered all character at alphabet advance next position. (its like counting at alphabet size base)
            if (positionIndex==0) {
                positionsIndex.set(i,positionIndex);
                int nextPositionValue = positionsIndex.get(i-1);
                positionsIndex.set(i-1,(nextPositionValue+1) % Keyboard.keyboardSize());
            }
            else{
                break;
            }
        }

    }


}
