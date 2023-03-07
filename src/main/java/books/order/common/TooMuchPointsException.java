package books.order.common;

public class TooMuchPointsException extends Exception {
    public TooMuchPointsException(String message) {
        super(message);
    }
}
