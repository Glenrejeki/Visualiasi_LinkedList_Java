import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;

public class VisualLinkedList extends Application {

    private LinkedList list = new LinkedList();
    private HBox visualBox = new HBox(10);
    private TextField removeIndexField = new TextField();

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20");

        // Buttons
        Button appendBtn = new Button("Append 10");
        Button prependBtn = new Button("Prepend 5");
        Button removeLastBtn = new Button("Remove Last");
        Button removeFirstBtn = new Button("Remove First");
        Button insertBtn = new Button("Insert(1, 20)");
        Button setBtn = new Button("Set(0, 99)");
        Button removeBtn = new Button("Remove(index)");
        Button reverseBtn = new Button("Reverse");

        // Input Field
        removeIndexField.setPromptText("Index (e.g., 2)");
        removeIndexField.setMaxWidth(100);

        // Event handlers
        appendBtn.setOnAction(e -> {
            list.append(10);
            updateView();
        });

        prependBtn.setOnAction(e -> {
            list.prepend(5);
            updateView();
        });

        removeLastBtn.setOnAction(e -> {
            list.removeLast();
            updateView();
        });

        removeFirstBtn.setOnAction(e -> {
            list.removeFirst();
            updateView();
        });

        insertBtn.setOnAction(e -> {
            list.insert(1, 20);
            updateView();
        });

        setBtn.setOnAction(e -> {
            list.set(0, 99);
            updateView();
        });

        removeBtn.setOnAction(e -> {
            try {
                int index = Integer.parseInt(removeIndexField.getText());
                list.remove(index);
                updateView();
            } catch (NumberFormatException ex) {
                showAlert("Masukkan index yang valid (angka).");
            }
        });

        reverseBtn.setOnAction(e -> {
            list.reverse();
            updateView();
        });

        // Layout
        HBox removeInputBox = new HBox(5, removeIndexField, removeBtn);
        VBox buttonBox = new VBox(5, appendBtn, prependBtn, removeLastBtn, removeFirstBtn,
                insertBtn, setBtn, reverseBtn, removeInputBox);

        root.getChildren().addAll(buttonBox, new Separator(), visualBox);

        Scene scene = new Scene(root, 700, 400);
        stage.setTitle("Visual Linked List");
        stage.setScene(scene);
        stage.show();
    }

    private void updateView() {
        visualBox.getChildren().clear();
        ArrayList<Integer> values = list.toArray();

        for (int i = 0; i < values.size(); i++) {
            VBox nodeBox = new VBox(5);
            Rectangle rect = new Rectangle(60, 30, Color.LIGHTBLUE);
            rect.setArcHeight(10);
            rect.setArcWidth(10);

            Text text = new Text(String.valueOf(values.get(i)));
            nodeBox.getChildren().addAll(rect, text);
            visualBox.getChildren().add(nodeBox);

            if (i < values.size() - 1) {
                Line arrow = new Line(0, 15, 30, 15);
                arrow.setStrokeWidth(2);
                visualBox.getChildren().add(new StackPane(arrow));
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }

    // LinkedList implementation
    class Node {
        int value;
        Node next;

        Node(int value) {
            this.value = value;
        }
    }

    class LinkedList {
        Node head, tail;
        int length = 0;

        void append(int value) {
            Node newNode = new Node(value);
            if (head == null) head = tail = newNode;
            else {
                tail.next = newNode;
                tail = newNode;
            }
            length++;
        }

        void prepend(int value) {
            Node newNode = new Node(value);
            newNode.next = head;
            head = newNode;
            if (tail == null) tail = newNode;
            length++;
        }

        void removeLast() {
            if (head == null) return;
            if (head == tail) {
                head = tail = null;
            } else {
                Node current = head;
                while (current.next != tail) {
                    current = current.next;
                }
                current.next = null;
                tail = current;
            }
            length--;
        }

        void removeFirst() {
            if (head == null) return;
            head = head.next;
            if (head == null) tail = null;
            length--;
        }

        Node get(int index) {
            if (index < 0 || index >= length) return null;
            Node current = head;
            for (int i = 0; i < index; i++) current = current.next;
            return current;
        }

        boolean set(int index, int value) {
            Node node = get(index);
            if (node != null) {
                node.value = value;
                return true;
            }
            return false;
        }

        boolean insert(int index, int value) {
            if (index < 0 || index > length) return false;
            if (index == 0) {
                prepend(value);
                return true;
            }
            if (index == length) {
                append(value);
                return true;
            }
            Node prev = get(index - 1);
            Node newNode = new Node(value);
            newNode.next = prev.next;
            prev.next = newNode;
            length++;
            return true;
        }

        Node remove(int index) {
            if (index < 0 || index >= length) return null;
            if (index == 0) {
                Node removed = head;
                removeFirst();
                return removed;
            }
            if (index == length - 1) {
                Node removed = tail;
                removeLast();
                return removed;
            }
            Node prev = get(index - 1);
            Node removed = prev.next;
            prev.next = removed.next;
            length--;
            return removed;
        }

        void reverse() {
            Node prev = null;
            Node current = head;
            tail = head;
            while (current != null) {
                Node nextNode = current.next;
                current.next = prev;
                prev = current;
                current = nextNode;
            }
            head = prev;
        }

        ArrayList<Integer> toArray() {
            ArrayList<Integer> arr = new ArrayList<>();
            Node current = head;
            while (current != null) {
                arr.add(current.value);
                current = current.next;
            }
            return arr;
        }
    }
}

/**
 * java --module-path "D:\SEMESTER 4\ASTRUDAT\Praktikum\Week 7\javafx-sdk-24\lib" --add-modules javafx.controls VisualLinkedList
 */