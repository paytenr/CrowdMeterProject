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
import java.io.*;
import py4j.GatewayServer;

public class PythonConnectorGUI extends Application {
    public String Message() { 
    	return "Hello world Jayden"; 
    	}
    private Label totalLabel = new Label("Total detections: 0");

    @Override
    public void start(Stage primaryStage) {
        Button simulateButton = new Button("Simulate Person");

        simulateButton.setOnAction(e -> {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    content.append(line);
                }
                
                // crude parsing for demo
                String json = content.toString();
                String total = json.replaceAll("\n+", ""); // get digits
                totalLabel.setText("Total detections: " + total);

             
        });
        
        Button resetButton = new Button("Reset Counter");

        resetButton.setOnAction(e -> {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    content.append(line);
                }
                totalLabel.setText("Total detections: 0"); // reset label locally

             
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
        GatewayServer g = new GatewayServer(new GFG(), 25533);
        g.start();
        System.out.println("Gateway Server Started");
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            g.shutdown();
            System.out.println("GatewayServer stopped.");
        }));
    }

}
