package hellofx;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StatesboroDash {
    private Scene scene;

    public StatesboroDash(Stage stage){

        VBox layout = new VBox(20);
        layout.setStyle("-fx-aligment: center-right; -fx-padding: 30;");

        Label title = new Label("Hey smirks");
        title.setStyle("-fx-font-size: 16px;");

        layout.getChildren().addAll(title);
        scene = new Scene(layout, 400, 600);




    }

    public Scene getScene() {
       return scene;
    }
}
