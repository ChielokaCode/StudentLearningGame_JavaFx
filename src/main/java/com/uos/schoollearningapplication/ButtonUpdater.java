package com.uos.schoollearningapplication;

import java.util.ArrayList;
import java.util.List;

public class ButtonUpdater{
    private final List<CustomButton> observers = new ArrayList<>();
    public void registerObserver(CustomButton observer) {
        observers.add(observer);
    }
    public List<CustomButton> getObservers() {
        return observers;
    }
}

