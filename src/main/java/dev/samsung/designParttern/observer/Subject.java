package dev.samsung.designParttern.observer;

public interface Subject {
    void attach(Observer observer);

    void detach(Observer observer);

    void notice(String message);
}
