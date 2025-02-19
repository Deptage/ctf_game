package put.edu.ctfgame.messenger.exception;

public class MessageDoesNotBelongToConversationException extends RuntimeException {
    public MessageDoesNotBelongToConversationException(String message) {
        super(message);
    }
}
