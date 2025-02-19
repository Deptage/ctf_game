package put.edu.ctfgame.messenger.exception;

public class WrongSenderException extends RuntimeException {
    public WrongSenderException(String message) {
        super(message);
    }
}
