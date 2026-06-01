package com.questshaper.game.server.handlers;

import com.questshaper.game.model.Player;
import com.questshaper.game.service.GameService;
import com.questshaper.game.server.RequestUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

public class PlaceHandler implements HttpHandler {

    private final GameService game;

    public PlaceHandler(GameService game) {
        this.game = game;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (RequestUtil.handleOptions(exchange, "GET")) return;
        RequestUtil.addCorsHeaders(exchange, "GET");

        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            RequestUtil.sendMethodNotAllowed(exchange);
            return;
        }

        Map<String, String> params =
                RequestUtil.queryToMap(exchange.getRequestURI().getQuery());

        Player player =
                game.getPlayer(
                        RequestUtil.extractSession(exchange, params)
                );

        if (player == null) {
            RequestUtil.sendUnauthorized(exchange);
            return;
        }

        if (!game.place(player)) {
            RequestUtil.sendNoContent(exchange);
            return;
        }

        RequestUtil.sendText(exchange, 200, "");
    }
}