package DTOS;

import engine.enigma.Machine.EnigmaMachine;

// TODO: 8/5/2022  menu 2
public class FileConfigurationDTO {

    // TODO: 8/9/2022 think if divers it to 2 files 
    private final int countOfRotors;



    private int countOfRotorsInUse;
    //private List<PairOfNotchAndRotorId> NotchAndIds;
    private final int countOfReflectors;

    private int numberOfMessageEncrypted;

    private boolean isConfigFromUser;


    // TODO: 8/13/2022 notice that the number of rotors is from file and not the user chose this  
    // TODO: 8/13/2022 every rotor have uniqe id  
    // TODO: 8/13/2022 כל רוטור ממפה באופן חד חד ערכי  
    // TODO: 8/13/2022 every rotor have notch in range  
    // TODO: 8/13/2022 if file not correct give a exeption with what went wrong 
    // TODO: 8/13/2022 check if when we upload again file the last one destroy
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
