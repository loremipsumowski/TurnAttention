package pl.pniedziela.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.pniedziela.entities.Player;
import pl.pniedziela.enums.Directions;
import pl.pniedziela.enums.MessageType;
import pl.pniedziela.handlers.SessionsManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Przemysław on 02.02.2017.
 */
@Component
public class GameTimer {

    @Autowired
    SessionsManager sessionManager;
    @Autowired
    ObjectMapper objectMapper;

    private static Timer gameTimer = null;
    private static Timer countTimer = null;
    private static int countTimercount = 5;
    private static final int BOARD_WIDTH = 700;
    private static final int BOARD_HEIGHT = 700;
    private static int[][] checkedFields = new int[BOARD_WIDTH][BOARD_HEIGHT];

    public void startCountDown() {
        countTimercount = 5;
        countTimer = new Timer(SessionsManager.class.getSimpleName() + " Timer");
        countTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                try {
                    sessionManager.sendMessageToAllSessions(MessageType.TEXTTOBOARD, Integer.toString(countTimercount--) + "!!!");
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
                if (countTimercount < 0) {
                    startGameTimer();
                    countTimer.cancel();
                }
            }
        }, 0, 1000);
    }

    private void startGameTimer() {
        this.checkedFields = new int[BOARD_WIDTH][BOARD_HEIGHT];
        randomPlayersPositionsAndDirections();
        try {
            sessionManager.sendMessageToAllSessions(MessageType.INITSNAKES, objectMapper.writeValueAsString(sessionManager.getPlayers()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        gameTimer = new Timer(SessionsManager.class.getSimpleName() + " Timer");
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                List<Player> playingPlayers = sessionManager.getPlayingPlayers();

                if (playingPlayers.size() == 1 && sessionManager.getPlayers().size() != 1) {
                    playingPlayers.get(0).addWin();
                    playingPlayers.get(0).setPlaying(false);
                }

                for (Player player : playingPlayers) {
                    switch (player.getDirection()) {
                        case UP:
                            player.decrementV();
                            break;
                        case DOWN:
                            player.incrementV();
                            break;
                        case RIGHT:
                            player.incrementH();
                            break;
                        case LEFT:
                            player.decrementH();
                            break;
                    }
                    if (checkPlayerIsOnBoard(player) && checkedFields[player.getH()][player.getV()] != 1) {
                        checkedFields[player.getH()][player.getV()] = 1;
                    } else {
                        player.setPlaying(false);
                    }
                }

                try {
                    sessionManager.sendMessageToAllSessions(MessageType.DRAWSNAKES, objectMapper.writeValueAsString(sessionManager.getPlayers()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                if (!sessionManager.gameIsActive()) {
                    sessionManager.sendMessageToAllSessions(MessageType.ENDROUND, "");
                    this.cancel();
                }
            }
        }, 0, 10);
    }

    private boolean checkPlayerIsOnBoard(Player player) {
        return player.getH() >= 0 && player.getH() <= BOARD_WIDTH - 1 && player.getV() >= 0 && player.getV() <= BOARD_HEIGHT - 1;
    }

    private void randomPlayersPositionsAndDirections() {
        for (Player player : sessionManager.getPlayers()) {
            player.setPlaying(true);
            player.addGame();
            player.setV(randomWithRange(BOARD_HEIGHT / 10, BOARD_HEIGHT - BOARD_HEIGHT / 10));
            player.setH(randomWithRange(BOARD_WIDTH / 10, BOARD_WIDTH - BOARD_WIDTH / 10));
            player.setDirection(Directions.randomDirection());
            checkedFields[player.getH()][player.getV()] = 1;
        }
    }

    private int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }
}
