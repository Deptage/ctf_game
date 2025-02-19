package put.edu.ctfgame.messenger.exception;

public class UserNotPartOfConversationException extends RuntimeException {
    public UserNotPartOfConversationException(String message) {
        super(message);
    }
}
