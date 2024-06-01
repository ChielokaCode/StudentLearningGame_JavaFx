package com.uos.schoollearningapplication;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public interface GameService {
    void initializeResources();
    Canvas setupCanvas(Pane root);
    void setupButtons(Pane root);
    void setupKeyEventHandlers(Scene scene);
    void setupScene(Stage primaryStage, Scene scene);
    void startAnimationTimer();
}
