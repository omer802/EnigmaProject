package enigma.Machine;


import enigma.PlugBoard.PlugBoard;
import enigma.keyboard.Keyboard;
import enigma.reflector.Reflector;
import enigma.reflector.Reflectors;
import enigma.rotors.RotatingRotor;
import enigma.rotors.RotatingRotors;

import java.util.List;

public class EnigmaMachine {



    // TODO: 8/2/2022 change name of enum
    /*public enum //ReflectorEnum {
        I, II, III, IV,V }*/
    private RotatingRotors rotors;
    private Reflectors reflectors;
    private PlugBoard plugBoard;
    private boolean havePlugBoard;
    private Keyboard keyboard;

    public static int theNumberOfStringsEncrypted;

    public boolean isConfigFromUser() {
        return ConfigFromUser;
    }

    public void setConfigFromUser() {
        this.ConfigFromUser = true;
    }

    private boolean ConfigFromUser;


    public EnigmaMachine(Keyboard keyboardInput, List<RotatingRotor> rotorsInput, List<Reflector> reflectorsInput) {
        keyboard = keyboardInput;
        rotors = new RotatingRotors(rotorsInput);
        reflectors = new Reflectors(reflectorsInput);
        havePlugBoard = false;
        theNumberOfStringsEncrypted = 0;
        ConfigFromUser = false;
    }



    // TODO: 8/2/2022 change to better translate
    /*public void SetChosenReflector(ReflectorEnum chosenReflector) {
        switch(chosenReflector){
            case I:
                this.chosenReflector = 0;
                break;
            case II:
                this.chosenReflector = 1;
                break;
            case III:
                this.chosenReflector = 2;
                break;
            case IV:
                this.chosenReflector = 3;
                break;
            case V:
                this.chosenReflector = 4;
                break;
        }
    }*/
    private static void addOneToCountOfDataEncrypted(){
        theNumberOfStringsEncrypted++;
    }
    public String encodeString(String toEncode){
        addOneToCountOfDataEncrypted();
        String encodeResult = new String();
        for (int i = 0; i <toEncode.length() ; i++) {
            encodeResult += encodeChar(toEncode.charAt(i));
        }
        return encodeResult;
    }
    public Character encodeChar(char charToEncode){
        char currentCharToEncode = charToEncode;
        // TODO: 8/7/2022 change to optianl 
        if(havePlugBoard) {
            if (plugBoard.isPluged(currentCharToEncode))
                currentCharToEncode = plugBoard.getPlugedValue(currentCharToEncode);
        }
        int indexToMap = keyboard.convertAbcToIndex(currentCharToEncode);
        int returnedIndex = encodingUsingRotors(indexToMap);
        char toReturnChar = keyboard.convertIndexToABC(returnedIndex);
        if(havePlugBoard) {
            if (plugBoard.isPluged(toReturnChar))
                toReturnChar = plugBoard.getPlugedValue(toReturnChar);
        }
        return toReturnChar;
    }
    private int encodingUsingRotors(int indexToMap){
        rotors.advanceRotorsInChain();
        int result;
        int rightToLeftRes;
        int leftToRight;
        int resultReflector;
        // send to rotors the inedx
        rightToLeftRes = TransferRightToLeft(indexToMap);
        //reflector
        resultReflector = reflectors.ToReflect(rightToLeftRes);

        leftToRight = TransferLeftToRight(resultReflector);
        //rotors
        return  leftToRight;
    }
    private int TransferRightToLeft(int index){
        int indexResRotor = index;
        for(int i = 0;i<rotors.getRotorsAmountInUse();i++) {
            indexResRotor = rotors.getChosenRotors().get(i).convertRightToLeft(indexResRotor);
        }
        return indexResRotor;
    }
    private int TransferLeftToRight(int index){
        int indexResRotor = index;
        for(int i = rotors.getRotorsAmountInUse()-1;i>=0;i--){
            indexResRotor =rotors.getChosenRotors().get(i).convertLeftToRight(indexResRotor);
        }
        return indexResRotor;
    }

    public void setPlugBoard(PlugBoard plugBoard) {
        this.plugBoard = plugBoard;
        this.havePlugBoard = true;
    }
    public void setPlugBoard(String connections){
       setPlugBoard(new PlugBoard(connections));
    }
    public void RemovePlugBoard(){
        this.plugBoard = null;
        this.havePlugBoard = false;
    }
    public boolean isPluged(){
        return havePlugBoard;
    }
    public PlugBoard getPlugBoard() {
        return plugBoard;
    }
    public RotatingRotors getRotorsObject() {
        return rotors;
    }
    public Reflectors getReflectors() {
        return reflectors;
    }

}
