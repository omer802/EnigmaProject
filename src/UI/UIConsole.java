package UI;

import DTOS.FileConfigurationDTO;
import DTOS.UserConfigurationDTO;
import DTOS.MachineStatisticsDTO;
import DTOS.Validators.xmlFileValidatorDTO;
import engine.enigma.Machine.PairOfNotchAndRotorId;
import engine.api.ApiEnigma;
import engine.api.ApiEnigmaImp;
import engine.enigma.keyboard.Keyboard;
import engine.enigma.reflector.Reflectors;
import engine.enigma.statistics.ConfigurationAndEncryption;
import engine.enigma.statistics.EncryptionData;
import sun.misc.ExtensionInstallationException;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

// TODO: 8/13/2022 figuer out how to cancle so much reverse 
public class UIConsole implements UI {
    public static void main(String[] args) {
        ReadFromMenu();
    }

    // TODO: 8/7/2022 check if members in main class is ok/ change this to member of api or send it to func by reference
    // TODO: 8/12/2022 think about change to only get file and the show to menu
    public static Boolean configByUser;
    public static Boolean ExitWasPressed;
    public static Boolean configFromFile;
    public static Boolean haveFirstConfig;
    public static void ReadFromMenu() {
        ApiEnigma api = new ApiEnigmaImp();
        ExitWasPressed = new Boolean(false);
        configByUser = new Boolean(false);
        haveFirstConfig = new Boolean(false);
        Scanner readInput = new Scanner(System.in);
        while (!ExitWasPressed) {
            menu(configByUser);
            String select = readInput.nextLine();
            if (!select.matches("\\D*\\d\\D*"))
                System.out.println("Wrong input! Only options from the menu can be entered");
            else
                choseOption(api, readInput, select);

        }
    }


    public static void choseOption(ApiEnigma api, Scanner readInput, String select) {
        if (!haveFirstConfig) {
            switch (select) {
                case "1":
                    // TODO: 8/12/2022 change the number to enum
                    readData(api, readInput);
                    break;
                case "2":
                    ExitWasPressed = true;
                    break;
                default:
                        System.out.println("wrong input! You didn't choose an option from the displayed menu");

            }
        }
        else{
            choseOptionAfterFirstConfig(api, readInput, select);
        }

    }
    public static void choseOptionAfterFirstConfig(ApiEnigma api, Scanner readInput, String select){
        switch (select) {
            case "1":
                // TODO: 8/12/2022 change the number to enum
                readData(api, readInput);
                break;
            case "2":
                showData(api, readInput);
                break;
            case "3":
                selectInitialCodeConfiguration(api, readInput);
                break;
            case "4":
                AutomaticallyInitialCodeConfiguration(api, readInput);
                configByUser = true;
                break;
            case "5":
                if (!configByUser)
                    ExitWasPressed = true;
                break;
            ///
            default:
                if (!configByUser)//we not allow the user to enter input different from 1-4, 8
                    System.out.println("wrong input! You didn't choose an option from the displayed menu");
        }
        if (configByUser)
            chosenOptionAfterConfiguration(api, readInput, select);
    }

    public static void chosenOptionAfterConfiguration(ApiEnigma api, Scanner readInput, String select) {
        switch (select) {
            case "5":
                dataEncryption(api, readInput);
                break;
            case "6":
                systemReset(api, readInput);
                break;
            case "7":
                showStatistics(api, readInput);
                break;
            case "8":
                ExitWasPressed = true;
                break;
        }
    }
    public static void showStatistics(ApiEnigma api, Scanner readInput) {
        if (!configFromFile)
            System.out.println("Statistics cannot be displayed when there is no machine in the system");
        else if (!configByUser)
            System.out.println("Statistics cannot be displayed when there is no configuration of the machine");
        else
            PrintStatistic(api, readInput);
    }
    public static void PrintStatistic(ApiEnigma api, Scanner readInput) {

        String pattern = "###,###,###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        MachineStatisticsDTO statisticsToShow = api.getStatistics();
        StringBuilder sb = new StringBuilder();
        sb.append("History and statistics:\n");
        sb.append("the code configurations performed in the system:\n");
        for (ConfigurationAndEncryption configuration: statisticsToShow.getConfigurations())
            sb.append("\t"+getStringDataReceiveFromUser(configuration.getConfiguration())+"\n");
       sb.append("\n");
       if(statisticsToShow.haveEncoded()) {
           sb.append("Configurations used by the machine and the encrypted messages in each configuration:\n");
           for (ConfigurationAndEncryption configuration : statisticsToShow.getConfigurationsInUse()) {
               UserConfigurationDTO config = configuration.getConfiguration();
               sb.append("In configuration:" + getStringDataReceiveFromUser(config) + " The following messages have been encrypted:\n");

               for (EncryptionData data : configuration.getEncryptionDataList()) {
                   String Format = decimalFormat.format(data.getProcessingTime());
                   sb.append("\t#.<" + data.getInput() + ">" + "--> " + "<" + data.getOutput() + ">" + "Encryption time: " + Format + "\n");
               }
           }
       }
       else
           sb.append("The machine has not yet encrypted messages");
        System.out.println(sb);
    }

