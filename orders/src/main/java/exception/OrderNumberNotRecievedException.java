package exception;

public class OrderNumberNotRecievedException extends RuntimeException {

    public OrderNumberNotRecievedException(String message) {
        super(message);
    }
}
