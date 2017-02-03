package pl.pniedziela.entities;

import pl.pniedziela.enums.MessageType;

/**
 * Created by Przemysław on 02.02.2017.
 */
public class WSMessage {

    private MessageType type;
    private String username;
    private String message;

    public WSMessage() {
    }

    public WSMessage(MessageType type, String message) {
        this.type = type;
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "WSMessage{" +
                "type='" + type + '\'' +
                ", username='" + username + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
