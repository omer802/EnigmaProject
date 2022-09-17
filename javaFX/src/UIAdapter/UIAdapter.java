package UIAdapter;

import engine.decryptionManager.task.AgentCandidatesList;

public interface UIAdapter {
    // TODO: 9/14/2022 interface need public to private in the start of the method?
    public void AddCandidateStringForDecoding(AgentCandidatesList Candidate);

    public UIAdapterImpJavaFX.RunningState getState();
    public void pauseTask();
    public void resumeTask();
    public void stopTask();
    public void taskInProcess();
    }
