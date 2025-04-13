import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.LinkedList;
import java.util.Queue;

public class VisualQueue extends Application {

    private final Queue<String> queue = new LinkedList<>();
    private final HBox queueDisplay = new HBox(10); // Untuk menampilkan elemen queue
    private final TextField inputField = new TextField();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Visual Queue - Enqueue & Dequeue");

        Label label = new Label("Masukkan Data:");
        Button enqueueBtn = new Button("Enqueue");
        Button dequeueBtn = new Button("Dequeue");

        // Action Enqueue
        enqueueBtn.setOnAction(e -> {
            String input = inputField.getText().trim();
            if (!input.isEmpty()) {
                queue.add(input);
                updateDisplay();
                inputField.clear();
            }
        });

        // Action Dequeue
        dequeueBtn.setOnAction(e -> {
            if (!queue.isEmpty()) {
                queue.poll(); // Menghapus elemen pertama
                updateDisplay();
            }
        });

        HBox controlBox = new HBox(10, label, inputField, enqueueBtn, dequeueBtn);
        controlBox.setStyle("-fx-padding: 10;");

        VBox root = new VBox(10, controlBox, queueDisplay);
        root.setStyle("-fx-padding: 10;");

        Scene scene = new Scene(root, 600, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateDisplay() {
        queueDisplay.getChildren().clear();
        for (String item : queue) {
            Label label = new Label(item);
            label.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: lightblue;");
            queueDisplay.getChildren().add(label);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

/*
 * javac --module-path "D:/SEMESTER 4/ASTRUDAT/Praktikum/Week 7/javafx-sdk-24/lib" --add-modules javafx.controls VisualQueue.java
java --module-path "D:/SEMESTER 4/ASTRUDAT/Praktikum/Week 7/javafx-sdk-24/lib" --add-modules javafx.controls VisualQueue

 */