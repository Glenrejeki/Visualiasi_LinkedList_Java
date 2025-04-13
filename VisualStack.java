import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import java.util.Stack;

public class VisualStack extends Application {

    private Stack<String> stack = new Stack<>();
    private VBox stackView = new VBox();

    public static void main(String[] args) {
        launch(args); // JavaFX entry point
    }

    @Override
    public void start(Stage primaryStage) {
        // Input field
        TextField inputField = new TextField();
        inputField.setPromptText("Masukkan data");

        // Buttons
        Button pushButton = new Button("Push");
        Button popButton = new Button("Pop");

        // Stack visual
        stackView.setAlignment(Pos.BOTTOM_CENTER);
        stackView.setSpacing(5);
        stackView.setStyle("-fx-border-color: black; -fx-padding: 10px;");

        // Button actions
        pushButton.setOnAction(e -> {
            String data = inputField.getText();
            if (!data.isEmpty()) {
                stack.push(data);
                updateStackView();
                inputField.clear();
            }
        });

        popButton.setOnAction(e -> {
            if (!stack.isEmpty()) {
                stack.pop();
                updateStackView();
            }
        });

        // Layout
        HBox inputBox = new HBox(10, inputField, pushButton, popButton);
        inputBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(20, inputBox, stackView);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20px;");

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setTitle("Visual Stack - Push & Pop");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Update visual stack view
    private void updateStackView() {
        stackView.getChildren().clear();
        for (int i = stack.size() - 1; i >= 0; i--) {
            Label label = new Label(stack.get(i));
            label.setStyle("-fx-border-color: gray; -fx-padding: 10px; -fx-background-color: lightblue;");
            stackView.getChildren().add(label);
        }
    }
}
