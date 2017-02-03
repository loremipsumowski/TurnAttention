package pl.pniedziela.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import pl.pniedziela.entities.Player;
import pl.pniedziela.repositories.PlayerRepository;
import pl.pniedziela.entities.WSMessage;
import pl.pniedziela.enums.Colors;
import pl.pniedziela.enums.Directions;
import pl.pniedziela.enums.MessageType;
import pl.pniedziela.game.GameTimer;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Przemysław on 27.01.2017.
 */
@Component
public class SessionsManager {

    @Autowired
    GameTimer gameTimer;
    @Autowired
    PlayerRepository playerRepository;

    ObjectMapper mapper = new ObjectMapper();
    private final HashSet<WebSocketSession> sessions = new HashSet<>();
    private final HashMap<WebSocketSession, Player> sessionsPlayers = new HashMap<>();
    private final List<Player> players = new ArrayList<>();


    public void addSession(WebSocketSession webSocketSession, String username) {
        Player player = new Player(username);
        sessions.add(webSocketSession);
        sessionsPlayers.put(webSocketSession, player);
        players.add(player);
    }

    public void removeSession(WebSocketSession webSocketSession) {
        final Player player = sessionsPlayers.get(webSocketSession);
        playerRepository.save(player);
        Colors.pushColor(player.getColor());
        this.sessions.remove(webSocketSession);
        this.players.remove(player);
        this.sessionsPlayers.remove(webSocketSession);
    }

    public List<Player> getPlayers() {
        return this.players;
    }


    public boolean gameIsActive() {
        List<Player> result = players.stream()
                .filter(it -> true == it.isPlaying())
                .collect(Collectors.toList());

        return result.size() > 0;
    }

    public void setUserPlayingAndBeginCounting(WebSocketSession webSocketSession) {
        sessionsPlayers.get(webSocketSession).setPlaying(true);
        gameTimer.startCountDown();
    }

    public void sendMessageToAllSessions(MessageType messageType, String message) {
        try {
            for (WebSocketSession session : sessions) {
                sendMessageToSession(new TextMessage(mapper.writeValueAsString(new WSMessage(messageType, message))), session);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToAllSessions(WebSocketMessage<?> webSocketMessage) {
        for (WebSocketSession session : sessions) {
            sendMessageToSession(webSocketMessage, session);
        }
    }

    public void sendMessageToSession(MessageType messageType, String message, WebSocketSession session) {
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(new WSMessage(messageType, message))));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToSession(WebSocketMessage<?> webSocketMessage, WebSocketSession session) {
        try {
            session.sendMessage(webSocketMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPlayerDirection(Directions direction, WebSocketSession webSocketSession) {

        Player player = this.sessionsPlayers.get(webSocketSession);
        if (direction == Directions.getOppositeForDirection(player.getDirection())) {
            return;
        } else {
            player.setDirection(direction);
        }
    }

    public List<Player> getPlayingPlayers() {
        return players.stream()
                .filter(it -> true == it.isPlaying())
                .collect(Collectors.toList());
    }
}
