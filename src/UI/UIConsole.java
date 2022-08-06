package UI;

import engine.api.ApiEnigma;
import engine.api.ApiEnigmaImp;

import java.util.Scanner;

public class UIConsole implements UI {
    public static void main(String[] args) {
        ReadFromMenu();
    }

    public static void ReadFromMenu(){
        ApiEnigma api =  new ApiEnigmaImp();
        boolean ExitWasPressed = false;
        Scanner readInput = new Scanner(System.in);
        while(!ExitWasPressed) {
            menu();
            String select = readInput.nextLine();
            switch (select){
                case "1":
                    readData(api,readInput);
                    break;
                case "2":
                    showData(api,readInput);
                    break;
                case "3":
                    selectInitialCodeConfiguration(api,readInput);
                    break;
                case "4":
                    ///
                    break;
                case "5":
                    dataEncryption(api,readInput);
                    break;

            }

        }
    }
    public static void dataEncryption(ApiEnigma api, Scanner readInput){
        System.out.println("Enter information you want to encrypt");
        String stringToEncrypt = readInput.nextLine();
        String encryptedString = api.dataEncryption(stringToEncrypt);
        System.out.println("data5 have encripted ! the result is: " + encryptedString);

    }
    public static void readData(ApiEnigma api, Scanner readInput){
        System.out.println("Please enter the file's full path:");
        String pathString = readInput.nextLine();
        api.readData(pathString);


    }
    public static void selectInitialCodeConfiguration(ApiEnigma api, Scanner readInput){
        System.out.println("please enter an initial code configuration");
        String Configuration = readInput.nextLine();
        api.selectInitialCodeConfiguration(Configuration);
    }
    public static void showData(ApiEnigma api, Scanner readInput) {

    }

    // TODO: 8/6/2022 thinking on what to show when the machine is not initalize
        public static void menu(){
            System.out.println("please choose option from the menu:");
            System.out.println("1) Initialize the Machine using a file");
            System.out.println("2) Displaying the machine specifications");
            System.out.println("3) Selecting an initial code configuration (manually)");
            System.out.println("4) Selection of initial code configuration (automatically)");
            System.out.println("5) encrypt");
            System.out.println("6) Resetting rotors position to pre-encryption position");
            System.out.println("7) History and statistics");
            System.out.println("8) Exit");
        }
}

