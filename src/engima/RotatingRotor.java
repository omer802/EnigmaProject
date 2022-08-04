package engima;

import java.util.List;

public class RotatingRotor extends Rotor {
    protected int notch;

    public RotatingRotor(int notch, String id, List<pairOfData> setPairArray) {
        super( id, setPairArray);
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
