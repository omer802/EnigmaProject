package engine.enigma.Machine;


import DTOS.UserConfigurationDTO;
import DTOS.MachineStatisticsDTO;
import engine.enigma.PlugBoard.PlugBoard;
import engine.enigma.keyboard.Keyboard;
import engine.enigma.reflector.Reflector;
import engine.enigma.reflector.Reflectors;
import engine.enigma.rotors.RotatingRotor;
import engine.enigma.rotors.RotatingRotors;
import engine.enigma.statistics.Statistics;

import java.io.Serializable;
import java.util.*;

public class EnigmaMachine implements Serializable {
    private RotatingRotors rotors;
    private Reflectors reflectors;
    private PlugBoard plugBoard;
    private boolean havePlugBoard;
    private Keyboard keyboard;

    public static int theNumberOfStringsEncrypted;

    public static int numOfConfiguration;
    private static int numOfConfigFromUser = 0;

    //private UserConfigurationDTO firstConfiguration;
    protected final int amountOfRotors;
    private Statistics statistics;
    static private boolean ConfigFromFile = false;
    static private boolean ConfigFromUser = false;

    public EnigmaMachine(Keyboard keyboardInput, List<RotatingRotor> rotorsInput,int amountOfRotorsToUse, List<Reflector> reflectorsInput) {
        this.keyboard = keyboardInput;
        this.rotors = new RotatingRotors(rotorsInput);
        this.rotors.setRotorsAmountInUse(amountOfRotorsToUse);
        this.amountOfRotors = rotorsInput.size();
        this.reflectors = new Reflectors(reflectorsInput);
        this.havePlugBoard = false;
        this.theNumberOfStringsEncrypted = 0;
        this.statistics = new Statistics();
        ConfigFromFile = true;
        ConfigFromUser = false;
    }

    private static void addOneToCountOfDataEncrypted() {
        theNumberOfStringsEncrypted++;
    }

    public String encodeString(String toEncode) {
        long startTime = System.nanoTime();
        addOneToCountOfDataEncrypted();
        String encodeResult = new String();
        for (int i = 0; i < toEncode.length(); i++) {
            encodeResult += encodeChar(toEncode.charAt(i));
        }
        long timeToEncode = System.nanoTime() - startTime;
        statistics.addEncryptionToStatistics(toEncode,encodeResult,timeToEncode);
        return encodeResult;
    }

    public Character encodeChar(char charToEncode) {
        char currentCharToEncode = charToEncode;
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

    /*here we create dto object and send it to the method selectInitialCodeConfiguration
     in the machine*/
    //הגרלת רשימת סטרינגים של
    public void automaticInitialCodeConfiguration() {
        List<String> chosenRotors = generateRotorsConfigurationRandomly();
        String chosenPositions =  generatePositionsConfigurationRandomly();
        String chosenReflector = generateChosenReflectorConfigurationRandomly();
        UserConfigurationDTO configuration = new UserConfigurationDTO(chosenRotors,chosenPositions,chosenReflector);
        Random rd = new Random();
        int isPlug = generateRandomIndexInList(2);
        if(isPlug==1){
            String plug = generatePlugBoardConnectionsRandomly();
            configuration.setPlugBoardConnections(plug);
        }
        selectInitialCodeConfiguration(configuration);
    }

    private String generatePlugBoardConnectionsRandomly(){
        //first getting the size of the plugBoard
        int keyboardSize = Keyboard.alphabet.length();
        int RandomSize = generateRandomIndexInList(keyboardSize/2);
        while(RandomSize < 1 )
            RandomSize = generateRandomIndexInList(keyboardSize/2);
        int counter = 0;
        Set<Character> allreadyExistChar = new HashSet<>();
        String plugBoardConnections = new String();

        while(counter<RandomSize){
            Character indexChar1 = Keyboard.alphabet.charAt(generateRandomIndexInList(keyboardSize));
            Character indexChar2 = Keyboard.alphabet.charAt(generateRandomIndexInList(keyboardSize));
            if((!allreadyExistChar.contains(indexChar1))&&(!allreadyExistChar.contains(indexChar2))
                && indexChar1 !=indexChar2){
                plugBoardConnections+= indexChar1;
                plugBoardConnections+= indexChar2;
                allreadyExistChar.add(indexChar1);
                allreadyExistChar.add(indexChar2);
                counter++;
            }
        }
        return plugBoardConnections;
    }

    private String generateChosenReflectorConfigurationRandomly() {
        int randomIndex = generateRandomIndexInList(getPossibleReflectors().size());
        String chosenReflector = getPossibleReflectors().get(randomIndex);
        return chosenReflector;
    }
    private String generatePositionsConfigurationRandomly() {
        String chosenPositions = new String();
        for (int i = 0; i < getRotorsAmountInUse(); i++) {
            int randomIndex = generateRandomIndexInList(Keyboard.alphabet.length());
            //take random character from alphabet and add to position
            Character randomPosition = Keyboard.alphabet.charAt(randomIndex);
            chosenPositions+= randomPosition;
        }
        return chosenPositions;
    }
    private int generateRandomIndexInList(int range){
        Random r = new Random();
        int RandomItem = r.nextInt(range);
        return RandomItem;
    }

    private List<String> generateRotorsConfigurationRandomly(){
        List<String> chosenRotors = new ArrayList<>();
        int counter = 0;
        while(counter < getRotorsAmountInUse()) {
            int randomIndex = generateRandomIndexInList(this.getAmountOfRotors());
            String randomRotor = getPossibleRotors().get(randomIndex);
            if(!chosenRotors.contains(randomRotor)){
                chosenRotors.add(randomRotor);
                counter++;
            }
        }
        return chosenRotors;
    }

    public void selectInitialCodeConfiguration(UserConfigurationDTO specification) {
        getRotorsObject().setChosenRotorToUse(specification.getChosenRotorsWithOrder());
        getRotorsObject().setPositions(specification.getRotorsStartingPosition());
        getReflectorsObject().SetChosenReflector(specification.getChosenReflector());
        specification.setPairOfNotchAndRotorId(getPairOfNotchAndRotorId());
        if (specification.isPlugged())
            setPlugBoard(specification.getPlugBoard());
        addConfiguration();
        ConfigFromUser = true;
    }
    //public MachineSpecificationFromUser(List<String> chosenRotors, String rotorsStartingPosition, String chosenReflector)
    private void addConfiguration(){
        UserConfigurationDTO config = new UserConfigurationDTO(this);
        statistics.addConfiguration(config);
    }




    public List<String> getPossibleRotors() {
        return getRotorsObject().getAllRotorsAsStringList();
    }
    public int getRotorsAmountInUse(){
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
            // getting the distance from notch. every rotor have alphabet from the keyboard
            int distanceFromNotch = (notch - rotor.getPosition() + Keyboard.alphabet.length()) % Keyboard.alphabet.length();
            pairs.add(new PairOfNotchAndRotorId(rotorId,distanceFromNotch));
        }
         Collections.reverse(pairs);
        return pairs;
    }
    public UserConfigurationDTO getCurrentConfiguration() {
        return statistics.getCurrentConfiguration();
    }
    public MachineStatisticsDTO getStatistics() {
        return new MachineStatisticsDTO(statistics);
    }
    public static boolean isConfigFromUser() {
        return ConfigFromUser;
    }
    public static boolean isConfigFromFile() {
        return ConfigFromFile;
    }

}