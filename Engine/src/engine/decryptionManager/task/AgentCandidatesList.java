package engine.decryptionManager.task;

import java.util.ArrayList;
import java.util.List;

public class AgentCandidatesList {
    Long timeToEval;
    String agentName;

    private List<String> candidates;
    private List<String> configurationList;

    public boolean isEmpty() {
        return empty;
    }

    private boolean empty;
    public AgentCandidatesList( long startingTime, String agentName){
        this.candidates = new ArrayList<>();
        this.configurationList = new ArrayList<>();
        this.timeToEval = startingTime;
        this.agentName = agentName;
        this.empty = true;
    }

    public void addCandidate(String candidate, String configuration){
        this.empty = false;
        this.candidates.add(candidate);
        this.configurationList.add(configuration);
    }
    public List<String> getCandidates() {
        return candidates;
    }

    public long getTimeToEval() {
        return timeToEval;
    }

    public String getAgentName() {
        return agentName;
    }
    public List<String> getConfigurationList() {
        return configurationList;
    }

}
