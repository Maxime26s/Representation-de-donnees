import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane);

        primaryStage.setHeight(600);
        primaryStage.setWidth(600);
        primaryStage.setTitle("Laboratoire 7");
        primaryStage.setResizable(true);

        Menu menuImporter = new Menu("Importer");
        MenuItem itemLignes = new MenuItem("Lignes");
        MenuItem itemRegions = new MenuItem("Régions");
        MenuItem itemBarres = new MenuItem("Barres");
        menuImporter.getItems().addAll(itemLignes, itemRegions, itemBarres);
        Menu menuExporter = new Menu("Exporter");
        MenuItem itemPNG = new MenuItem("PNG");
        MenuItem itemGIF = new MenuItem("GIF");
        menuExporter.getItems().addAll(itemPNG, itemGIF);
        MenuBar menuBar = new MenuBar(menuImporter, menuExporter);
        borderPane.setTop(menuBar);

        itemLignes.setOnAction(event -> {
            ArrayList<String>[] parts = new ArrayList[0];
            try {
                parts = readFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            borderPane.setCenter(graphLignes(parts[0], parts[1]));
        });
        itemRegions.setOnAction(event -> {
            ArrayList<String>[] parts = new ArrayList[0];
            try {
                parts = readFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            borderPane.setCenter(graphRegions(parts[0], parts[1]));
        });
        itemBarres.setOnAction(event -> {
            ArrayList<String>[] parts = new ArrayList[0];
            try {
                parts = readFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            borderPane.setCenter(graphBarres(parts[0], parts[1]));
        });
        itemPNG.setOnAction(event -> saveAsPng(scene));
        itemGIF.setOnAction(event -> saveAsGif(scene));

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private LineChart graphLignes(ArrayList<String> part1, ArrayList<String> part2) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Mois");
        yAxis.setLabel("Température (c°)");
        //creating the chart
        final LineChart<String, Number> root = new LineChart<>(xAxis, yAxis);
        root.setTitle("La température en fonction du mois");
        //adding the data
        XYChart.Series series = new XYChart.Series();
        series.setName("Données");
        for (int i = 0; i < part1.size(); i++)
            series.getData().add(new XYChart.Data(part1.get(i), Integer.parseInt(part2.get(i))));
        root.getData().addAll(series);
        return root;
    }

    private AreaChart graphRegions(ArrayList<String> part1, ArrayList<String> part2) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Mois");
        yAxis.setLabel("Température (c°)");
        //creating the chart
        final AreaChart<String,Number> root = new AreaChart<>(xAxis,yAxis);
        root.setTitle("La température en fonction du mois");
        //adding the data
        XYChart.Series series = new XYChart.Series();
        series.setName("Données");
        for (int i = 0; i < part1.size(); i++)
            series.getData().add(new XYChart.Data(part1.get(i), Integer.parseInt(part2.get(i))));
        root.getData().addAll(series);
        return root;
    }

    private BarChart graphBarres(ArrayList<String> part1, ArrayList<String> part2) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Mois");
        yAxis.setLabel("Température (c°)");
        //creating the chart
        final BarChart<String, Number> root = new BarChart<>(xAxis, yAxis);
        root.setTitle("La température en fonction du mois");
        //adding the data
        XYChart.Series series = new XYChart.Series();
        series.setName("Données");
        for (int i = 0; i < part1.size(); i++)
            series.getData().add(new XYChart.Data(part1.get(i), Integer.parseInt(part2.get(i))));
        root.getData().addAll(series);
        return root;
    }

    private ArrayList<String>[] readFile() throws IOException {
        List<String> allLines = Files.readAllLines(getFile().toPath());
        ArrayList<String>[] partss = new ArrayList[allLines.size()];
        for (int i=0; i<partss.length;i++)
            partss[i] = new ArrayList<>();
        String string;
        for (int i = 0; i<allLines.size(); i++) {
            string=allLines.get(i);
            String[] parts = string.split(", ");
            for (int j = 0; j < parts.length; j++) {
                partss[i].add(parts[j]);
            }
        }
        return partss;
    }

    private File getFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Veuillez sélectionner un fichier");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers data", "*.dat"));
        return fc.showOpenDialog(stage);
    }

    private File saveFile(FileChooser.ExtensionFilter filter){
        FileChooser fc = new FileChooser();
        fc.setTitle("Veuillez sélectionner un fichier");
        fc.getExtensionFilters().add(filter);
        return fc.showSaveDialog(stage);
    }

    private void saveAsPng(Scene scene) {
        WritableImage image = scene.snapshot(null);
        File file = saveFile(new FileChooser.ExtensionFilter("Fichiers PNG", "*.png"));
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveAsGif(Scene scene) {
        WritableImage image = scene.snapshot(null);
        File file = saveFile(new FileChooser.ExtensionFilter("Fichiers GIF", "*.gif"));
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "gif", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}