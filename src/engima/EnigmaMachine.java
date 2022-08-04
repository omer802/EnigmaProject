package engima;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnigmaMachine {

    // TODO: 8/2/2022 change name of enum
    public enum ReflectorEnum {
        I, II, III, IV,V }
    private List<RotatingRotor> rotors;
    //בוחרים reflector אחד
    // private List<reflector> reflectors;
    private List<Reflector> reflectors;
    private int chosenReflector;
    private PlugBoard plugBoard;
    private Map<Character,Integer> ABCToIndex;
    private Map<Integer,Character> IndexToABC;
    private String alphabet;
    private boolean havePlugBoard;

    private final int rotorsCount;

    public EnigmaMachine(String alphabetInput, List<RotatingRotor> rotorsInput, List<Reflector> reflectorsInput) {
        rotorsCount = rotorsInput.size();
        alphabet = alphabetInput;
        rotors = rotorsInput;
        reflectors = reflectorsInput;
        BuildDictionaries();
    }

    private void addPlugBoard(PlugBoard plugBoardInput) {
        plugBoard = plugBoardInput;
    }

    // TODO: 8/2/2022 change to better translate
    public void SetChosenReflector(ReflectorEnum chosenReflector) {
        switch(chosenReflector){
            case I: this.chosenReflector = 0;
            case II: this.chosenReflector = 1;
            case III: this.chosenReflector = 2;
            case IV: this.chosenReflector = 3;
            case V: this.chosenReflector = 4;
        }
    }

    private int convertAbcToIndex(Character charInput){
        return ABCToIndex.get(charInput);
    }
    private char convertIndexToABC(Integer intInput){
        return IndexToABC.get(intInput);
    }// protected??
    protected void BuildDictionaries(){
        IndexToABC = new HashMap<>();
        ABCToIndex = new HashMap<>();
        for (int i = 0; i < alphabet.length(); i++) {
            IndexToABC.put(i,alphabet.charAt(i));
            ABCToIndex.put(alphabet.charAt(i),i);
        }
    }
    public String encodeString(String toEncode){
        String encodeResult = new String();
        for (int i = 0; i <toEncode.length() ; i++) {
            encodeResult += encodeChar(toEncode.charAt(i));
        }
        return encodeResult;
    }
    public Character encodeChar(char charToEncode){
        char currentCharToEncode = charToEncode;
        if(havePlugBoard) {
            if (plugBoard.isPluged(currentCharToEncode))
                currentCharToEncode = plugBoard.getPlugedValue(currentCharToEncode);
        }
        int indexToMap = ABCToIndex.get(currentCharToEncode);
        int returnedIndex = encodingUsingRotors(indexToMap);
        char toReturnChar = convertIndexToABC(returnedIndex);
        if(havePlugBoard) {
            if (plugBoard.isPluged(toReturnChar))
                toReturnChar = plugBoard.getPlugedValue(toReturnChar);
        }
        return toReturnChar;
    }
    private int encodingUsingRotors(int indexToMap){
        rotateRotors();
        int result;
        int rightToLeftRes;
        int leftToRight;
        int resultReflector;
        // send to rotors the inedx
        rightToLeftRes = TransferRightToLeft(indexToMap);
        //reflector
        resultReflector = reflectors.get(chosenReflector).reflect(rightToLeftRes);

        leftToRight = TransferLeftToRight(resultReflector);

        //rotors
        return  leftToRight;

    }
    private int TransferRightToLeft(int index){
        int indexResRotor = index;
        for(int i = 0;i<rotors.size();i++) {
            indexResRotor = rotors.get(i).convertRightToLeft(indexResRotor);
        }
        return indexResRotor;
    }
    private int TransferLeftToRight(int index){
        int indexResRotor = index;
        for(int i = rotors.size()-1;i>=0;i--){
            indexResRotor = rotors.get(i).convertLeftToRight(indexResRotor);
        }
        return indexResRotor;
    }
    private void rotateRotors()
    {
        rotors.get(0).rotate();
        for (int i = 0; i < rotors.size() -1 ; i++) {
            if(rotors.get(i).checkNotch())
                rotors.get(i+1).rotate();
            else//לבדוק אם לעשות break
                break;



        }



    }



}
