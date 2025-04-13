import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

public class VisualisasiGraph extends Application {

    private final int WIDTH = 800;
    private final int HEIGHT = 600;

    private Canvas canvas;
    private GraphicsContext gc;

    private Map<String, Vertex> vertices = new HashMap<>();
    private List<Edge> edges = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Visualisasi Graph");

        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

        TextField vertexName = new TextField();
        vertexName.setPromptText("Nama Vertex");

        TextField vertexX = new TextField();
        vertexX.setPromptText("x");
        TextField vertexY = new TextField();
        vertexY.setPromptText("y");

        Button addVertexBtn = new Button("Add Vertex");

        TextField from = new TextField();
        from.setPromptText("From");

        TextField to = new TextField();
        to.setPromptText("To");

        Button addEdgeBtn = new Button("Add Edge");
        Button removeEdgeBtn = new Button("Remove Edge");
        Button removeVertexBtn = new Button("Remove Vertex");

        Label status = new Label();

        addVertexBtn.setOnAction(e -> {
            try {
                String name = vertexName.getText().trim();
                int x = Integer.parseInt(vertexX.getText());
                int y = Integer.parseInt(vertexY.getText());
                if (!vertices.containsKey(name)) {
                    vertices.put(name, new Vertex(name, x, y));
                    redraw();
                    status.setText("Vertex " + name + " ditambahkan.");
                } else {
                    status.setText("Vertex sudah ada.");
                }
            } catch (Exception ex) {
                status.setText("Input vertex tidak valid.");
            }
        });

        addEdgeBtn.setOnAction(e -> {
            String a = from.getText().trim();
            String b = to.getText().trim();
            if (vertices.containsKey(a) && vertices.containsKey(b)) {
                Edge edge = new Edge(vertices.get(a), vertices.get(b));
                if (!edges.contains(edge)) {
                    edges.add(edge);
                    redraw();
                    status.setText("Edge " + a + "-" + b + " ditambahkan.");
                } else {
                    status.setText("Edge sudah ada.");
                }
            } else {
                status.setText("Vertex tidak ditemukan.");
            }
        });

        removeEdgeBtn.setOnAction(e -> {
            String a = from.getText().trim();
            String b = to.getText().trim();
            edges.removeIf(edge -> edge.connects(a, b));
            redraw();
            status.setText("Edge " + a + "-" + b + " dihapus (jika ada).");
        });

        removeVertexBtn.setOnAction(e -> {
            String name = vertexName.getText().trim();
            if (vertices.containsKey(name)) {
                vertices.remove(name);
                edges.removeIf(edge -> edge.v1.name.equals(name) || edge.v2.name.equals(name));
                redraw();
                status.setText("Vertex " + name + " dihapus.");
            } else {
                status.setText("Vertex tidak ditemukan.");
            }
        });

        HBox vertexInput = new HBox(5, new Label("Vertex:"), vertexName, vertexX, vertexY, addVertexBtn, removeVertexBtn);
        HBox edgeInput = new HBox(5, new Label("Edge:"), from, to, addEdgeBtn, removeEdgeBtn);
        VBox layout = new VBox(10, vertexInput, edgeInput, status, canvas);
        layout.setStyle("-fx-padding: 10;");

        stage.setScene(new Scene(layout));
        stage.show();
    }

    private void redraw() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.LIGHTBLUE);

        // Draw edges
        for (Edge edge : edges) {
            gc.strokeLine(edge.v1.x, edge.v1.y, edge.v2.x, edge.v2.y);
        }

        // Draw vertices
        for (Vertex v : vertices.values()) {
            gc.setFill(Color.LIGHTBLUE);
            gc.fillOval(v.x - 15, v.y - 15, 30, 30);
            gc.setStroke(Color.BLACK);
            gc.strokeOval(v.x - 15, v.y - 15, 30, 30);
            gc.strokeText(v.name, v.x - 10, v.y + 5);
        }
    }

    // ================= Class bantu =================

    static class Vertex {
        String name;
        int x, y;

        Vertex(String name, int x, int y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }
    }

    static class Edge {
        Vertex v1, v2;

        Edge(Vertex a, Vertex b) {
            this.v1 = a;
            this.v2 = b;
        }

        boolean connects(String a, String b) {
            return (v1.name.equals(a) && v2.name.equals(b)) || (v1.name.equals(b) && v2.name.equals(a));
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Edge other) {
                return connects(other.v1.name, other.v2.name);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(v1.name, v2.name) + Objects.hash(v2.name, v1.name);
        }
    }
}


/*

javac --module-path "D:/SEMESTER 4/ASTRUDAT/Praktikum/Week 7/javafx-sdk-24/lib" --add-modules javafx.controls VisualisasiGraph.java
java --module-path "D:/SEMESTER 4/ASTRUDAT/Praktikum/Week 7/javafx-sdk-24/lib" --add-modules javafx.controls VisualisasiGraph

 * 1. Menambahkan Vertex (Titik)
Input:

Nama Vertex: A

x: 100

y: 100

Klik tombol Add Vertex
👉 Titik A akan muncul di koordinat (100, 100)

Ulangi langkah untuk vertex lainnya:

B di (300, 100)

C di (200, 250)

2. Menambahkan Edge (Garis/Sisi)
Input:

From: A

To: B

Klik tombol Add Edge
👉 Akan muncul garis dari titik A ke B

Tambahkan edge lain:

A ke C

B ke C

3. Menghapus Edge
Input:

From: A

To: B

Klik tombol Remove Edge
👉 Garis dari A ke B akan dihapus (jika ada)

4. Menghapus Vertex
Input:

Nama Vertex: C

Klik tombol Remove Vertex
👉 Titik C dan semua garis yang terhubung ke C akan dihapus
 */