    public static void systemReset(ApiEnigma api, Scanner readInput) {
        api.systemReset();
        System.out.println("The reset was done successfully");
    }

    public static void dataEncryption(ApiEnigma api, Scanner readInput) {
        String encryptedString;
        boolean goodInput = false;
        boolean tryAgain = true;
        while (!goodInput && tryAgain) {
            System.out.println("Enter massage you want to encrypt");
            String toEncrypt = readInput.nextLine();
            goodInput = validateStringToEncrypt(toEncrypt);
            if (!goodInput) {
                String message = "Some of the letters you entered are not from the alphabet. Please enter a valid string from the alphabet";
                tryAgain = wrongInputMenu(message, readInput);
            } else {
                encryptedString = api.dataEncryption(toEncrypt);
                System.out.println("Message have encrypted. The encryption result is: " + encryptedString);
            }

        }
    }
    //if return true continue else go back to main menu
    public static boolean wrongInputMenu(String wrongInputmessage,Scanner readInput) {
        boolean goodInput = false;
        boolean toReturn = false;
        System.out.println();
        System.out.println("Invalid input. " + wrongInputmessage);
        while (!goodInput) {
            System.out.println("1) Try again");
            System.out.println("2) Return to menu");
            String select = readInput.nextLine();
            switch (select) {
                case "1":
                    toReturn = true;
                    goodInput = true;
                    break;
                case "2":
                    toReturn = false;
                    goodInput = true;
                    break;
                default:
                    System.out.println("Incorrect input. Please select an option from the menu");
            }
        }
        return toReturn;
    }
    public static boolean validateStringToEncrypt(String stringToEncrypt) {
    return Keyboard.isStringInRange(stringToEncrypt);


    }
    public static void readData(ApiEnigma api, Scanner readInput) {
        System.out.println("Please enter the file's full path:");
        String pathString = readInput.nextLine();
        xmlFileValidatorDTO validator = api.readData(pathString);
        if (validator.getListOfExceptions().size() > 0) {
            System.out.println("The following errors occurred while reading the file:\n");
            for (Exception e : validator.getListOfExceptions()) {
                System.out.println(e.getMessage());
            }
            System.out.println("");
        } else {
            System.out.println("The information was read successfully");
            configByUser = false;
            configFromFile = true;
            haveFirstConfig = true;
        }
    }

    public static void selectInitialCodeConfiguration(ApiEnigma api, Scanner readInput) {
        if (configFromFile) {
            UserConfigurationDTO specification;
            System.out.println("set initial configuration");
            try {
                 specification = getDTOConfigurationFromUser(api, readInput);
            }
            catch (RuntimeException e){
                System.out.println(e.getMessage());
                return;
            }
            api.selectInitialCodeConfiguration(specification);
            System.out.println("Your configuration has been successfully saved in the system");
            configByUser = true;
        } else
            System.out.println("Configuration cannot be received from a user without a machine being created");
    }

