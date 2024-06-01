package com.uos.schoollearningapplication;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MovingButtonsApp extends Application {

    public GameServiceImpl gameService = GameServiceImpl.getInstance();

    @Override
    public void start(Stage primaryStage) {

        Pane root = new Pane();
        gameService.initializeResources();
        Canvas canvas = gameService.setupCanvas(root);
        gameService.setupButtons(root);

        Scene scene = new Scene(root, AppConstant.SCENE_WIDTH, AppConstant.SCENE_HEIGHT);
        gameService.setupKeyEventHandlers(scene);
        gameService.setupScene(primaryStage, scene);



        gameService.startAnimationTimer();

        // Making sure scene is focused to receive key events
        canvas.setOnMouseClicked(event -> canvas.requestFocus());
        canvas.requestFocus();
    }

    public static void main(String[] args) {
        launch();
    }
}

