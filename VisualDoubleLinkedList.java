import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class VisualDoubleLinkedList extends Application {

    private Node head, tail;
    private HBox view = new HBox(10);
    private int highlightIndex = -1; // Index yang ingin di-highlight

    private class Node {
        int data;
        Node prev, next;

        Node(int data) {
            this.data = data;
        }
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        HBox controls = new HBox(10);

        controls.getChildren().addAll(
                createButton("Append", () -> prompt("Append value:", val -> append(Integer.parseInt(val)))),
                createButton("Prepend", () -> prompt("Prepend value:", val -> prepend(Integer.parseInt(val)))),
                createButton("Remove First", this::removeFirst),
                createButton("Remove Last", this::removeLast),
                createButton("Insert At", () -> prompt("Index:", index ->
                        prompt("Value:", val -> insertAt(Integer.parseInt(index), Integer.parseInt(val))))),
                createButton("Remove At", () -> prompt("Index to remove:", idx -> removeAt(Integer.parseInt(idx)))),
                createButton("Set", () -> prompt("Index:", idx ->
                        prompt("New Value:", val -> setAt(Integer.parseInt(idx), Integer.parseInt(val))))),
                createButton("Get", () -> prompt("Index to get:", idx -> {
                    Integer value = getAt(Integer.parseInt(idx));
                    if (value != null) {
                        System.out.println("Value at index " + idx + " is " + value);
                    } else {
                        System.out.println("Index out of bounds.");
                    }
                }))
        );

        root.setTop(controls);
        root.setCenter(view);

        Scene scene = new Scene(root, 1000, 400);
        stage.setTitle("Visual Double Linked List");
        stage.setScene(scene);
        stage.show();
    }

    private Button createButton(String label, Runnable action) {
        Button btn = new Button(label);
        btn.setOnAction(e -> action.run());
        return btn;
    }

    private void prompt(String message, java.util.function.Consumer<String> callback) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(message);
        dialog.showAndWait().ifPresent(callback);
    }

    private StackPane createNodeVisual(int data, boolean highlight) {
        Rectangle rect = new Rectangle(60, 40);
        rect.setArcWidth(15);
        rect.setArcHeight(15);
        rect.setFill(highlight ? Color.LIGHTGREEN : Color.LIGHTBLUE);
        rect.setStroke(Color.DARKBLUE);

        Text text = new Text(String.valueOf(data));
        return new StackPane(rect, text);
    }

    private void refreshView() {
        view.getChildren().clear();
        Node current = head;
        int index = 0;
        while (current != null) {
            boolean highlight = (index == highlightIndex);
            StackPane nodePane = createNodeVisual(current.data, highlight);
            view.getChildren().add(nodePane);
            current = current.next;
            index++;
        }
    }

    // --- Operations ---

    private void append(int value) {
        Node newNode = new Node(value);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        refreshView();
    }

    private void prepend(int value) {
        Node newNode = new Node(value);
        if (head == null) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        refreshView();
    }

    private void removeFirst() {
        if (head == null) return;
        head = head.next;
        if (head != null) head.prev = null;
        else tail = null;
        refreshView();
    }

    private void removeLast() {
        if (tail == null) return;
        tail = tail.prev;
        if (tail != null) tail.next = null;
        else head = null;
        refreshView();
    }

    private void insertAt(int index, int value) {
        if (index <= 0) {
            prepend(value);
            return;
        }

        Node curr = head;
        for (int i = 0; i < index - 1 && curr != null; i++) {
            curr = curr.next;
        }

        if (curr == null || curr.next == null) {
            append(value);
        } else {
            Node newNode = new Node(value);
            newNode.next = curr.next;
            newNode.prev = curr;
            curr.next.prev = newNode;
            curr.next = newNode;
            refreshView();
        }
    }

    private void removeAt(int index) {
        if (index < 0 || head == null) return;
        if (index == 0) {
            removeFirst();
            return;
        }

        Node curr = head;
        for (int i = 0; i < index && curr != null; i++) {
            curr = curr.next;
        }

        if (curr == null) return;

        if (curr.prev != null) curr.prev.next = curr.next;
        if (curr.next != null) curr.next.prev = curr.prev;
        if (curr == tail) tail = curr.prev;

        refreshView();
    }

    private Integer getAt(int index) {
        Node curr = head;
        int i = 0;
        while (curr != null && i < index) {
            curr = curr.next;
            i++;
        }
        if (curr != null) {
            highlightIndex = index;
            refreshView(); // Tampilkan highlight

            // Delay 1 detik sebelum menghapus highlight
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> {
                highlightIndex = -1;
                refreshView(); // Reset tampilan
            });
            pause.play();

            return curr.data;
        } else {
            return null;
        }
    }

    private void setAt(int index, int value) {
        Node curr = head;
        for (int i = 0; i < index && curr != null; i++) {
            curr = curr.next;
        }
        if (curr != null) {
            curr.data = value;
            refreshView();
        }
    }
}

/*
 * Compile:
 * javac --module-path "D:/SEMESTER 4/ASTRUDAT/Praktikum/Week 7/javafx-sdk-24/lib" --add-modules javafx.controls VisualDoubleLinkedList.java
 *
 * Run:
 * java --module-path "D:/SEMESTER 4/ASTRUDAT/Praktikum/Week 7/javafx-sdk-24/lib" --add-modules javafx.controls VisualDoubleLinkedList
 */
