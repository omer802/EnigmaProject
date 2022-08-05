package UI;

import engine.api.ApiEnigma;
import engine.api.ApiEnigmaImp;

import java.util.Scanner;

public class UIConsole implements UI {
    public static void main(String[] args) {
        ReadFromMenu();
        ApiEnigma api =  new ApiEnigmaImp();
        boolean ExitWasPressed = false;
        Scanner readInput = new Scanner(System.in);

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
            }

        }
    }
    public static void readData(ApiEnigma api, Scanner readInput){
        System.out.println("please enter a file path");
        String res = readInput.nextLine();
        api.readData(res);


    }
    public static void showData(ApiEnigma api, Scanner readInput) {

    }

        public static void menu(){
        System.out.println("press 1 ");
    }
}
