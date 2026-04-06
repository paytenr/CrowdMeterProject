package hellofx;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LocationsScreen {
    private Scene scene;

    public LocationsScreen(Stage stage, LoginScreen loginScreen, StatesboroDash statesboroDash) {
        VBox layout = new VBox(20);
        layout.setStyle("-fx-aligment: center-right; -fx-padding: 30;");

        Label title = new Label("Choose your Campus:");
        title.setStyle("-fx-font-size: 16px;");

        Button statesboro = new Button("Statesboro");
        statesboro.setStyle("-fx-font-size: 16px;");
        statesboro.setOnAction(e -> {
            stage.setScene(statesboroDash.getScene());

        });

        layout.getChildren().addAll(title, statesboro);
        scene = new Scene(layout, 400, 600);
    }

    public Scene getScene() {
        return scene;
    }
}