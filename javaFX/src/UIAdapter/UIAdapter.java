package UIAdapter;

import engine.decryptionManager.task.AgentCandidatesList;

import java.util.List;

public interface UIAdapter {
    // TODO: 9/14/2022 interface need public to private in the start of the method?
    public void AddCandidateStringForDecoding(AgentCandidatesList Candidate);
}
