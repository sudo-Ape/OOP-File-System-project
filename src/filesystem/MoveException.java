package filesystem;

public class MoveException extends RuntimeException {
    public MoveException(String message) {
      /**
       * Create a MoveException with given error message
       *
       * @param message Given error message
       */
      super(message);
    }
}
