package ch14;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PythonConnectorGUI extends Application {

    private Label totalLabel = new Label("Total detections: 0");

    @Override
    public void start(Stage primaryStage) {
        Button simulateButton = new Button("Simulate Person");

        simulateButton.setOnAction(e -> {
            try {
                URL url = new URL("http://127.0.0.1:8000/simulate");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                // Send empty POST body
                OutputStream os = con.getOutputStream();
                os.write(new byte[0]);
                os.flush();
                os.close();

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream())
                );
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    content.append(line);
                }
                in.close();
                con.disconnect();

                // crude parsing for demo
                String json = content.toString();
                String total = json.replaceAll("\n+", ""); // get digits
                totalLabel.setText("Total detections: " + total);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        
        Button resetButton = new Button("Reset Counter");

        resetButton.setOnAction(e -> {
            try {
                URL url = new URL("http://127.0.0.1:8000/reset");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.getOutputStream().write(new byte[0]); // empty body
                con.getOutputStream().flush();
                con.getOutputStream().close();

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream())
                );
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    content.append(line);
                }
                in.close();
                con.disconnect();

                totalLabel.setText("Total detections: 0"); // reset label locally

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        VBox root = new VBox(10, simulateButton, resetButton, totalLabel);
        root.setStyle("-fx-padding: 20;");
        Scene scene = new Scene(root, 300, 150);
        primaryStage.setTitle("Face Detector Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}