package com.uos.schoollearningapplication;

import java.util.List;

public interface ButtonFactory {
   void update(List<CustomButton> buttons);
   double getX();
   double getY();
   void stopMoving();
   boolean isMoving();
}
