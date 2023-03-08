package books.order.common;

public class NotEnoughPointStampException extends Exception{
    public NotEnoughPointStampException(String message) {
        super(message);
    }
}
