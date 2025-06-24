package lab10;
// excepciones  para el B-Tree
public class Exceptions {
    public static class NullValueException extends RuntimeException {
        //valores nulos
        public NullValueException(String message) {
            super(message);
        }
    }

    public static class NegativeValueException extends RuntimeException {
        //Solo valores positivos
        public NegativeValueException(String message) {
            super(message);
        }
    }

    public static class NonNumericValueException extends RuntimeException {
        //Solo valores numericos
        public NonNumericValueException(String message) {
            super(message);
        }
    }

    
    /**
     * Excepción para operaciones ilegales en el B-Tree
     */
    public static class IllegalOperationException extends IllegalStateException {
        public IllegalOperationException(String message) {
            super(message);
        }
    }
    public static class IllegalArgumentBTreeException extends RuntimeException {
        public IllegalArgumentBTreeException(String msg) {
            super(msg);
        }
    }
    public static class ItemNotFound extends RuntimeException {
        public ItemNotFound(String message) {
            super(message);
        }
    }

}

 

    


