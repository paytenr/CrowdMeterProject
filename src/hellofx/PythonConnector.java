package hellofx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import py4j.GatewayServer;

public class PythonConnector {

    private Label totalLabel = new Label("Total detections: 0");

    // Returns a JavaFX node to embed in your dashboard
    public Node getGUI() {
        Button simulateButton = new Button("Simulate Person");
        simulateButton.setOnAction(e -> totalLabel.setText("Total detections: 1"));

        Button resetButton = new Button("Reset Counter");
        resetButton.setOnAction(e -> totalLabel.setText("Total detections: 0"));

        VBox root = new VBox(10, simulateButton, resetButton, totalLabel);
        root.setStyle("-fx-padding: 10; -fx-border-color: gray; -fx-border-width: 1;");
        return root;
    }

    // Python will call this method via Py4J
    public void sendResult(String msg) {
        System.out.println("[Java] Python says: " + msg);
        Platform.runLater(() -> totalLabel.setText("Total detections: " + msg));
    }

    // Start Py4J GatewayServer
    public void startGateway() {
        int port = 25333;
        GatewayServer server = new GatewayServer(this, port);
        server.start();
        System.out.println("[Java] GatewayServer started on port " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.shutdown();
            System.out.println("[Java] GatewayServer stopped, port freed");
        }));
    }
}