package com.questshaper.game.controller;

import com.questshaper.game.service.GameService;
import com.questshaper.game.model.Player;
import com.questshaper.game.model.LoginResponse;
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

    if (name == null || encpswrd == null || name.isBlank() || encpswrd.isBlank()) {
        return ResponseEntity.badRequest().body("Missing name or encpswrd");
    }

    String sessionId = gameService.login(name, encpswrd);

    if (sessionId == null) {
        return ResponseEntity.status(401).body("Server Full");
    }

    Player player = gameService.getPlayer(sessionId);

    return ResponseEntity.ok(
            Map.of(
                    "session", sessionId,
                    "x", player.getX(),
                    "y", player.getY()
            ));
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

    @GetMapping("/take")
public ResponseEntity<?> take(
        @RequestParam String session) {

    if (!gameService.isValidSession(session)) {
        return ResponseEntity.status(401).build();
    }

    return gameService.take(session)
            ? ResponseEntity.ok().build()
            : ResponseEntity.noContent().build();
}

@GetMapping("/use")
public ResponseEntity<?> use(
        @RequestParam String session,
        @RequestParam(defaultValue="0") int dy,
        @RequestParam(defaultValue="0") int dx) {

    if (!gameService.isValidSession(session)) {
        return ResponseEntity.status(401).build();
    }

    return gameService.use(session, dy, dx)
            ? ResponseEntity.ok().build()
            : ResponseEntity.noContent().build();
}

@GetMapping("/place")
public ResponseEntity<?> place(
        @RequestParam String session) {

    if (!gameService.isValidSession(session)) {
        return ResponseEntity.status(401).build();
    }

    return gameService.place(session)
            ? ResponseEntity.ok().build()
            : ResponseEntity.noContent().build();
}
}