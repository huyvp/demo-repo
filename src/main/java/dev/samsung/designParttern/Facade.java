package dev.samsung.designParttern;

public class Facade {
    private static class Shipping {
        public double calc() {
            return 5;
        }
    }

    private static class Discount {
        public double calc(double price) {
            return price * 0.9;
        }
    }

    private static class Fees {
        public double calc(double price) {
            return price * 1.05;
        }
    }

    private static class ShoppeFacade {
        private final Discount discount;
        private final Fees fees;
        private final Shipping shipping;


        public ShoppeFacade() {
            this.discount = new Discount();
            this.fees = new Fees();
            this.shipping = new Shipping();
        }

        public double calc(double price) {
            price = discount.calc(price);
            price = fees.calc(price);
            price += shipping.calc();
            return price;
        }
    }

    public static void main(String[] args) {
        ShoppeFacade facade = new ShoppeFacade();
        System.out.println(
                facade.calc(1000)
        );
    }
}
