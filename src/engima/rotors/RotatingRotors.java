package engima.rotors;

import java.util.List;

public class RotatingRotors {

    private List<RotatingRotor> Rotors;
    private int RotorsAmount;

    private int RotorsAmountInUse;


    public void setRotors(List<RotatingRotor> rotors) {
        Rotors = rotors;
    }

    public List<RotatingRotor> getRotors() {
        return Rotors;
    }

    public int getRotorsAmount() {
        return RotorsAmount;
    }

    public void setRotorsAmount(int rotorsAmount) {
        RotorsAmount = rotorsAmount;
    }


    public int getRotorsAmountInUse() {
        return RotorsAmountInUse;
    }

    public void setRotorsAmountInUse(int rotorsAmountInUse) {
        RotorsAmountInUse = rotorsAmountInUse;
    }


}
