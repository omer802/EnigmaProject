package engine.enigma.keyboard;

import java.util.HashMap;
import java.util.Map;

// TODO: 8/6/2022 add to rotors and everybody the keyboard  and work with it - index to char .
// TODO: 8/6/2022 starting with Machine and after that to rotors and reflectors 
public class Keyboard {
    private static Map<Character,Integer> ABCToIndex;
    private static Map<Integer,Character> IndexToABC;
    public static String alphabet;

    public Keyboard(String alphabet)
    {
        this.alphabet = alphabet;
        BuildDictionaries();
    }
    public static int convertAbcToIndex(Character charInput){
        return ABCToIndex.get(charInput);
    }
    public static char convertIndexToABC(Integer intInput){
        return IndexToABC.get(intInput);
    }
    protected static void BuildDictionaries(){
        IndexToABC = new HashMap<>();
        ABCToIndex = new HashMap<>();
        for (int i = 0; i < alphabet.length(); i++) {
            IndexToABC.put(i,alphabet.charAt(i));
            ABCToIndex.put(alphabet.charAt(i),i);
        }
    }
    public static boolean isCharacterInRange(char ch){ return alphabet.contains(Character.toString(ch));}

    public static boolean isStringInRange(String str){
        for (int i = 0; i <str.length() ; i++) {
            if(!isCharacterInRange(str.charAt(i)))
                return false;
        }
        return true;
    }
    
}