    // TODO: 8/13/2022 add option to exit every time we want to
    public static UserConfigurationDTO getDTOConfigurationFromUser(ApiEnigma api, Scanner readInput) {
        List<String> chosenRotors = chosenRotorsFromUser(api, readInput);
        String chosenPositions = getRotorsStartingPosition(api, readInput, chosenRotors.size());
        String chosenReflector = getReflector(api, readInput);
        UserConfigurationDTO Specification = new UserConfigurationDTO(chosenRotors, chosenPositions, chosenReflector);
        boolean isPlugged = isPluged(api, readInput);
        if (isPlugged) {
            String plug = getPlug(api, readInput);
            Specification.setPlugBoardConnections(plug);
        }
        return Specification;
    }

    public static boolean isPluged(ApiEnigma api, Scanner readInput) {
        boolean wrongInput = true;
        boolean toReturn = false;
        System.out.println("Want to connect a plug board? press y/n");
        while (wrongInput) {
            String select = readInput.nextLine();
            switch (select){
                case "y":
                    toReturn = true;
                    wrongInput = false;
                    break;
                case "n":
                    return false;

                default:
                    //boolean continueGetPlug = wrongInputMenu("No y or n was Pressed",readInput);
                    //if(!continueGetPlug)
                      //  throw new RuntimeException("The configuration was not saved");
            }
        }
        return false;
    }
    public void menuForInitalConfiguration(Scanner readInput){

    }

    public static String getPlug(ApiEnigma api, Scanner readInput) {
        boolean wrongInput = true;
        System.out.println("select letters pairs from the alphabet for the plug board");
        while (wrongInput) {
            String chosenPlug = readInput.nextLine();
            if ((chosenPlug.length() % 2 == 0) && (Keyboard.isStringInRange(chosenPlug)) && isUniqueCharacters(chosenPlug))
                return chosenPlug;
            else
                System.out.println("invalid input, please letters pairs from the alphabet");
        }
        return null;
    }

    private static boolean isUniqueCharacters(String str) {
        // If at any time we encounter 2 same
        // characters, return false
        for (int i = 0; i < str.length(); i++)
            for (int j = i + 1; j < str.length(); j++)
                if (str.charAt(i) == str.charAt(j))
                    return false;

        // If no duplicate characters encountered,
        // return true
        return true;
    }


    public static String getReflector(ApiEnigma api, Scanner readInput) {
        boolean wrongInput = true;
        String chosenReflector = new String();
        System.out.println("select reflector from " + api.getPossibleReflectors());
        while (wrongInput) {
            chosenReflector = readInput.nextLine();
            try {
                if (api.isReflectorValid(chosenReflector))
                    wrongInput = false;
                else
                    System.out.println("Invalid reflector was chosen. \nPlease select a reflector from the list." +
                            " For example for reflector 1 choose I");
            } catch (Exception e) {
                System.out.println("Invalid reflector was chosen. " + e.getMessage() + "\nPlease select a reflector from the list." +
                        " For example for reflector 1 choose I");
            }
        }
        return Reflectors.IntegerToReflectorString(Integer.parseInt(chosenReflector));
    }

    public static String getRotorsStartingPosition(ApiEnigma api, Scanner readInput, int rotorsAmount) {
        boolean wrongInput = true;
        String rotorsPositionsInput = new String();
        System.out.println("select The initial rotors positions from the alphabet: ");
        while (wrongInput) {
            rotorsPositionsInput = readInput.nextLine();
            if ((api.isRotorsPositionsInRange(rotorsPositionsInput)) && (rotorsPositionsInput.length() == rotorsAmount))
                wrongInput = false;
            else {
                // TODO: 8/13/2022 make option for exit
                System.out.println("Invalid input! Please enter legal positions of the rotors");
            }
        }
        return rotorsPositionsInput;
    }

    public static List<String> chosenRotorsFromUser(ApiEnigma api, Scanner readInput) {
        System.out.println("enter the rotors you would like to use with a comma separating each rotor");
        boolean wrongInput = true;
        String rotorsInput = new String();
        wrongInput = true;
        while (wrongInput) {
            rotorsInput = readInput.nextLine();
            try {
                if (api.isLegalRotors(rotorsInput))
                    wrongInput = false;
            } catch (ExceptionInInitializerError e) {
                // TODO: 8/13/2022 add option to exit from this operation think like expetion
                System.out.println(e.getMessage());
            }
        }
        // TODO: 8/13/2022 think how is incarge of reverse the list for machine
        List<String> chosenRotors = api.cleanStringAndReturnList(rotorsInput);
        Collections.reverse(chosenRotors);
        return chosenRotors;
    }
    public static void AutomaticallyInitialCodeConfiguration(ApiEnigma api, Scanner readInput) {
        if (configFromFile) {
            UserConfigurationDTO machineConfigUser = api.AutomaticallyInitialCodeConfiguration();
            System.out.println(getStringDataReceiveFromUser(machineConfigUser));
        } else {
            System.out.println("It is not possible to create a machine configuration " +
                    "without a machine in the system. Please insert a file that produces the machine");
        }
    }


