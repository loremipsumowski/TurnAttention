package pl.pniedziela.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pl.pniedziela.enums.Colors;
import pl.pniedziela.enums.Directions;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Przemysław on 01.02.2017.
 */
@Entity
public class Player {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private int games;
    private int winners;
    private String color;
    private boolean playing;
    private int V;
    private int H;
    private Directions direction;

    public Player(String username) {
        this.username = username;
        this.color = Colors.popColor();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addGame() {
        this.games++;
    }

    public void addWin() {
        this.winners++;
    }

    public String getUsername() {
        return this.username;
    }

    public int getGames() {
        return this.games;
    }

    public int getWinners() {
        return this.winners;
    }

    public String getColor() {
        return this.color;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public int getV() {
        return V;
    }

    public void setV(int v) {
        V = v;
    }

    public int getH() {
        return H;
    }

    public void setH(int h) {
        H = h;
    }

    public Directions getDirection() {
        return direction;
    }

    public void setDirection(Directions direction) {
        this.direction = direction;
    }

    @JsonIgnore
    public boolean isPlaying() {
        return this.playing;
    }

    public void incrementV() {
        this.V++;
    }

    public void decrementV() {
        this.V--;
    }

    public void incrementH() {
        this.H++;
    }

    public void decrementH() {
        this.H--;
    }
}
