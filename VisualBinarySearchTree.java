import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class VisualBinarySearchTree extends Application {

    private TreeNode root;
    private Canvas canvas;
    private final int WIDTH = 800;
    private final int HEIGHT = 600;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Visual Binary Search Tree");

        canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        TextField input = new TextField();
        input.setPromptText("Masukkan angka");

        Button insertBtn = new Button("Insert");
        Button deleteBtn = new Button("Delete");
        Button searchBtn = new Button("Contains");

        Label status = new Label();

        insertBtn.setOnAction(e -> {
            try {
                int val = Integer.parseInt(input.getText());
                root = insert(root, val);
                redrawTree(gc);
                status.setText("Inserted: " + val);
                input.clear();
            } catch (NumberFormatException ex) {
                status.setText("Masukkan angka valid.");
            }
        });

        deleteBtn.setOnAction(e -> {
            try {
                int val = Integer.parseInt(input.getText());
                root = delete(root, val);
                redrawTree(gc);
                status.setText("Deleted: " + val);
                input.clear();
            } catch (NumberFormatException ex) {
                status.setText("Masukkan angka valid.");
            }
        });

        searchBtn.setOnAction(e -> {
            try {
                int val = Integer.parseInt(input.getText());
                boolean found = contains(root, val);
                status.setText(found ? "Found: " + val : "Not found: " + val);
            } catch (NumberFormatException ex) {
                status.setText("Masukkan angka valid.");
            }
        });

        HBox controls = new HBox(10, new Label("Input:"), input, insertBtn, deleteBtn, searchBtn);
        VBox layout = new VBox(10, controls, status, canvas);
        layout.setStyle("-fx-padding: 10;");

        stage.setScene(new Scene(layout));
        stage.show();
    }

    // ================= LOGIKA BST =================

    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int v) {
            val = v;
        }
    }

    private TreeNode insert(TreeNode node, int val) {
        if (node == null) return new TreeNode(val);
        if (val < node.val) node.left = insert(node.left, val);
        else if (val > node.val) node.right = insert(node.right, val);
        return node;
    }

    private boolean contains(TreeNode node, int val) {
        if (node == null) return false;
        if (val == node.val) return true;
        return val < node.val ? contains(node.left, val) : contains(node.right, val);
    }

    private TreeNode delete(TreeNode node, int val) {
        if (node == null) return null;
        if (val < node.val) node.left = delete(node.left, val);
        else if (val > node.val) node.right = delete(node.right, val);
        else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            node.val = findMin(node.right);
            node.right = delete(node.right, node.val);
        }
        return node;
    }

    private int findMin(TreeNode node) {
        while (node.left != null) node = node.left;
        return node.val;
    }

    // ================= VISUALISASI =================

    private void redrawTree(GraphicsContext gc) {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        drawNode(gc, root, WIDTH / 2, 50, WIDTH / 4);
    }

    private void drawNode(GraphicsContext gc, TreeNode node, double x, double y, double offset) {
        if (node == null) return;

        gc.setFill(Color.LIGHTBLUE);
        gc.fillOval(x - 15, y - 15, 30, 30);
        gc.setStroke(Color.BLACK);
        gc.strokeOval(x - 15, y - 15, 30, 30);
        gc.strokeText(String.valueOf(node.val), x - 6, y + 4);

        if (node.left != null) {
            gc.strokeLine(x, y, x - offset, y + 60);
            drawNode(gc, node.left, x - offset, y + 60, offset / 1.5);
        }

        if (node.right != null) {
            gc.strokeLine(x, y, x + offset, y + 60);
            drawNode(gc, node.right, x + offset, y + 60, offset / 1.5);
        }
    }
}
/*
 * javac --module-path "D:/SEMESTER 4/ASTRUDAT/Praktikum/Week 7/javafx-sdk-24/lib" --add-modules javafx.controls VisualBinarySearchTree.java
java --module-path "D:/SEMESTER 4/ASTRUDAT/Praktikum/Week 7/javafx-sdk-24/lib" --add-modules javafx.controls VisualBinarySearchTree

 */