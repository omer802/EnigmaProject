package EngineMachine;

import java.util.List;

public class RotatingRotor extends Rotor {
    private int notch;

    public RotatingRotor(int notch, int startingPosition, String id, List<pairOfData> setPairArray) {
        super(startingPosition, id, setPairArray);
        this.notch = notch;
    }
    public void rotate() {
        position = (position + 1) % PairOfDataArray.size();
    }
    public boolean checkNotch() {
        if(position == notch)
            return true;
        return false;
    }


}
