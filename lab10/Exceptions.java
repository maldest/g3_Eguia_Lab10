package lab10;
// Excepciones personalizadas para el B-Tree
public class Exceptions {
    public static class NullValueException extends RuntimeException {
        public NullValueException(String message) {
            super(message);
        }
    }

    public static class NegativeValueException extends RuntimeException {
        public NegativeValueException(String message) {
            super(message);
        }
    }

    public static class NonNumericValueException extends RuntimeException {
        public NonNumericValueException(String message) {
            super(message);
        }
    }
}