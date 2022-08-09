package DTOS;

import enigma.Machine.EnigmaMachine;
import enigma.PlugBoard.PlugBoard;
import enigma.reflector.Reflectors;
import enigma.rotors.RotatingRotor;

import java.util.ArrayList;
import java.util.List;

// TODO: 8/5/2022  menu 2
public class MachineSpecification {


    private final int countOfRotors;
    private final int CountOfRotorsInUse;
    private List<PairOfNotchAndRotorId> NotchAndIds;
    private final int countOfReflectors;
    private int numberOfMessegeEncrypted;
    boolean haveConfigFromUser;
    String RotorsStartingPosition;
    Reflectors.ReflectorEnum chosenReflector;
    boolean isPlugged;
    PlugBoard plugBoard;
    String chosenRotorsWithOrder;



    public MachineSpecification(final EnigmaMachine machineInput) {
        setPairOfNotchAndRotorId(machineInput);
        this.countOfRotors = machineInput.getRotorsObject().getRotorsAmount();
        this.CountOfRotorsInUse = machineInput.getRotorsObject().getRotorsAmountInUse();
        this.countOfReflectors = machineInput.getReflectors().getReflectorsAmount();
        this.numberOfMessegeEncrypted = machineInput.theNumberOfStringsEncrypted;
        this.haveConfigFromUser = machineInput.isConfigFromUser();
        if (haveConfigFromUser) {
            chosenRotorsWithOrder =machineInput.getRotorsObject().toString();
            setRotorsStartingPosition(machineInput);
            this.chosenReflector = machineInput.getReflectors().getChosenReflectorByRomeNumerals();
            this.isPlugged = machineInput.isPluged();
            this.plugBoard = machineInput.getPlugBoard();
        }
    }

    private void setRotorsStartingPosition(final EnigmaMachine machineInput){
        List<RotatingRotor> rotors = machineInput.getRotorsObject().getRotors();
        String rotorsStartingPositions = new String();
        // To display the rotors in a way that matches the way the rotors appear we need to reverse the order of the starting positions
        for (int i = rotors.size() -1; i >=0 ; i--) {
            char position = rotors.get(i).startingPositionCharacter;
            rotorsStartingPositions = rotorsStartingPositions + position;
        }
        this.RotorsStartingPosition = rotorsStartingPositions;
    }
    private void setPairOfNotchAndRotorId(final EnigmaMachine machineInput){
        List<RotatingRotor> rotorsInUse = machineInput.getRotorsObject().getRotors();
        List<PairOfNotchAndRotorId> pairs = new ArrayList<>();
        for (int i = 0; i < rotorsInUse.size(); i++) {
            String rotorId = rotorsInUse.get(i).getId();
            int notch = rotorsInUse.get(i).getNotch();
            pairs.add(new PairOfNotchAndRotorId(rotorId,notch));
        }
        this.NotchAndIds = pairs;
    }
    public PlugBoard getPlugBoard() {
        return plugBoard;
    }
    public int getCountOfRotors() {
        return countOfRotors;
    }
    public int getCountOfRotorsInUse() {
        return CountOfRotorsInUse;
    }
    public List<PairOfNotchAndRotorId> getNotchAndIds() {
        return NotchAndIds;
    }
    public int getCountOfReflectors() {
        return countOfReflectors;
    }
    public int getNumberOfMessegeEncrypted() {
        return numberOfMessegeEncrypted;
    }
    public boolean isHaveConfigFromUser() {
        return haveConfigFromUser;
    }
    public String getRotorsStartingPosition() {
        return RotorsStartingPosition;
    }
    public Reflectors.ReflectorEnum getChosenReflector() {
        return chosenReflector;
    }
    public boolean isPlugged() {
        return isPlugged;
    }
    public String getChosenRotorsWithOrder() {
        return chosenRotorsWithOrder;
    }
}
