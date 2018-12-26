package com.myapp.guiapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableEurekaClient
public class GuiApplication extends Application {

    private ConfigurableApplicationContext springContext;
    private Parent rootNode;

    public static void main(String[] args) {
       Application.launch(args);
    }

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(GuiApplication.class);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GuiApp.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        rootNode = fxmlLoader.load();
    }

    @Override
    public void start(Stage stage) throws Exception {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GuiApp.fxml"));
//        BorderPane rootElement = (BorderPane) loader.load();
        Scene scene = new Scene(rootNode, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
//        primaryStage.setTitle("Video processing");
//        primaryStage.setScene(scene);
//        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        springContext.stop();
    }
}
