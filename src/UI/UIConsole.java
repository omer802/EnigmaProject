package UI;

import DTOS.MachineSpecificationFromFile;
import DTOS.MachineSpecificationFromUser;
import DTOS.PairOfNotchAndRotorId;
import com.sun.javafx.binding.StringFormatter;
import engine.api.ApiEnigma;
import engine.api.ApiEnigmaImp;
import enigma.keyboard.Keyboard;
import enigma.reflector.Reflectors;

import java.util.*;
import java.util.stream.Collectors;

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
    public static void ReadFromMenu() {
        ApiEnigma api = new ApiEnigmaImp();
        ExitWasPressed = new Boolean(false);
        configByUser = new Boolean(false);
        Scanner readInput = new Scanner(System.in);
        while (!ExitWasPressed) {
            menu(configByUser);
            String select = readInput.nextLine();
            if (!select.matches("\\D*\\d\\D*"))
                System.out.println("Wrong input! Only numbers between 1-8 can be entered");
            else
                choseOption(api, readInput, select);

        }
    }

    public static void choseOption(ApiEnigma api, Scanner readInput, String select) {
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
                //;
                break;
            case "8":
                ExitWasPressed = true;
                break;
        }
    }

    public static void systemReset(ApiEnigma api, Scanner readInput) {
        api.systemReset();
        System.out.println("The reset was done successfully");
    }

    public static void dataEncryption(ApiEnigma api, Scanner readInput) {
        System.out.println("Enter information you want to encrypt");
        String stringToEncrypt = readInput.nextLine();
        String encryptedString = api.dataEncryption(stringToEncrypt);
        System.out.println("data have encripted. the string result is: " + encryptedString);

    }

    public static void readData(ApiEnigma api, Scanner readInput) {
        System.out.println("Please enter the file's full path:");
        String pathString = readInput.nextLine();
        api.readData(pathString);
        System.out.println("The information was read successfully");
        configByUser = false;
        configFromFile = true;
    }

    public static void selectInitialCodeConfiguration(ApiEnigma api, Scanner readInput) {
        if (configFromFile) {
            System.out.println("set initial configuration");
            MachineSpecificationFromUser specification = getDTOConfigurationFromUser(api, readInput);
            api.selectInitialCodeConfiguration(specification);
            System.out.println("Your configuration has been successfully saved in the system");
            configByUser = true;
        } else
            System.out.println("Configuration cannot be received from a user without a machine being created");
    }

    // TODO: 8/13/2022 add option to exit every time we want to
    public static MachineSpecificationFromUser getDTOConfigurationFromUser(ApiEnigma api, Scanner readInput) {
        List<String> chosenRotors = chosenRotorsFromUser(api, readInput);
        String chosenPositions = getRotorsStartingPosition(api, readInput, chosenRotors.size());
        String chosenReflector = getReflector(api, readInput);
        MachineSpecificationFromUser Specification = new MachineSpecificationFromUser(chosenRotors, chosenPositions, chosenReflector);
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
            String isPluged = readInput.nextLine();
            if (isPluged.equals("y")) {
                toReturn = true;
                wrongInput = false;
            } else if (!isPluged.equals("n")) {
                System.out.println("invalid input. please press y/n");
            } else if (isPluged.equals("n")) {
                toReturn = false;
                wrongInput = false;
            }
        }
        return toReturn;
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
            MachineSpecificationFromUser machineConfigUser = api.AutomaticallyInitialCodeConfiguration();
            ShowDataReceiveFromUser(machineConfigUser);
        } else {
            System.out.println("It is not possible to create a machine configuration " +
                    "without a machine in the system. Please insert a file that produces the machine");
        }
    }


    // TODO: 8/7/2022  use stringBuilder and add <> in the end
    // TODO: 8/12/2022 לשנות את מיקומי הזיזים ככה שהם המיקום היחסי כרגע  
    public static void showData(ApiEnigma api, Scanner readInput) {
        MachineSpecificationFromFile machineConfigFile = api.showDataReceivedFromFile();
        MachineSpecificationFromUser machineConfigUser = api.showDataReceivedFromUser();
        System.out.println("The possible number of rotors/the number of rotors in use is: "
                + machineConfigFile.getCountOfRotors() + "/" + machineConfigFile.getCountOfRotorsInUse());
        System.out.println("Number of reflectors:" + machineConfigFile.getCountOfReflectors());
        System.out.println("Number of messages encrypted by the machine: " + machineConfigUser.getNumberOfMessageEncrypted());
        if (machineConfigUser.isHaveConfigFromUser()) {
            if (api.isConfigFromUser()) {
                System.out.println("original configuration: ");
                ShowDataReceiveFromUser(api.getFirstConfig());
                System.out.println("Current configuration: ");
                ShowDataReceiveFromUser(machineConfigUser);
            }

        }

    }


    // TODO: 8/13/2022 add firstConfiguration and now configuration 
    public static void ShowDataReceiveFromUser(MachineSpecificationFromUser machineConfigUser){
        StringBuilder stringBuilder = new StringBuilder();
        List<PairOfNotchAndRotorId> chosenRotors = machineConfigUser.getNotchAndIds();
        getChosenRotorForBuilder(chosenRotors, stringBuilder);
        String startingPositions = machineConfigUser.getRotorsStartingPosition();
        stringBuilder.append("<"+startingPositions+">");
        //getStartingPositionForBuilder(startingPositions,stringBuilder);
        stringBuilder.append("<"+machineConfigUser.getChosenReflector()+">");
        if(machineConfigUser.isPlugged())
            stringBuilder.append("<" + machineConfigUser.getPlugBoardConnectionsWithFormat()+ ">");
        System.out.println(stringBuilder);
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

  /*  public static void showConfigurationFromUser(MachineSpecificationFromUser machineSpecification) {
        System.out.println("The configuration received from the user:");
        System.out.println("Rotors in the order they were chosen: " + machineSpecification.getChosenRotorsWithOrder());
        System.out.println("Rotors starting positions: " + machineSpecification.getRotorsStartingPosition());
        System.out.println("selected reflector: " + machineSpecification.getChosenReflector());
        if (machineSpecification.isPlugged())
        System.out.println("plugs: " + machineSpecification.getPlugBoard());
    }*/

    // TODO: 8/6/2022 thinking on what to show when the machine is not initalize
    public static void menu(boolean configByUser) {
        System.out.println("please choose option from the menu:");
        System.out.println("1) Initialize the Machine using a file");
        System.out.println("2) Displaying the machine specifications");
        System.out.println("3) Selecting an initial code configuration (manually)");
        System.out.println("4) Selection of initial code configuration (automatically)");
        if(!configByUser)
            System.out.println("5) Exit");
        else{
            System.out.println("5) encrypt");
            System.out.println("6) Resetting rotors position to pre-encryption position");
            System.out.println("7) History and statistics");
            System.out.println("8) Exit");
        }

    }
}


