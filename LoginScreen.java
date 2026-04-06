package hellofx;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen {
    private Scene scene;
    private LocationsScreen locationsScreen;
    private Stage stage;

    public LoginScreen(Stage stage) {
        this.stage = stage;

        VBox layout = new VBox(15);
        layout.setStyle("-fx-alignment: center; -fx-padding: 30;");

        Label label = new Label("Login");
        label.setStyle("-fx-font-size: 24px;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(200);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(200);

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-font-size: 16px;");

        // Login action
        loginButton.setOnAction(e -> handleLogin(usernameField, passwordField));

        // Enter key triggers login
        passwordField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                handleLogin(usernameField, passwordField);
            }
        });

        layout.getChildren().addAll(label, usernameField, passwordField, loginButton);
        scene = new Scene(layout, 400, 300);
    }

    public void setLocationsScreen(LocationsScreen screen) {
        this.locationsScreen = screen;
    }

    private void handleLogin(TextField usernameField, PasswordField passwordField) {
        if (usernameField.getText().equals("admin") && passwordField.getText().equals("1234")) {
            stage.setScene(locationsScreen.getScene());
            stage.setTitle("Select Your Campus");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Login Failed");
            alert.setContentText("Incorrect username or password.");
            alert.showAndWait();
        }
    }

    public Scene getScene() {
        return scene;
    }
}