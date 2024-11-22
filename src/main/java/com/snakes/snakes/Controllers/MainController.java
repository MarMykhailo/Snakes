package com.snakes.snakes.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

    @GetMapping("/statistics")
    public String statistics() {
        return "statistics.html";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings.html";
    }
}
