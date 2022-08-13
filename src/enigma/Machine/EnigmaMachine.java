package enigma.Machine;


import DTOS.MachineSpecificationFromUser;
import DTOS.PairOfNotchAndRotorId;
import enigma.PlugBoard.PlugBoard;
import enigma.keyboard.Keyboard;
import enigma.reflector.Reflector;
import enigma.reflector.Reflectors;
import enigma.rotors.RotatingRotor;
import enigma.rotors.RotatingRotors;
import enigma.rotors.Rotor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EnigmaMachine {
    private RotatingRotors rotors;
    private Reflectors reflectors;
    private PlugBoard plugBoard;
    private boolean havePlugBoard;
    private Keyboard keyboard;

    public static int theNumberOfStringsEncrypted;
    private static int numOfConfigFromUser = 0;

    private MachineSpecificationFromUser firstConfiguration;
    protected final int amountOfRotors;


    public EnigmaMachine(Keyboard keyboardInput, List<RotatingRotor> rotorsInput,int amountOfRotorsToUse, List<Reflector> reflectorsInput) {
        this.keyboard = keyboardInput;
        this.rotors = new RotatingRotors(rotorsInput);
        this.rotors.setRotorsAmountInUse(amountOfRotorsToUse);
        this.amountOfRotors = rotorsInput.size();
        this.reflectors = new Reflectors(reflectorsInput);
        this.havePlugBoard = false;
        this.theNumberOfStringsEncrypted = 0;
    }

    private static void addOneToCountOfDataEncrypted() {
        theNumberOfStringsEncrypted++;
    }

    public String encodeString(String toEncode) {
        addOneToCountOfDataEncrypted();
        String encodeResult = new String();
        for (int i = 0; i < toEncode.length(); i++) {
            encodeResult += encodeChar(toEncode.charAt(i));
        }
        return encodeResult;
    }

    public Character encodeChar(char charToEncode) {
        char currentCharToEncode = charToEncode;
        // TODO: 8/7/2022 change to optianl 
        if (havePlugBoard) {
            if (plugBoard.isPluged(currentCharToEncode))
                currentCharToEncode = plugBoard.getPlugedValue(currentCharToEncode);
        }
        int indexToMap = keyboard.convertAbcToIndex(currentCharToEncode);
        int returnedIndex = encodingUsingRotors(indexToMap);
        char toReturnChar = keyboard.convertIndexToABC(returnedIndex);
        if (havePlugBoard) {
            if (plugBoard.isPluged(toReturnChar))
                toReturnChar = plugBoard.getPlugedValue(toReturnChar);
        }
        return toReturnChar;
    }

    private int encodingUsingRotors(int indexToMap) {
        rotors.advanceRotorsInChain();
        int result;
        int rightToLeftRes;
        int leftToRight;
        int resultReflector;
        // send to rotors from right to left the index
        rightToLeftRes = TransferRightToLeft(indexToMap);
        //reflector
        resultReflector = reflectors.ToReflect(rightToLeftRes);
        // send to rotors from left to right the index receive from reflector
        leftToRight = TransferLeftToRight(resultReflector);
        //rotors
        return leftToRight;
    }

    private int TransferRightToLeft(int index) {
        int indexResRotor = index;
        for (int i = 0; i < rotors.getRotorsAmountInUse(); i++) {
            indexResRotor = rotors.getChosenRotors().get(i).convertRightToLeft(indexResRotor);
        }
        return indexResRotor;
    }

    private int TransferLeftToRight(int index) {
        int indexResRotor = index;
        for (int i = rotors.getRotorsAmountInUse() - 1; i >= 0; i--) {
            indexResRotor = rotors.getChosenRotors().get(i).convertLeftToRight(indexResRotor);
        }
        return indexResRotor;
    }

    public void setPlugBoard(PlugBoard plugBoard) {
        this.plugBoard = plugBoard;
        this.havePlugBoard = true;
    }

    public void setPlugBoard(String connections) {
        setPlugBoard(new PlugBoard(connections));
    }

    public void RemovePlugBoard() {
        this.plugBoard = null;
        this.havePlugBoard = false;
    }

    public boolean isPluged() {
        return havePlugBoard;
    }

    public PlugBoard getPlugBoard() {
        return plugBoard;
    }

    public RotatingRotors getRotorsObject() {
        return rotors;
    }

    public Reflectors getReflectorsObject() {
        return reflectors;
    }

    public void automaticInitialCodeConfiguration() {

        List<String> rotorsId = rotors.getRotors().stream().map(Rotor::toString).collect(Collectors.toList());
        //stream().forEach(RotatingRotor::returnToStartingPosition)
    }

    public void selectInitialCodeConfiguration(MachineSpecificationFromUser specification) {
        getRotorsObject().setChosenRotorToUse(specification.getChosenRotorsWithOrder());
        getRotorsObject().setPositions(specification.getRotorsStartingPosition());
        getReflectorsObject().SetChosenReflector(specification.getChosenReflector());
        specification.setPairOfNotchAndRotorId(getPairOfNotchAndRotorId());
        if (specification.isPlugged())
            setPlugBoard(specification.getPlugBoard());
        if (this.numOfConfigFromUser == 0) {
            saveFirstConfiguration();
        }
        numOfConfigFromUser++;
    }
    //public MachineSpecificationFromUser(List<String> chosenRotors, String rotorsStartingPosition, String chosenReflector)
    private void saveFirstConfiguration(){
        this.firstConfiguration = new MachineSpecificationFromUser(this);
    }




    // TODO: 8/11/2022 make it a single row using stream
    public List<String> getPossibleRotors() {
        return getRotorsObject().getAllRotorsAsStringList();
    }
    public int getAmountPossibleRotors(){
        return rotors.getRotorsAmountInUse();
    }
    public int getAmountOfRotors(){
        return rotors.getRotorsAmount();
    }

    public List<String> getPossibleReflectors(){
        return getReflectorsObject().getPossibleReflectors();
    }
    public List<PairOfNotchAndRotorId> getPairOfNotchAndRotorId(){
        List<RotatingRotor> rotorsInUse = getRotorsObject().getChosenRotors();
        List<PairOfNotchAndRotorId> pairs = new ArrayList<>();
        for (RotatingRotor rotor :rotorsInUse) {
            String rotorId = rotor.getId();
            int notch = rotor.getNotch();
            // TODO: 8/13/2022 check if work
            // getting the distance from notch. every rotor have alphabet from the keyboard
            int distanceFromNotch = (notch - rotor.getPosition() + Keyboard.alphabet.length()) % Keyboard.alphabet.length();
            pairs.add(new PairOfNotchAndRotorId(rotorId,distanceFromNotch));
        }
         Collections.reverse(pairs);
        return pairs;
    }
    public static int getNumOfConfigFromUser() {
        return numOfConfigFromUser;
    }
    public static boolean isFirstConfig(){
        return numOfConfigFromUser == 0;
    }
    public MachineSpecificationFromUser getFirstConfiguration() {
        return firstConfiguration;
    }
}