package pl.pniedziela.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import pl.pniedziela.entities.WSMessage;
import pl.pniedziela.enums.Directions;
import pl.pniedziela.enums.MessageType;

import javax.websocket.Session;
import java.util.HashSet;

/**
 * Created by Przemysław on 27.01.2017.
 */
public class SessionHandler implements WebSocketHandler {

    @Autowired
    SessionsManager sessionsManager;
    @Autowired
    ObjectMapper mapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {

    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {

        try {
            WSMessage wsm = mapper.readValue((String) webSocketMessage.getPayload(), WSMessage.class);
            switch (wsm.getType()) {
                case NEWPLAYER:
                    sessionsManager.addSession(webSocketSession, wsm.getUsername());
                    if (!sessionsManager.gameIsActive()) {
                        sessionsManager.sendMessageToAllSessions(MessageType.TEXTTOBOARD, "Wciśnij spację, aby rozpocząć grę!");
                    }
                    break;
                case MESSAGE:
                    sessionsManager.sendMessageToAllSessions(new TextMessage(mapper.writeValueAsString(wsm)));
                    break;
                case GAMEEVENT:
                    if (wsm.getMessage().equals("UP")) {
                        sessionsManager.setPlayerDirection(Directions.UP, webSocketSession);
                    } else if (wsm.getMessage().equals("RIGHT")) {
                        sessionsManager.setPlayerDirection(Directions.RIGHT, webSocketSession);
                    } else if (wsm.getMessage().equals("DOWN")) {
                        sessionsManager.setPlayerDirection(Directions.DOWN, webSocketSession);
                    } else if (wsm.getMessage().equals("LEFT")) {
                        sessionsManager.setPlayerDirection(Directions.LEFT, webSocketSession);
                    } else if (wsm.getMessage().equals("SPACE") && !sessionsManager.gameIsActive()) {
                        sessionsManager.setUserPlayingAndBeginCounting(webSocketSession);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        sessionsManager.removeSession(webSocketSession);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
