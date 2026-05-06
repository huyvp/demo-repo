package dev.samsung.designParttern;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Strategy {

    // Tất cả con đường đều dẫn tới thành Rome. Túc là cùng Input - Output nhưng khác nhau cách triển khai.

    public static Function<Double, Double> getPromotePrice = price -> price * 0.05;
    public static Function<Double, Double> getDefaultPrice = price -> price;

    private static final Map<String, Function<Double, Double>> priceStrategies = new HashMap<>();

    static {
        priceStrategies.put("promotion", getPromotePrice);
        priceStrategies.put("default", getDefaultPrice);
    }

    public static double getPrice(Double originalPrice, String type) {
        return priceStrategies.get(type).apply(originalPrice);
    }

    public static void main(String[] args) {
        System.out.println(getPrice(200.88, "promotion"));
    }
}
