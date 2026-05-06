package dev.samsung.designParttern;

/* Assign cho một người nào đó có quyền hỗ trợ giải quyết nhưng chủ thể vẫn là người quyết định.
   Thư kí nhận yêu cầu nhưng Leader sẽ quyết định
*/
public class Proxy {

    public static class Leader {
        public void receiveRequest(String offer) {
            System.out.println("result::OK: " + offer);
        }
    }

    public static class Secretary {  // Proxy
        private final Leader leader;

        public Secretary() {
            this.leader = new Leader();
        }

        public void receiveRequest(String offer) {
            this.leader.receiveRequest(offer);
        }
    }


    public static class Developer {
        private final String offer;

        public Developer(String offer) {
            this.offer = offer;
        }

        public void applyFor(Secretary secretary) {
            secretary.receiveRequest(offer);
        }
    }

    public static void main(String[] args) {
        Developer dev = new Developer("Huy Upto 30tr");
        dev.applyFor(new Secretary());
    }
}
