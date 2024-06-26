package com.example.ApiGateway;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WelcomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/redirect")
    public String redirect(@RequestParam String service) {
        if ("CarPick1".equals(service)) {
            return "redirect:http://localhost:8081/";
        } else if ("CarPickKia1".equals(service)) {
            return "redirect:http://localhost:8082/";
        }
        return "index";
    }
}