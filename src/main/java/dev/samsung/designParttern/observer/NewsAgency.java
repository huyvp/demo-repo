package dev.samsung.designParttern.observer;

import java.util.ArrayList;
import java.util.List;

public class NewsAgency implements Subject {
    private final List<Observer> observers = new ArrayList<Observer>();

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notice(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public void publishNews(String news) {
        System.out.println("NewsAgency phát tin: " + news);
        notice(news);
    }
}
