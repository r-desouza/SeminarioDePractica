package com.gestionreposteria;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // Inicializa la escena cargando el Dashboard
        // Las dimensiones (950x650) coinciden con el diseño del BorderPane
        scene = new Scene(loadFXML("dashboard"), 950, 650);

        stage.setScene(scene);
        stage.setTitle("Sistema de Gestión - Repostería Artesanal");

        // Optimización para pantallas compactas como la laptop de la cocina
        stage.setResizable(true);
        stage.setMinWidth(900);
        stage.setMinHeight(600);

        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    // Busca y procesa el archivo FXML correspondiente desde la carpeta de recursos
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/gestionreposteria/vista/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        // Inicia la aplicacion
        launch();
    }
}