package engine.decryptionManager.Agents;

import engine.enigma.Machine.EnigmaMachine;

public class Agent {
    EnigmaMachine machine;
    String ID;

    public Agent(EnigmaMachine machine, String ID){
        this.machine = machine;
        this.ID = ID;
    }
}
