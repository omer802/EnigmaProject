package engine.decryptionManager.dictionary;

import DTOS.Validators.xmlFileValidatorDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Dictionary {

    static public Set<String> getWordDictionary() {
        return wordDictionary;
    }
    static public boolean isWordsInDictionary(List<String> words){
        for (String word:words) {
            if(!wordDictionary.contains(word))
                return false;
        }
        return true;
    }


    static private Set<String> wordDictionary;
    private String excludeChars;

    public Dictionary(String words, String excludeChars) {
        this.excludeChars = excludeChars;
        createWordDictionary(words, excludeChars);
    }

    private void createWordDictionary(String words, String excludeChars) {
        String wordToClean = cleanString(words,excludeChars);
        List<String> wordList = Arrays.asList(wordToClean.split(" "));
        this.wordDictionary = wordList.stream().collect(Collectors.toSet());

    }
    private String cleanString(String words,String excludeChars) {
        String wordToClean = new String(words);
        wordToClean = wordToClean.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
        wordToClean = wordToClean.replaceAll("\\p{C}", "");
        wordToClean = wordToClean.trim();
        wordToClean = wordToClean.toUpperCase();
        //delete all exclude chars from string
        for (int i = 0; i <excludeChars.length() ; i++) {
            wordToClean = wordToClean.replace(String.valueOf(excludeChars.charAt(i)),"");
        }
        return wordToClean;
    }
}
