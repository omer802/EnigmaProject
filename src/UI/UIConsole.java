package UI;

import DTOS.MachineSpecification;
import engine.api.ApiEnigma;
import engine.api.ApiEnigmaImp;

import java.util.Scanner;

public class UIConsole implements UI {
    public static void main(String[] args) {
        ReadFromMenu();
    }

    // TODO: 8/7/2022 check if members in main class is ok
    public static Boolean configByUser;
    public static boolean ExitWasPressed;
    public static void ReadFromMenu() {
        ApiEnigma api = new ApiEnigmaImp();
         ExitWasPressed = false;
        configByUser = new Boolean(false);
        Scanner readInput = new Scanner(System.in);
        while (!ExitWasPressed) {
            menu(configByUser);
            String select = readInput.nextLine();
            if (!select.matches("\\D*\\d\\D*"))
                System.out.println("Wrong input! Only numbers between 1-8 can be entered");
            else
                choseOption(api, readInput, select, ExitWasPressed);

        }
    }


    public static void choseOption(ApiEnigma api, Scanner readInput, String select, Boolean ExitWasPressed) {
        switch (select) {
            case "1":
                readData(api, readInput);
                configByUser = false;
                break;
            case "2":
                showData(api, readInput);
                break;
            case "3":
                selectInitialCodeConfiguration(api, readInput);
                configByUser = true;
                break;
            case "4":
                AutomaticallyInitialCodeConfiguration(api, readInput);
                configByUser = true;
                break;
            case "8":
                ExitWasPressed = true;
                ///
                break;
            default:
                if (!configByUser)//we not allow the user to enter input different from 1-4, 8
                    System.out.println("wrong input! You didn't choose an option from the displayed menu");
        }
        if (configByUser)
            chosenOptionAfterConfiguration(api, readInput, select);
    }
    public static void chosenOptionAfterConfiguration(ApiEnigma api, Scanner readInput,String select){
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
        System.out.println("data5 have encripted ! the result is: " + encryptedString);

    }

    public static void readData(ApiEnigma api, Scanner readInput) {
        System.out.println("Please enter the file's full path:");
        String pathString = readInput.nextLine();
        api.readData(pathString);
        System.out.println("The information was read successfully");
    }

    public static void selectInitialCodeConfiguration(ApiEnigma api, Scanner readInput) {
        System.out.println("please enter an initial code configuration");
        String Configuration = readInput.nextLine();
        api.selectInitialCodeConfiguration(Configuration);
        System.out.println("The configuration has been successfully saved in the system");
    }
    public static void AutomaticallyInitialCodeConfiguration(ApiEnigma api, Scanner readInput){

    }

    // TODO: 8/7/2022  use stringBuilder and add <> in the end
    public static void showData(ApiEnigma api, Scanner readInput) {
        MachineSpecification machineSpecification = api.showData();
        System.out.println("The possible number of rotors/the number of rotors in use is: "
                + machineSpecification.getCountOfRotors() + "/" + machineSpecification.getCountOfRotorsInUse());
        System.out.println("Locations of notch in each rotor: " + machineSpecification.getNotchAndIds());
        System.out.println("Number of reflectors:" + machineSpecification.getCountOfReflectors());
        System.out.println("Number of messages encrypted by the machine: " + machineSpecification.getNumberOfMessegeEncrypted());
        if (machineSpecification.isHaveConfigFromUser())
            showConfigurationFromUser(machineSpecification);

    }

    public static void showConfigurationFromUser(MachineSpecification machineSpecification) {
        System.out.println("The configuration received from the user:");
        System.out.println("Rotors in the order they were chosen: " + machineSpecification.getChosenRotorsWithOrder());
        System.out.println("Rotors starting positions: " + machineSpecification.getRotorsStartingPosition());
        System.out.println("selected reflector: " + machineSpecification.getChosenReflector());
        if (machineSpecification.isPlugged()) ;
        System.out.println("plugs: " + machineSpecification.getPlugBoard());
    }

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


