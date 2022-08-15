package engine.enigma;

import engine.enigma.Machine.EnigmaMachine;

// TODO: 8/8/2022 make it a warp class to machine 
public class enigma {
    protected EnigmaMachine Machine;
    public void setMachine(EnigmaMachine machine) {
        Machine = machine;
    }

    public EnigmaMachine getMachine() {
        return Machine;
    }

}
