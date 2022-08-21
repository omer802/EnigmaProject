package DTOS;

import engine.enigma.Machine.EnigmaMachine;

public class FileConfigurationDTO {

    private final int countOfRotors;



    private int countOfRotorsInUse;
    //private List<PairOfNotchAndRotorId> NotchAndIds;
    private final int countOfReflectors;

    private int numberOfMessageEncrypted;

    private boolean isConfigFromUser;


    public FileConfigurationDTO(final EnigmaMachine machineInput) {
        //setPairOfNotchAndRotorId(machineInput);
        this.countOfRotors = machineInput.getRotorsObject().getRotorsAmount();
        this.countOfRotorsInUse = machineInput.getRotorsAmountInUse();
        this.countOfReflectors = machineInput.getReflectorsObject().getReflectorsAmount();
        this.numberOfMessageEncrypted = machineInput.theNumberOfStringsEncrypted;
        this.isConfigFromUser = machineInput.isConfigFromUser();


    }
    public int getCountOfRotors() {
        return countOfRotors;
    }

    public int getCountOfReflectors() {
        return countOfReflectors;
    }
    public int getCountOfRotorsInUse() {
        return countOfRotorsInUse;
    }

    public int getNumberOfMessageEncrypted() {
        return numberOfMessageEncrypted;
    }
    public boolean isConfigFromUser() {
        return isConfigFromUser;
    }
}
