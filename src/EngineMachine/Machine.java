package EngineMachine;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Machine {
    private List<RotatingRotor> rotors;
    //בוחרים reflector אחד
    // private List<reflector> reflectors;
    private Reflector reflector;
    private PlugBoard plugBoard;
    private Map<Character,Integer> ABCToIndex;
    private Map<Integer,Character> IndexToABC;
    private String alphabet;
    private boolean havePlugBoard;

    public Machine(String alphabetInput, List<RotatingRotor> rotorsInput, Reflector reflectorInput, PlugBoard plugBoardInput) {
        alphabet = alphabetInput;
        rotors = rotorsInput;
        reflector = reflectorInput;
        BuildDictionaries();
        if(plugBoardInput.pairsInPlug > 0) {
            addPlugBoard(plugBoardInput);
            havePlugBoard = true;
        }
        else
            havePlugBoard = false;
    }

    private void addPlugBoard(PlugBoard plugBoardInput) {
        plugBoard = plugBoardInput;
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
        resultReflector = reflector.reflect(rightToLeftRes);

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
