package engima.rotors;

import engima.pairOfData;

import java.util.List;

public class RotatingRotor extends Rotor implements advanceRotor {
    protected int notch;
    private RotatingRotor nextRotor;


    private boolean lastRotor;


    public RotatingRotor(int notch, String id, List<pairOfData> setPairArray) {
        super( id, setPairArray);
        this.notch = notch;
    }
    public void advanceNextRotor() {
        position = (position + 1) % PairOfDataArray.size();
        if(!islastRotor()){
            if(nextRotor==null)
                // TODO: 8/4/2022 throw exption
                throw  new ExceptionInInitializerError("you havent intialzie rotors right");
            else{
                if(atNotch()) {
                    nextRotor.advanceNextRotor();
                }
            }
        }
    }
    public boolean atNotch() {
        if (position == notch)
            return true;
        return false;
    }
    public void setNextRotor(RotatingRotor nextRotor) {
        this.nextRotor = nextRotor;
    }
    public RotatingRotor getNextRotor() {
        return nextRotor;
    }
    public boolean islastRotor() {
        return lastRotor;
    }

    public void setIslastRotor(boolean islastRotor) {
        this.lastRotor = islastRotor;
    }


}
