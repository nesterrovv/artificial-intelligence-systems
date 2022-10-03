package exceptions;

/**
 * Class of exception which are thrown
 * when some necessary file not found.
 * @author Ivan Nesterov
 * @version 1.0
 * @since JDK 18.0
 */
public class UnknownClassException extends RuntimeException {

    /**
     * Constructor of this class.
     * @param message error message for this exception
     */
    public UnknownClassException(String message) {
        super(message);
    }

}
