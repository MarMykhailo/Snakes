package com.snakes.snakes.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.snakes.snakes.Services.GameService;

@Controller
public class GamesController {
    private final GameService gameService;

    @Autowired
    public GamesController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/snakes")
    public String snakes() {
        return "snakes.html";
    }
}
