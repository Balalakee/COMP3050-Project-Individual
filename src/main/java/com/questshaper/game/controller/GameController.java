package com.questshaper.game.controller;

import com.questshaper.game.service.GameService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {

    String name = body.get("name");
    String encpswrd = body.get("encpswrd");

    if (name == null || encpswrd == null) {
        return ResponseEntity.badRequest().body("Missing name or encpswrd");
    }

    String sessionId = gameService.createSession(name);

    if (sessionId == null) {
        return ResponseEntity.status(401).build();
    }

    Map<String, String> response = new HashMap<>();
    response.put("session", sessionId);

    return ResponseEntity.ok(response);
}


    // LOGOUT   
   @GetMapping("/logout")
public ResponseEntity<?> logout(@RequestParam String session) {
    gameService.logout(session);
    return ResponseEntity.ok().build();
}

    // MOVE
    @GetMapping("/move")
    public ResponseEntity<?> move(
            @RequestParam String session,
            @RequestParam int dy,
            @RequestParam int dx
    ) {
        boolean moved = gameService.move(session, dy, dx);

        if (!moved) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.ok().build();
    }

    // INFO
    @GetMapping("/info")
    public ResponseEntity<?> info(
            @RequestParam String session,
            @RequestParam int y,
            @RequestParam int x
    ) {
        var result = gameService.getInfo(session, y, x);

        if (result == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(result);
    }
}