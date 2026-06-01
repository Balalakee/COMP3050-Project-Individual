package com.questshaper.game.server.handlers;

import com.questshaper.game.service.GameService;
import com.questshaper.game.server.RequestUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

public class LogoutHandler implements HttpHandler {

    private final GameService game;

    public LogoutHandler(GameService game) {
        this.game = game;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (RequestUtil.handleOptions(exchange, "GET, POST")) return;
        RequestUtil.addCorsHeaders(exchange, "GET, POST");

        String method = exchange.getRequestMethod();

        if (!"GET".equalsIgnoreCase(method) && !"POST".equalsIgnoreCase(method)) {
            RequestUtil.sendMethodNotAllowed(exchange);
            return;
        }

        Map<String, String> params =
                RequestUtil.queryToMap(exchange.getRequestURI().getQuery());

        String session =
                RequestUtil.extractSession(exchange, params);

        if (!game.logout(session)) {
            RequestUtil.sendUnauthorized(exchange);
            return;
        }

        RequestUtil.sendText(exchange, 200, "");
    }
}