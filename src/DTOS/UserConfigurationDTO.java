package DTOS;

import engine.enigma.Machine.EnigmaMachine;
import engine.enigma.Machine.PairOfNotchAndRotorId;
import engine.enigma.PlugBoard.PlugBoard;
import engine.enigma.reflector.Reflectors;
import engine.enigma.rotors.RotatingRotor;

import java.io.Serializable;
import java.util.List;

public class UserConfigurationDTO implements Serializable {
    private int CountOfRotorsInUse;
    private int numberOfMessageEncrypted;
    private List<String> chosenRotorsWithOrder;
    private String RotorsStartingPosition;
    private boolean isPlugged;

    private PlugBoard plugBoardConnections;
    static boolean haveConfigFromUser = false;
    private Reflectors.ReflectorEnum chosenReflector;

    private List<PairOfNotchAndRotorId> NotchAndIds;

    //constructor for transferring data from ui to engine
    public UserConfigurationDTO(List<String> chosenRotors, String rotorsStartingPosition, String chosenReflector){
        this.chosenRotorsWithOrder = chosenRotors;
        this.RotorsStartingPosition = rotorsStartingPosition;
        this.chosenReflector = Reflectors.ReflectorEnum.valueOf(chosenReflector);
        this.haveConfigFromUser = true;
    }

    //constructor for answer of the engine to ui
    public UserConfigurationDTO(final EnigmaMachine machineInput){
        setPairOfNotchAndRotorId(machineInput.getPairOfNotchAndRotorId());
        this.CountOfRotorsInUse = machineInput.getRotorsObject().getRotorsAmountInUse();
        this.numberOfMessageEncrypted = machineInput.theNumberOfStringsEncrypted;
        // TODO: 8/11/2022 think about chainging toString that return the used to all rotors in object
        setChosenRotorsWithOrder(machineInput);
        setRotorsStartingPosition(machineInput);
        this.isPlugged = machineInput.isPluged();
        this.chosenReflector = machineInput.getReflectorsObject().getChosenReflectorByRomeNumerals();
        this.plugBoardConnections = machineInput.getPlugBoard();
        this.haveConfigFromUser = true;
    }

    // TODO: 8/13/2022 see how to change that we dont need to excess this from the machine
    private void setChosenRotorsWithOrder(final EnigmaMachine machineInput){
        this.chosenRotorsWithOrder = machineInput.getRotorsObject().getChosenRotorsString();

    }
    public void setChosenRotorsWithOrder(List<String> chosenRotors){
        this.chosenRotorsWithOrder = chosenRotors;
    }
    public int getCountOfRotorsInUse() {
        return CountOfRotorsInUse;
    }
    public int getNumberOfMessageEncrypted() {
        return numberOfMessageEncrypted;
    }
    public List<String> getChosenRotorsWithOrder() {
        return chosenRotorsWithOrder;
    }
    private void setRotorsStartingPosition(final EnigmaMachine machineInput){
        List<RotatingRotor> rotors = machineInput.getRotorsObject().getChosenRotors();
        String rotorsStartingPositions = new String();
        // To display the rotors in a way that matches the way the rotors appear we need to reverse the order of the starting positions
        for (int i = rotors.size() -1; i >=0 ; i--) {
            char position = rotors.get(i).getCurrentPositionCharacter();
            rotorsStartingPositions = rotorsStartingPositions + position;
        }
        this.RotorsStartingPosition = rotorsStartingPositions;
    }
    public String getRotorsStartingPosition() {
        return RotorsStartingPosition;
    }
    public boolean isPlugged() {
        return isPlugged;
    }

    public Reflectors.ReflectorEnum getChosenReflector() {
        return chosenReflector;
    }
    public PlugBoard getPlugBoard() {
        return plugBoardConnections;
    }
    public static boolean isHaveConfigFromUser() {
        return haveConfigFromUser;
    }

    public void setPlugBoardConnections(String plugBoardConnections) {
        this.plugBoardConnections = new PlugBoard(plugBoardConnections);
        this.isPlugged = true;
    }
    public String getPlugBoardConnectionsWithFormat(){
        return plugBoardConnections.toString();
    }
    public void setPairOfNotchAndRotorId(List<PairOfNotchAndRotorId> pairs){
        this.NotchAndIds = pairs;
    }
    public List<PairOfNotchAndRotorId> getNotchAndIds() {
        return NotchAndIds;
    }
}
