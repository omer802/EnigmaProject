package engine.enigma;

import engine.decryptionManager.DM;
import engine.enigma.Machine.EnigmaMachine;

public class Enigma {

    private DM decipher;
    protected EnigmaMachine Machine;
    public Enigma(EnigmaMachine machine, DM decipher){
        this.Machine = machine;
        this.decipher = decipher;
    }
    public void setMachine(EnigmaMachine machine) {
        Machine = machine;
    }

    public EnigmaMachine getMachine() {
        return Machine;
    }
    public DM getDecipher() {
        return decipher;
    }

    public void setDecipher(DM decipher) {
        this.decipher = decipher;
    }

}
