package UI.Console;
import DTOS.Configuration.FileConfigurationDTO;
import DTOS.Configuration.UserConfigurationDTO;
import DTOS.Validators.xmlFileValidatorDTO;
import engine.enigma.Machine.NotchAndLetterAtPeekPane;
import engine.api.ApiEnigma;
import engine.api.ApiEnigmaImp;
import engine.enigma.keyboard.Keyboard;
import engine.enigma.reflector.Reflectors;

import java.io.IOException;
import java.util.*;

public class UIConsole  {

    public static void main(String[] args) {
        ReadFromMenu();
    }

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
            if(select.matches("[0-9]+")){
                if(Integer.parseInt(select) >0 && Integer.parseInt(select)<11)
                    choseOption(api, readInput, select);
                else
                    System.out.println("wrong input. please select option from the menu between 1-10");
            }
            else
                System.out.println("wrong input. please insert only numbers between 1-10");
            }
        }



    public static void choseOption(ApiEnigma api, Scanner readInput, String select) {
        if (!haveFirstConfig) {
            switch (select) {
                case "1":
                    readData(api, readInput);
                    break;
                case "2":
                    ExitWasPressed = true;
                    break;
                default:
                        System.out.println("wrong input. please choose an option from the menu");

            }
        }
        else{
            choseOptionAfterFirstConfig(api, readInput, select);
        }

    }
    public static void choseOptionAfterFirstConfig(ApiEnigma api, Scanner readInput, String select){
        switch (select) {
            case "1":
                readData(api, readInput);
                break;
            case "2":
                showData(api, readInput);
                break;
            case "3":
                if(configFromFile) {
                    selectInitialCodeConfiguration(api, readInput);
                }
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
            case "9":
                saveMachineToFile(api,readInput);
                break;
            case "10":
                loadMachineStateFromFile(api,readInput);
                break;
        }
    }
    public static void showStatistics(ApiEnigma api, Scanner readInput) {
        if (!configFromFile)
            System.out.println("Statistics cannot be displayed when there is no machine in the system");
        else if (!configByUser)
            System.out.println("Statistics cannot be displayed when there is no configuration of the machine");
        else
            System.out.println(api.PrintStatistic());
    }
    /*public static StringBuilder PrintStatistic(ApiEnigma api) {
        //System.out.println(api.PrintStatistic());
        string pattern = "###,###,###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        MachineStatisticsDTO statisticsToShow = api.getStatistics();
        StringBuilder sb = new StringBuilder();
        sb.append("History and statistics:\n");
        sb.append("the code configurations performed in the system:\n");
        for (ConfigurationAndEncryption configuration: statisticsToShow.getConfigurations())
            sb.append("\t"+getStringDataReceiveFromUser(api,configuration.getConfiguration())+"\n");
       sb.append("\n");
       if(statisticsToShow.haveEncoded()) {
           sb.append("Configurations used by the machine and the encrypted messages in each configuration:\n");
           for (ConfigurationAndEncryption configuration : statisticsToShow.getConfigurationsInUse()) {
               UserConfigurationDTO config = configuration.getConfiguration();
               sb.append("In configuration:" + getStringDataReceiveFromUser(api,config) + " The following messages have been encrypted: \n");

               for (EncryptionData data : configuration.getEncryptionDataList()) {
                   String Format = decimalFormat.format(data.getProcessingTime());
                   sb.append("\t#.<" + data.getInput().toUpperCase() + ">" + "--> " + "<" + data.getOutput().toUpperCase() + ">" + " Encryption time in nano seconds: " + Format + "\n");
               }
           }
       }

       else
           sb.append("The machine has not yet encrypted messages");
        return sb;
    }*/

    public static void systemReset(ApiEnigma api, Scanner readInput) {
        api.resetPositions();
        System.out.println("The reset was done successfully");
    }

    public static void dataEncryption(ApiEnigma api, Scanner readInput) {
        String encryptedString;
        boolean goodInput = false;
        boolean ExitWasPress = false;
        while (!goodInput && (!ExitWasPress)) {
            System.out.println("Enter massage you want to encrypt");
            String toEncrypt = readInput.nextLine();
            toEncrypt = toEncrypt.toUpperCase();
            // TODO: 8/23/2022 move validatestring to api
            goodInput = api.validateStringToEncrypt(toEncrypt);
            if (!goodInput) {
                System.out.println("Some of the letters you entered are not from the alphabet");;
                ExitWasPress = ExitToMenu(readInput);
            } else {
                encryptedString = api.dataEncryption(toEncrypt);
                System.out.println("Message have encrypted. The encryption result is: " + encryptedString.toUpperCase());
            }

        }
    }
    //if return true continue else go back to main menu
    public static boolean ExitToMenu(Scanner readInput) {
        boolean goodInput = false;
        boolean toReturn = false;
        System.out.println();
        while (!goodInput) {
            System.out.println("1) Try again");
            System.out.println("2) Return to menu");
            String select = readInput.nextLine();
            switch (select) {
                case "1":
                    toReturn = false;
                    goodInput = true;
                    break;
                case "2":
                    toReturn = true;
                    goodInput = true;
                    break;
                default:
                    System.out.println("Incorrect input. Please select an option from the menu");
            }
        }
        return toReturn;
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
            System.out.println("The file was read successfully");
            configByUser = false;
            configFromFile = true;
            haveFirstConfig = true;
        }
    }

    public static void selectInitialCodeConfiguration(ApiEnigma api, Scanner readInput) {
        UserConfigurationDTO specification;
        System.out.println("set initial configuration");
        specification = getDTOConfigurationFromUser(api, readInput);
        if (specification == null) {
            System.out.println("The configuration was not saved. ");
            return;
        } else {
            api.selectInitialCodeConfiguration(specification);
            System.out.println("Your configuration has been successfully saved in the system");
            configByUser = true;
        }
    }


    public static UserConfigurationDTO getDTOConfigurationFromUser(ApiEnigma api, Scanner readInput) {
        List<String> chosenRotors = chosenRotorsFromUser(api, readInput);
        if (chosenRotors == null)
            return null;
        else
            System.out.println("Rotors id updated successfully");
        String chosenPositions = getRotorsStartingPosition(api, readInput, chosenRotors.size());
        if (chosenPositions == null)
            return null;
        else
            System.out.println("rotors starting positions updated successfully ");
        String chosenReflector = getReflector(api, readInput);
        if (chosenReflector == null)
            return null;
        else
            System.out.println("Reflector id updated successfully");

        UserConfigurationDTO Specification = new UserConfigurationDTO(chosenRotors, chosenPositions, chosenReflector);
        int isPlugged = isPluged(api, readInput);
        if (isPlugged == -1)
            return null;
        else if (isPlugged == 1) {
            String plug = getPlug(api, readInput);
            if(plug == null) {
                System.out.println("Plug board was not set");
                return null;
            }
            else {
                Specification.setPlugBoardConnections(plug);
                System.out.println("Plug board updated successfully");
            }
        } else if (isPlugged == 0)
            System.out.println("Plug board was not set");

            return Specification;
    }

    public static int isPluged(ApiEnigma api, Scanner readInput) {
        boolean wrongInput = true;
        int toReturn = 0;
        while (wrongInput) {
            System.out.println("Want to connect a plug board? press y/n");
            String select = readInput.nextLine();
            switch (select){
                case "y":
                    toReturn = 1;
                    wrongInput = false;
                    break;
                case "n":
                    toReturn = 0;
                    wrongInput = false;
                    break;
                default:
                    System.out.println("No y or n was entered");
                    if(ExitToMenu(readInput))
                       return -1;
            }
        }
        return toReturn;
    }
    public static String getPlug(ApiEnigma api, Scanner readInput) {
        boolean wrongInput = true;
        while (wrongInput) {
            System.out.println("select letters pairs from the alphabet for the plug board");
            String chosenPlug = readInput.nextLine();
            chosenPlug = chosenPlug.toUpperCase();
            if (chosenPlug.length() % 2 != 0)
                System.out.println("Error: only an even amount of characters can be entered");
            if(!Keyboard.isStringInRange(chosenPlug)){
                System.out.println("Error: one of the letters entered is not in the alphabet");
            }
            if(!isUniqueCharacters(chosenPlug))
            {
                System.out.println("Error: Not all letters are different");
            }
            if((chosenPlug.length() % 2 == 0) && (Keyboard.isStringInRange(chosenPlug)) && isUniqueCharacters(chosenPlug))
                return chosenPlug;
            if(ExitToMenu(readInput))
                return null;

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
        while (wrongInput) {
            System.out.println("select reflector from: " + api.getPossibleReflectors());

            chosenReflector = readInput.nextLine();
            List<String> errorsInReflectorInput = api.isReflectorValid(chosenReflector);
            if (errorsInReflectorInput.size() > 0) {
                errorsInReflectorInput.forEach(System.out::println);
                if (ExitToMenu(readInput)) {
                    return null;
                }
            }
            else {
                wrongInput = false;
            }
        }
        return Reflectors.IntegerToReflectorString(Integer.parseInt(chosenReflector));
    }

    public static String getRotorsStartingPosition(ApiEnigma api, Scanner readInput, int rotorsAmount) {
        boolean wrongInput = true;
        String rotorsPositionsInput = new String();
        while (wrongInput) {
            System.out.println("select " +api.getAmountOfRotors()+" positions from the alphabet:");
            rotorsPositionsInput = readInput.nextLine();
            rotorsPositionsInput = rotorsPositionsInput.toUpperCase();
            if(!api.isRotorsPositionsInRange(rotorsPositionsInput))
                System.out.println("Error: a letter that is not in the alphabet was entered");
            if(rotorsPositionsInput.length() != rotorsAmount)
            System.out.println("Error: The amount of positions entered does not match the amount of rotors in the system. you " +
                    "need to enter " +api.getAmountOfRotors()+ " positions from the alphabet");
            if ((api.isRotorsPositionsInRange(rotorsPositionsInput)) && (rotorsPositionsInput.length() == rotorsAmount))
                wrongInput = false;
            else {
                if(ExitToMenu(readInput))
                    return null;
            }
        }
        return rotorsPositionsInput;
    }

    public static List<String> chosenRotorsFromUser(ApiEnigma api, Scanner readInput) {
        boolean wrongInput = true;
        String rotorsInput = new String();
        wrongInput = true;
        while (wrongInput) {
            System.out.println("enter "+ api.getAmountOfRotors() +" rotors you would like to use with a comma separating each rotor from: " +api.getPossibleRotors());
            rotorsInput = readInput.nextLine();
            List<String> ErrorsInRotorsInput = api.isLegalRotors(rotorsInput);
            if (ErrorsInRotorsInput.size() == 0)
                wrongInput = false;
            else {
                ErrorsInRotorsInput.forEach(System.out::println);
                if (ExitToMenu(readInput))
                    return null;
            }
        }
            List<String> chosenRotors = api.cleanStringAndReturnList(rotorsInput);
            Collections.reverse(chosenRotors);
            return chosenRotors;

    }
    public static void AutomaticallyInitialCodeConfiguration(ApiEnigma api, Scanner readInput) {
        if (configFromFile) {
            UserConfigurationDTO machineConfigUser = api.AutomaticallyInitialCodeConfiguration();
            System.out.println("configuration has been successfully saved in the system");
            System.out.println("Current machine Configurations: "+ getStringDataReceiveFromUser(api,machineConfigUser));
        } else {
            System.out.println("It is not possible to create a machine configuration " +
                    "without a machine in the system. Please insert a file that produces the machine");
        }
    }


    public static void showData(ApiEnigma api, Scanner readInput) {
        FileConfigurationDTO machineConfigFile = api.showDataReceivedFromFile();
        System.out.println("Current machine Configurations:");
        System.out.println("the amount of rotors in use/the amount of possible rotors to use is: "
                + machineConfigFile.getCountOfRotorsInUse() + "/" + machineConfigFile.getCountOfRotors());
        System.out.println("Amount of reflectors: " + machineConfigFile.getCountOfReflectors());
        System.out.println("Amount of messages encrypted by the machine: " + machineConfigFile.getNumberOfMessageEncrypted());
        if (machineConfigFile.isConfigFromUser()) {
            System.out.println("original configuration: ");
            System.out.println(getStringDataReceiveFromUser(api,api.getOriginalConfiguration()));
            System.out.println("Current configuration: ");
            UserConfigurationDTO machineConfigUser = api.getCurrentConfiguration();
            System.out.println(getStringDataReceiveFromUser(api, machineConfigUser));
        }

        }

    public static StringBuilder getStringDataReceiveFromUser(ApiEnigma api, UserConfigurationDTO machineConfigUser){
        return api.getStringDataReceiveFromUser(machineConfigUser);
    }
    public static void getChosenRotorsWithOrder( List<String> rotorsWithOrder, StringBuilder stringBuilderInput){
        boolean isFirst = true;
        stringBuilderInput.append("<");
        for (String rotorID: rotorsWithOrder) {
            if(!isFirst)
                stringBuilderInput.append(",");
            stringBuilderInput.append(rotorID);
            isFirst = false;
        }
        stringBuilderInput.append(">");
    }
    public static void getCurrentPositionsWithDistanceFromNotch(List<NotchAndLetterAtPeekPane> chosenRotors, StringBuilder stringBuilderInput){
        //reverse the order of rotors beacuse in the machine we work from right to left and the ui show from left to right
        //Collections.reverse(chosenRotors);
        boolean isFirst = true;
        stringBuilderInput.append("<");
        for (NotchAndLetterAtPeekPane pair: chosenRotors)
            stringBuilderInput.append(pair.toString());
        stringBuilderInput.append( ">");
    }

    public static void menu(boolean configByUser) {
        System.out.println("-------------------------------------------------");
        System.out.println("please choose option from the menu:");
        if(!haveFirstConfig){
            System.out.println("1) Initialize the Machine using a file");
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
                System.out.println("9) Save current machine state to file");
                System.out.println("10) Load current machine state from file");
            }
        }
        System.out.println("-------------------------------------------------");
    }
    public static void saveMachineToFile(ApiEnigma api, Scanner readInput) {
        System.out.println("Please enter full path of the file without suffix ");
        String path = readInput.nextLine();
        try{
            api.saveMachineStateToFile(path);
            System.out.println("The current machine state was saved successfully");
        }
        catch(IOException e){
            System.out.println("Error: cannot write to file");

        }
    }
    public static void loadMachineStateFromFile(ApiEnigma api, Scanner readInput){
        System.out.println("please enter the full path of the file you want to reload");
        String path = readInput.nextLine();
        try{
            api.loadMachineStateFromFIle(path);
            System.out.println("the machine state loaded successfully from file");
        }
         catch (ClassNotFoundException e ) {
             System.out.println("Error: cannot load data from a file. please check the file and the path of the file you have insert");
        } catch (IOException e) {
            System.out.println("Error: cannot load data from a file. please check the file and the path of the file you have insert");
        }
    }
}


