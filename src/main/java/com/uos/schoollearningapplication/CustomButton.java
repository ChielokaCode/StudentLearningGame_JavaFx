package com.uos.schoollearningapplication;

import javafx.scene.control.Button;
import javafx.scene.media.AudioClip;

import java.util.List;
import java.util.Objects;


public class CustomButton extends Button implements ButtonFactory {
    private double x;
    private double y;
    private double dx;
    private double dy;
    private boolean moving = true;
    private static final String[] labels = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");
    private static int expectedIndex = 0;
    private static final double BUTTON_SIZE = 35.0;
    private final AudioClip collisionSound;
    
    private CustomButton(Builder builder) {
        super(builder.text);
        this.x = builder.x;
        this.y = builder.y;
        this.dx = builder.dx;
        this.dy = builder.dy;
        this.collisionSound = builder.collisionSound;
        relocate(x, y);
        setStyle(
                "-fx-background-color: " + builder.color + ";" +
                        "-fx-background-radius:" + BUTTON_SIZE + "em;" +
                        "-fx-min-width: " + BUTTON_SIZE + "px;" +
                        "-fx-min-height: " + BUTTON_SIZE + "px;" +
                        "-fx-max-width: " + BUTTON_SIZE + "px;" +
                        "-fx-max-height: " + BUTTON_SIZE + "px;" +
                        "-fx-border-color: black;" +
                        "-fx-border-radius: 30em;" +
                        "-fx-text-fill: black;"
        );
    }

    public static class Builder {
        private String text;
        private double x;
        private double y;
        private double dx;
        private double dy;
        private String color;
        private AudioClip collisionSound;

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setX(double x) {
            this.x = x;
            return this;
        }

        public Builder setY(double y) {
            this.y = y;
            return this;
        }

        public Builder setDx(double dx) {
            this.dx = dx;
            return this;
        }

        public Builder setDy(double dy) {
            this.dy = dy;
            return this;
        }

        public Builder setColor(String color) {
            this.color = color;
            return this;
        }

        public Builder setCollisionSound(AudioClip collisionSound) {
            this.collisionSound = collisionSound;
            return this;
        }

        public CustomButton build() {
            return new CustomButton(this);
        }
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void stopMoving() {
        //compare the button text and the text in the created Array
        if (Objects.equals(getText(), labels[expectedIndex])) {
            moving = false;
            setStyle(
                    "-fx-background-color: #FF0000;" +  // Change color to indicate it has stopped
                            "-fx-background-radius: 40em;" +    // Radius to make it circular
                            "-fx-min-width: 40px;" +            // Minimum width
                            "-fx-min-height: 40px;" +           // Minimum height
                            "-fx-max-width: 40px;" +            // Maximum width
                            "-fx-max-height: 40px;" +           // Maximum height
                            "-fx-border-color: black;" +
                            "-fx-border-radius: 40em;" +
                            "-fx-text-fill: white;"
            );
            expectedIndex++;
            collisionSound.play();
        }
    }

    @Override
    public boolean isMoving() {
        return moving;
    }

    @Override
    public void update(List<CustomButton> buttons) {
        if (moving) {
            x += dx;
            y += dy;

            // Ensure the button stays within the scene bounds
            if (x <= 0 || x >= AppConstant.SCENE_WIDTH - BUTTON_SIZE) {
                dx *= -1;
            }
            if (y <= 0 || y >= AppConstant.SCENE_HEIGHT - BUTTON_SIZE) {
                dy *= -1;
            }
            relocate(x, y);
        }
    }

}
