package engine.enigma;

import engine.enigma.Machine.EnigmaMachine;

public class enigma {
    protected EnigmaMachine Machine;
    public void setMachine(EnigmaMachine machine) {
        Machine = machine;
    }

    public EnigmaMachine getMachine() {
        return Machine;
    }

}
