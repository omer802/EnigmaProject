package engine.decryptionManager.Agents;

import engine.enigma.Machine.EnigmaMachine;

import java.util.ArrayList;
import java.util.List;

public class Agents {
    private List<Agent> agents;
    final int AgentsAmount;

    public EnigmaMachine getMachine() {
        return machine;
    }

    public void setMachine(EnigmaMachine machine) {
        this.machine = machine;
    }

    private EnigmaMachine machine;
    public Agents(EnigmaMachine machine, int AgentsAmount){
        this.AgentsAmount = AgentsAmount;
        createAgents(machine);
    }
    private void createAgents(EnigmaMachine machine){
        agents = new ArrayList<>();
        for (int i = 0; i < AgentsAmount; i++) {
            EnigmaMachine machineClone = machine.clone();
            String ID = new String("Agent " +i+1);
            agents.add(new Agent(machineClone,ID));

        }
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public int getAgentsAmount(){
        return AgentsAmount;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }
}
