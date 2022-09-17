package JavaFX.mainPage;

import engine.api.ApiEnigma;
import engine.api.ApiEnigmaImp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.net.URL;

public class UIJavaFx extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("The Enigma Machine");
        ApiEnigma api = new ApiEnigmaImp();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/JavaFX/mainPage/EnigmaMachine.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane root = fxmlLoader.load(url.openStream());
        MainPageController mainController = fxmlLoader.getController();
        mainController.setApi(api);
        api.setMainController(mainController);
        mainController.setPrimaryStage(primaryStage);
       //Parent load = FXMLLoader.load(getClass().getResource("anigmaWithSplitPaneAnd tabs.fxml"));
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
