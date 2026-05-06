package dev.samsung.designParttern;

/*  - Mặc định của prototype là shadow copy
    - implements Cloneable để sử dụng super.clone()
    - Nếu thuộc tính là một Object thì sẽ gây lỗi khi đổi giá trị, nó sẽ đổi giá trị cả 2 Object
 */

public class Prototype {

    public static class Player implements Cloneable {
        private String name;
        private String team;
        private String position;
        private int goals;
        private int minutesPlayed = 10;

        public Player(String name, String team, String position, int goals) {
            this.name = name;
            this.team = team;
            this.position = position;
            this.goals = goals;
        }

        public void score() {
            this.goals++;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setTeam(String team) {
            this.team = team;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public void setGoals(int goals) {
            this.goals = goals;
        }

        public void setMinutesPlayed(int minutesPlayed) {
            this.minutesPlayed = minutesPlayed;
        }

        public Player clone() {
            try {
                return (Player) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void main(String[] args) throws CloneNotSupportedException {
        Player playerPrototype = new Player("CR7", "MU", "FW", 1);

        Player cr7 = playerPrototype.clone();
        Player m10 = playerPrototype.clone();

        m10.setName("Messi");
        m10.setTeam("PSG");

        cr7.score();
        System.out.println(cr7.name + " has recorded \t" + cr7.goals + " goals \t" + cr7.minutesPlayed);

        m10.score();
        m10.score();
        System.out.println(m10.name + " has recorded \t" + m10.goals + " goals \t" + m10.minutesPlayed);
    }
}
