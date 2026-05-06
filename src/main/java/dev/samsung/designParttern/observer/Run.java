package dev.samsung.designParttern.observer;

public class Run {
    public static void main(String[] args) {
        NewsAgency agency = new NewsAgency();

        Observer chanel1 = new Channel("VTC");
        Observer chanel2 = new Channel("HTV");

        agency.attach(chanel1);
        agency.attach(chanel2);

        agency.publishNews("Tai nạn ở hà nội");
    }
}
