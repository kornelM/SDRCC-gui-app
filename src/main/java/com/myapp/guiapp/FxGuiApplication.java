package com.myapp.guiapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class FxGuiApplication extends Application implements Runnable {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GuiApp.fxml"));
            BorderPane rootElement = (BorderPane) loader.load();
            Scene scene = new Scene(rootElement, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
            primaryStage.setTitle("Video processing");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Application.launch();
    }

}
