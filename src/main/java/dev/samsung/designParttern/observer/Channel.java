package dev.samsung.designParttern.observer;

public class Channel implements Observer {
    private final String name;

    public Channel(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        System.out.println(name + " nhận tin mới: " + message);
    }
}
