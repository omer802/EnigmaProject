package engima.rotors;

import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class RotatingRotors {

    private List<RotatingRotor> rotors;
    private int rotorsAmount;
    private int rotorsAmountInUse;
    private List<String> ChosenRotors;

    public RotatingRotors(List<RotatingRotor> rotors){
        this.rotors = rotors;
        this.rotorsAmount = rotors.size();
        rotorsAmountInUse = 0;
    }
    public List<RotatingRotor> getRotorsInUse(){
        List<RotatingRotor> rotorsToReturn = new ArrayList<>();
        for (String RotorId: ChosenRotors) {
            for (RotatingRotor rotor: rotors) {
                if(RotorId.equals(rotor.getId())) {
                    rotorsToReturn.add(rotor);
                }
            }
        }
        return  rotorsToReturn;
    }
    public void setRotors(List<RotatingRotor> rotors) {rotors = rotors;
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

    private void setRotorsAmountInUse() {rotorsAmountInUse = ChosenRotors.size();
    }

    public List<String> getChosenRotors() {

        return ChosenRotors;
    }

    public void setChosenRotorsId(List<String> chosenRotors) {
        if(chosenRotors.size()>rotors.size())
            throw new IllegalArgumentException("It is not possible to initialize an amount of rotors in use that is greater than the existing amount of rotors");
        ChosenRotors = chosenRotors;
        setRotorsAmount(chosenRotors.size());
    }

    //handle expetions
    public void setPositions(List<Integer> positions)
    {
        for (int i = 0; i < positions.size(); i++) {
            //String toConvert = positions.get(positions.size() - 1 - i);
           // int value[] = Arrays.stream() //getting the positions from right to left
            rotors.get(i).setPosition(positions.size()-1 -i);
        }
    }


}
