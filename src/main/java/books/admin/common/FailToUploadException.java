package books.admin.common;

public class FailToUploadException extends RuntimeException {
    public FailToUploadException(String message) {
        super(message);
    }

    public FailToUploadException(Throwable cause) {
        super(cause);
    }
}
