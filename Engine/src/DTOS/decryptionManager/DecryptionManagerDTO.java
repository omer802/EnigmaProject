package DTOS.decryptionManager;

import UIAdapter.UIAdapter;
import engine.decryptionManager.DM;

public class DecryptionManagerDTO {
    private String messageToDecipher;
    private DM.DifficultyLevel level;
    private int missionSize;
    private UIAdapter uiAdapter;
    private int amountOfAgentsForProcess;
    private double missionAmount;

    public DecryptionManagerDTO(String messageToDecipher, DM.DifficultyLevel difficulty, int missionSize, UIAdapter uiAdapter, int amountOfAgentsForProcess, Double missionAmount){
       this.messageToDecipher = messageToDecipher;
       this.level = difficulty;
       this.missionSize = missionSize;
       this.uiAdapter = uiAdapter;
       this.amountOfAgentsForProcess = amountOfAgentsForProcess;
       this.missionAmount = missionAmount;
    }
    public String getMessageToDecipher() {
        return messageToDecipher;
    }

    public DM.DifficultyLevel getLevel() {
        return level;
    }

    public int getMissionSize() {
        return missionSize;
    }

    public UIAdapter getUiAdapter() {
        return uiAdapter;
    }

    public int getAmountOfAgentsForProcess() {
        return amountOfAgentsForProcess;
    }

    public double getMissionAmount() {
        return missionAmount;
    }
}
