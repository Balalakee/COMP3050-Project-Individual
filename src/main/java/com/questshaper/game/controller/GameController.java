package com.questshaper.game.controller;

import com.questshaper.game.service.GameService;
import com.questshaper.game.service.MapService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
@RestController
@RequestMapping("/")
public class GameController {

    private final GameService gameService;

    public GameController() {
        MapService mapService = new MapService();
        this.gameService = new GameService(mapService);
    }

    // MOVE endpoint
    @GetMapping("/move")
    public ResponseEntity<?> move(
            @RequestParam int dy,
            @RequestParam int dx,
            @RequestParam(required = false) String session
    ) {
        boolean moved = gameService.move(dy, dx);

        if (!moved) {
            // 204 = blocked (your frontend expects this)
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.ok("moved");
    }

    // INFO endpoint
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info(
            @RequestParam int y,
            @RequestParam int x,
            @RequestParam(required = false) String session
    ) {
        Map<String, Object> result = gameService.getInfo(y, x);
        return ResponseEntity.ok(result);
    }
}