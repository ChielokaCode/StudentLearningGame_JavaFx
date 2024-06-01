package com.uos.schoollearningapplication;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameServiceImpl implements GameService{
    private static GameServiceImpl instance;
    private GraphicsContext gc;
    private final ButtonUpdater buttonUpdater = new ButtonUpdater();

    private List<CustomButton> buttons;
    private Student student;
    private Image backgroundImage;
    private AudioClip gameOverSound;
    private AudioClip studentWonSound;
    private AnimationTimer timer;
    private final long startTime = System.currentTimeMillis();
    private int countdownTime;
    private boolean gameWon = false;
    private final int setMinutesInSecondsFormat = 150; //count down timer , set minutes in seconds format

    // Private constructor to prevent instantiation
    private GameServiceImpl() {}

    // Static method to provide access to the instance
    public static GameServiceImpl getInstance() {
        if (instance == null) {
            instance = new GameServiceImpl();
        }
        return instance;
    }

    @Override
    public void initializeResources() {
        backgroundImage = new Image(Objects.requireNonNull(MovingButtonsApp.class.getResourceAsStream("/com/uos/schoollearningapplication/Classroom.png")));
        gameOverSound = new AudioClip(Objects.requireNonNull(MovingButtonsApp.class.getResource("/com/uos/schoollearningapplication/gameOverSound.mp3")).toExternalForm());
        studentWonSound = new AudioClip(Objects.requireNonNull(MovingButtonsApp.class.getResource("/com/uos/schoollearningapplication/studentWonSound.mp3")).toExternalForm());
    }

    @Override
    public Canvas setupCanvas(Pane root) {
        Canvas canvas = new Canvas(AppConstant.SCENE_WIDTH, AppConstant.SCENE_HEIGHT);
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();
        gc.drawImage(backgroundImage, 0, 0, canvas.getWidth(), canvas.getHeight());
        student = new Student(gc, 5, 50);
        return canvas;
    }

    @Override
    public void setupButtons(Pane root) {
        buttons = new ArrayList<>();
        String[] colors = {
                "#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF", "#00FFFF",
                "#800000", "#008000", "#000080", "#808000", "#800080", "#008080",
                "#C0C0C0", "#FF69B4", "#CD5C5C", "#4B0082", "#4682B4", "#32CD32",
                "#FFD700", "#FFA500", "#DC143C", "#ADFF2F", "#00CED1", "#FF1493",
                "#7FFF00", "#FF4500"
        };

        double speedVelocity = 0.7;
        for (int i = 0; i < 26; i++) {
            char label = (char) ('A' + i);
            String color = colors[i % colors.length];
            CustomButton button = new CustomButton.Builder()
                    .setText(String.valueOf(label))
                    .setX(Math.random() * (AppConstant.SCENE_WIDTH - 50))
                    .setY(Math.random() * (AppConstant.SCENE_HEIGHT - 50))
                    .setDx((Math.random() * 2 - 1) * speedVelocity)
                    .setDy((Math.random() * 2 - 1) * speedVelocity)
                    .setColor(color)
                    .setCollisionSound(new AudioClip(Objects.requireNonNull(Student.class.getResource("/com/uos/schoollearningapplication/collisionSound.mp3")).toExternalForm()))
                    .build();
            buttons.add(button);
            buttonUpdater.registerObserver(button);
        }
        root.getChildren().addAll(buttons);
    }

    @Override
    public void setupKeyEventHandlers(Scene scene) {
        scene.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            double moveAmount = 15; // Adjust as needed
            switch (keyCode) {
                case UP, W -> student.move(0, -moveAmount);
                case DOWN, S -> student.move(0, moveAmount);
                case LEFT, A -> student.move(-moveAmount, 0);
                case RIGHT, D -> student.move(moveAmount, 0);
                case Q -> student.move(-10, -moveAmount);
                case E -> student.move(10, -moveAmount);
                case Z -> student.move(-moveAmount, 10);
                case C -> student.move(moveAmount, 10);
                default -> {
                    // Do nothing for other keys
                }
            }
        });
    }

    @Override
    public void setupScene(Stage primaryStage, Scene scene) {
        primaryStage.setTitle("Moving Buttons App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void startAnimationTimer() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedMillis = System.currentTimeMillis() - startTime;
                int elapsedSeconds = (int) (elapsedMillis / 1000);

                countdownTime = setMinutesInSecondsFormat - elapsedSeconds;  // Update countdown time

                if (countdownTime <= 0) {
                    endGame("Game Over", gameOverSound);
                } else {
                    updateGame();
                }
            }
        };
        timer.start();
    }



    private void updateGame() {
        gc.drawImage(backgroundImage, 0, 0, AppConstant.SCENE_WIDTH, AppConstant.SCENE_HEIGHT);
        gc.setFill(Color.WHITE);
        gc.setFont(new javafx.scene.text.Font(40));
        gc.fillText("Time: " + countdownTime, 700, 60); // Draw the timer on the canvas
        student.update();

        for (CustomButton button : buttonUpdater.getObservers()) {
            student.checkCollision(button);
            button.update(buttonUpdater.getObservers());
        }

        if (checkAllButtonsStopped(buttons)) {
            gameWon = true;
            endGame("You Won", studentWonSound);
        }
    }


    private void endGame(String message, AudioClip sound) {
        gc.clearRect(0, 0, AppConstant.SCENE_WIDTH, AppConstant.SCENE_HEIGHT);
        gc.setFill(Color.BLACK);
        student.update();
        gc.setFont(new javafx.scene.text.Font(40)); // Set font size for the message text
        gc.fillText(message, AppConstant.SCENE_WIDTH / 2 - 100, AppConstant.SCENE_HEIGHT / 2);
        sound.play();
        timer.stop();
    }


    private boolean checkAllButtonsStopped(List<CustomButton> buttons) {
        for (CustomButton button : buttons) {
            if (button.isMoving()) {
                return false;
            }
        }
        return true;
    }
}
