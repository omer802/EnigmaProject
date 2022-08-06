package engima.keyboard;

import java.util.HashMap;
import java.util.Map;

// TODO: 8/6/2022 add to rotors and everybody the keyboard  and work with it - index to char .
// TODO: 8/6/2022 starting with Machine and after that to rotors and reflectors 
public class Keyboard {
    private Map<Character,Integer> ABCToIndex;
    private Map<Integer,Character> IndexToABC;
    private String alphabet;
    
    private int convertAbcToIndex(Character charInput){
        return ABCToIndex.get(charInput);
    }
    private char convertIndexToABC(Integer intInput){
        return IndexToABC.get(intInput);
    }

    protected void BuildDictionaries(){
        IndexToABC = new HashMap<>();
        ABCToIndex = new HashMap<>();
        for (int i = 0; i < alphabet.length(); i++) {
            IndexToABC.put(i,alphabet.charAt(i));
            ABCToIndex.put(alphabet.charAt(i),i);
        }
    }
    
}
