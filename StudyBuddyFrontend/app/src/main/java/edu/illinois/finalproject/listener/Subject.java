package edu.illinois.finalproject.listener;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {

    private final String TAG = Subject.class.getSimpleName();
    private static List<Observer> observerList = new ArrayList<>();

    public static void addObserver(Observer observer){
        observerList.add(observer);
    }

    public static void dataChanged() {
        for (Observer observer : observerList) {
            observer.update();
        }
    }
}
