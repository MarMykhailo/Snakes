package com.snakes.snakes.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GameController {

    @GetMapping("/game")
    public String game() {
        return "snakes.html"; // поверне game.html з папки templates
    }
}
