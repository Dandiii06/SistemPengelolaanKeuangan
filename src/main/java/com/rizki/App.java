package com.rizki;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        Label label = new Label("Hello JavaFX 21");

        Scene scene = new Scene(new StackPane(label), 400, 300);

        stage.setTitle("Sistem Pengelolaan Keuangan");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}