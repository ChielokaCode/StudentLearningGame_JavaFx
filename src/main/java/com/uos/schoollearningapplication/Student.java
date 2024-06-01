package com.uos.schoollearningapplication;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

import java.util.Objects;

public class Student extends GameObject{
    private double x;
    private double y;
    private final GraphicsContext gc;
    private final Image studentImage;
    private final AudioClip moveSound;


    public Student(GraphicsContext gc, double x, double y) {
        super(gc, x, y);
        this.gc = gc;
        this.x = x;
        this.y = y;
        this.studentImage = new Image(Objects.requireNonNull(Student.class.getResourceAsStream("/com/uos/schoollearningapplication/student.png")));
        this.moveSound = new AudioClip(Objects.requireNonNull(Student.class.getResource("/com/uos/schoollearningapplication/studentWalking.mp3")).toExternalForm());
    }

    public void move(double dx, double dy) {
        // Update the x and y coordinates
        x += dx;
        y += dy;

        // Making sure the student stays within the bounds of the canvas
        if (x < 0) {
            x = 0;
        } else if (x > gc.getCanvas().getWidth() - 30) { // Assuming 30 is the width of the student image
            x = gc.getCanvas().getWidth() - 30;
        }
        if (y < 0) {
            y = 0;
        } else if (y > gc.getCanvas().getHeight() - 30) { // Assuming 30 is the height of the student image
            y = gc.getCanvas().getHeight() - 30;
        }

        //play sound while moving
        moveSound.play();
        update();
    }



    public void checkCollision(CustomButton button) {
        if (x < button.getX() + 30 && x + 30 > button.getX() &&
                y < button.getY() + 30 && y + 30 > button.getY()) {
            button.stopMoving();
        }
    }




    public void update() {
        // Draw the student at the current position
        gc.drawImage(studentImage, x, y, 50, 50); // Example: Draw the StudentImage representing the student
    }
}



