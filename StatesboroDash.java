package hellofx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

public class StatesboroDash {

    private Scene scene;
    private Label countLabel;

    public StatesboroDash(Stage stage) {

        VBox layout = new VBox(20);
        layout.setStyle("-fx-alignment: center; -fx-padding: 30;");

        Label title = new Label("Statesboro Dashboard");
        title.setStyle("-fx-font-size: 16px;");

        countLabel = new Label("People: 0");
        countLabel.setStyle("-fx-font-size: 14px;");

        layout.getChildren().addAll(title, countLabel);

        scene = new Scene(layout, 400, 600);

        ensureCountFileExists();
        startUpdatingCount();
    }

    // Make sure count.txt exists so we don’t get errors
    private void ensureCountFileExists() {
        try {
            File file = new File("count.txt");
            if (!file.exists()) {
                file.createNewFile();
                try (PrintWriter pw = new PrintWriter(file)) {
                    pw.println("0");  // starting value
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Updates the label every second
    private void startUpdatingCount() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> updateCount())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateCount() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("count.txt"));
            String line = reader.readLine();
            reader.close();

            int count = Integer.parseInt(line);
            countLabel.setText("People: " + count);

        } catch (Exception e) {
            countLabel.setText("Error reading data");
            e.printStackTrace();
        }
    }

    public Scene getScene() {
        return scene;
    }
}

