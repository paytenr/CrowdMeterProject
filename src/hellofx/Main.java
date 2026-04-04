package hellofx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        // Create screens
        LoginScreen loginScreen = new LoginScreen(stage);
        StatesboroDash statesboroDash = new StatesboroDash(stage);
        LocationsScreen locationsScreen = new LocationsScreen(stage, loginScreen, statesboroDash);

        //switching screens
        loginScreen.setLocationsScreen(locationsScreen);

        // Show login first
        stage.setScene(loginScreen.getScene());
        stage.setTitle("Login");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}