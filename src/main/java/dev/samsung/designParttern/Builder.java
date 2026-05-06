package dev.samsung.designParttern;

public class Builder {

    public static class Stats {
        private int playedMinutes;
    }

    public static class Player {
        private String name;
        private int age;
        private String nationality;
        private String team;
        private Stats stats;

        public Player(String name, int age, String nationality, String team, Stats stats) {
            this.name = name;
            this.age = age;
            this.nationality = nationality;
            this.team = team;

        }
    }

    public static void main(String[] args) {

    }
}
