package engima.rotors;

import java.util.ArrayList;
import java.util.List;

public class RotatingRotors {

    private List<RotatingRotor> rotors;
    private int rotorsAmount;
    private int rotorsAmountInUse;
    private List<RotatingRotor> chosenRotorsArray;

    public RotatingRotors(List<RotatingRotor> rotors){
        setRotors(rotors);
        setRotorsAmount(rotors.size());
        rotorsAmountInUse = 0;
    }
    public void setRotors(List<RotatingRotor> rotors) {this.rotors = rotors;
    }

    public List<RotatingRotor> getRotors() {
        return rotors;
    }

    public int getRotorsAmount() {
        return rotorsAmount;
    }

    public void setRotorsAmount(int rotorsAmount) {
        rotorsAmount = rotorsAmount;
    }


    public int getRotorsAmountInUse() {
        return rotorsAmountInUse;
    }

    private void setRotorsAmountInUse() {this.rotorsAmountInUse = chosenRotorsArray.size();
    }

    public List<RotatingRotor> getChosenRotors() {

        return chosenRotorsArray;
    }

    public void setChosenRotorToUse(List<String> chosenRotors) {
        if(chosenRotors.size()>rotors.size())
            throw new IllegalArgumentException("It is not possible to initialize an amount of rotors in use that is greater than the existing amount of rotors");
        List<RotatingRotor> chosenRotorsToUpdate = new ArrayList<>();
        for (int i = 0; i <chosenRotors.size() ; i++) {
            for (int j = 0; j <rotors.size() ; j++) {
                if(chosenRotors.get(i).equals(rotors.get(j).getId()))
                    chosenRotorsToUpdate.add(rotors.get(j));
            }
        }
        chosenRotorsArray = chosenRotorsToUpdate;
        setRotorsAmountInUse();
        setRotorsChain();
    }

    // TODO: 8/6/2022 think about what exption i need here. like if the position is not in the scoop in every rotor
    public void setPositions(String positions)
    {
        for (int i = 0; i <positions.length() ; i++){
           // The positions of the rotors were determined from right to left, although the data is typed from left to right
            char charPositionEndToBegin = positions.charAt(positions.length() - i -1);
            getChosenRotors().get(i).setPosition(charPositionEndToBegin);
    }
    }
    public void advanceRotorsInChain()
    {
        getChosenRotors().get(0).advanceNextRotor();
    }
    public  void setRotorsChain(){
        for (int i = 0; i < chosenRotorsArray.size()-1; i++) {
            chosenRotorsArray.get(i).setNextRotor(chosenRotorsArray.get(i+1));
            chosenRotorsArray.get(i).setIslastRotor(false);
        }
        chosenRotorsArray.get(chosenRotorsArray.size()-1).setNextRotor(null);
        chosenRotorsArray.get(chosenRotorsArray.size()-1).setIslastRotor(true);
    }


}
