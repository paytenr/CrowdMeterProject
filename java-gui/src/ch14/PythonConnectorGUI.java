package ch14;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import py4j.GatewayServer;

public class PythonConnectorGUI extends Application {

    private Label totalLabel = new Label("Total detections: 0");

    // Interface Python implements to send data to Java
    public interface PyActions {
        void sendResult(String msg);
    }

    // Python will call this method
    public void sendResult(String msg) {
        System.out.println("[Java] Python says: " + msg);

        // Update JavaFX label safely on UI thread
        Platform.runLater(() -> totalLabel.setText("Total detections: " + msg));
    }

    @Override
    public void start(Stage primaryStage) {
        Button simulateButton = new Button("Simulate Person");
        simulateButton.setOnAction(e -> totalLabel.setText("Total detections: 1"));

        Button resetButton = new Button("Reset Counter");
        resetButton.setOnAction(e -> totalLabel.setText("Total detections: 0"));

        VBox root = new VBox(10, simulateButton, resetButton, totalLabel);
        root.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(root, 300, 150);
        primaryStage.setTitle("Face Detector Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {

        PythonConnectorGUI app = new PythonConnectorGUI();

        // Declare port first
        int port = 25333;

        // Start Java GatewayServer
        GatewayServer server = new GatewayServer(app, port);
        server.start();
        System.out.println("[Java] GatewayServer started on port " + port);

        // Launch JavaFX GUI
        launch(args);
        
        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.shutdown();
            System.out.println("[Java] GatewayServer stopped, port freed");
        }));

    }
   }