    // TODO: 8/7/2022  use stringBuilder and add <> in the end
    // TODO: 8/12/2022 לשנות את מיקומי הזיזים ככה שהם המיקום היחסי כרגע  
    public static void showData(ApiEnigma api, Scanner readInput) {
        FileConfigurationDTO machineConfigFile = api.showDataReceivedFromFile();
        System.out.println("Current machine Configurations:");
        System.out.println("the amount of rotors in use/the amount of possible rotors to use is: "
                + machineConfigFile.getCountOfRotorsInUse() + "/" + machineConfigFile.getCountOfRotors());
        System.out.println("Amount of reflectors: " + machineConfigFile.getCountOfReflectors());
        System.out.println("Amount of messages encrypted by the machine: " + machineConfigFile.getNumberOfMessageEncrypted());
        if (machineConfigFile.isConfigFromUser()) {
            UserConfigurationDTO machineConfigUser = api.showDataReceivedFromUser();
            System.out.println("original configuration: ");
            System.out.println(getStringDataReceiveFromUser(api.getCurrentConfiguration()));
            System.out.println("Current configuration: ");
            System.out.println(getStringDataReceiveFromUser(machineConfigUser));
        }

        }

    // TODO: 8/13/2022 add firstConfiguration and now configuration 
    public static StringBuilder getStringDataReceiveFromUser(UserConfigurationDTO machineConfigUser){
        StringBuilder stringBuilder = new StringBuilder();
        List<PairOfNotchAndRotorId> chosenRotors = machineConfigUser.getNotchAndIds();
        getChosenRotorForBuilder(chosenRotors, stringBuilder);
        String startingPositions = machineConfigUser.getRotorsStartingPosition();
        stringBuilder.append("<"+startingPositions+">");
        //getStartingPositionForBuilder(startingPositions,stringBuilder);
        stringBuilder.append("<"+machineConfigUser.getChosenReflector()+">");
        if(machineConfigUser.isPlugged())
            stringBuilder.append("<" + machineConfigUser.getPlugBoardConnectionsWithFormat()+ ">");
        return stringBuilder;
    }
    public static void getChosenRotorForBuilder(List<PairOfNotchAndRotorId> chosenRotors,StringBuilder stringBuilderInput){
        //reverse the order of rotors beacuse in the machine we work from right to left and the ui show from left to right
        //Collections.reverse(chosenRotors);
        boolean isFirst = true;
        stringBuilderInput.append("<");
        for (PairOfNotchAndRotorId pair: chosenRotors) {
            if(!isFirst)
                stringBuilderInput.append(",");
            stringBuilderInput.append(pair.toString());
            isFirst = false;
        }
        stringBuilderInput.append( ">");
    }

    // TODO: 8/6/2022 thinking on what to show when the machine is not initalize
    public static void menu(boolean configByUser) {
        System.out.println("-------------------------------------------------");
        System.out.println("please choose option from the menu:");
        System.out.println("1) Initialize the Machine using a file");
        if(!haveFirstConfig){
            System.out.println("2) Exit");
        }
        else {
            System.out.println("1) Initialize the Machine using a file");
            System.out.println("2) Displaying the machine specifications");
            System.out.println("3) Selecting an initial code configuration (manually)");
            System.out.println("4) Selection of initial code configuration (automatically)");
            if (!configByUser)
                System.out.println("5) Exit");
            else {
                System.out.println("5) encrypt");
                System.out.println("6) Resetting rotors position to pre-encryption position");
                System.out.println("7) History and statistics");
                System.out.println("8) Exit");
            }
        }
        System.out.println("-------------------------------------------------");
    }
}


