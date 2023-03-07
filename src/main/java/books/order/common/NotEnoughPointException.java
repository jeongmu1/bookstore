package books.order.common;

public class NotEnoughPointException extends Exception {
    public NotEnoughPointException(String message) {
        super(message);
    }
}
