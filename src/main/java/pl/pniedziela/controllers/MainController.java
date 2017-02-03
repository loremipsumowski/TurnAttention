package pl.pniedziela.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.socket.TextMessage;
import pl.pniedziela.handlers.SessionsManager;

/**
 * Created by Przemysław on 26.01.2017.
 */
@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    SessionsManager sessionsManager;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String loginPage() {

        return "loginPage";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String mainPage(@RequestParam String username, Model model) {
System.out.println("POST");
        model.addAttribute("playerNickname", username);
        return "mainPage";
    }
}
