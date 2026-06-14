package com.rizki;

import com.rizki.view.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        // Hubungkan Stage utama ke ViewManager
        ViewManager.setStage(stage);
        
        // Tampilkan halaman login pertama kali
        ViewManager.showLoginView();
        
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}