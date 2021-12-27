package com.example.personalfinanceproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class FinanceMain extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(FinanceMain.class.getResource("Finance-Main.fxml"));
        VBox page = loader.load();
        Scene scene = new Scene(page);


        primaryStage.setTitle("Personal Finance Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}