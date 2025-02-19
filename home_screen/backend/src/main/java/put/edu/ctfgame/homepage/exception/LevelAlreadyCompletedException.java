package put.edu.ctfgame.homepage.exception;

public class LevelAlreadyCompletedException extends RuntimeException {
    public LevelAlreadyCompletedException(String message) {
        super(message);
    }
}
