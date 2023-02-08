package books.order.common;

public class OverStockException extends Exception {
    public OverStockException(String message) {
        super(message);
    }
}
