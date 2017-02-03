package pl.pniedziela.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.pniedziela.entities.Player;
import pl.pniedziela.handlers.SessionsManager;

import java.util.List;

/**
 * Created by Przemysław on 02.02.2017.
 */
@RestController
@RequestMapping(value = "/rest")
public class GameController {

    @Autowired
    SessionsManager sessionsManager;

    @RequestMapping(value = "/players", method = RequestMethod.GET)
    public List<Player> getPlayers() {
        return sessionsManager.getPlayers();
    }
}
