package com.questshaper.game.server.handlers;

import com.questshaper.game.service.GameService;
import com.questshaper.game.util.JsonUtil;
import com.questshaper.game.server.RequestUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class LoginHandler implements HttpHandler {

    private final GameService game;

    public LoginHandler(GameService game) {
        this.game = game;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (RequestUtil.handleOptions(exchange, "POST")) return;
        RequestUtil.addCorsHeaders(exchange, "POST");

        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            RequestUtil.sendMethodNotAllowed(exchange);
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> json = JsonUtil.parseStringObject(body);

        String name = json.get("name");
        String encpswrd = json.get("encpswrd");

        if (name == null || encpswrd == null || !GameService.isValidPlayerName(name)) {
            RequestUtil.sendBadRequest(exchange, "missing or invalid name/password");
            return;
        }

        String session = game.login(name, encpswrd);

        if (session == null) {
            RequestUtil.sendUnauthorized(exchange);
            return;
        }

        RequestUtil.sendJson(
                exchange,
                200,
                "{\"session\":" + JsonUtil.quote(session) + "}"
        );
    }
}