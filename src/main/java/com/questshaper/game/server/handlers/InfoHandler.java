package com.questshaper.game.server.handlers;

import com.questshaper.game.model.Player;
import com.questshaper.game.service.GameService;
import com.questshaper.game.server.RequestUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

public class InfoHandler implements HttpHandler {

    private final GameService game;

    public InfoHandler(GameService game) {
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

        String session =
                RequestUtil.extractSession(exchange, params);

        Player player =
                game.getPlayer(session);

        if (player == null) {
            RequestUtil.sendUnauthorized(exchange);
            return;
        }

        Integer y =
                RequestUtil.parseIntParam(params, "y", 0);

        Integer x =
                RequestUtil.parseIntParam(params, "x", 0);

        if (y == null || x == null) {
            RequestUtil.sendNoContent(exchange);
            return;
        }

        String json =
                game.infoJson(player, y, x);

        if (json == null) {
            RequestUtil.sendNoContent(exchange);
            return;
        }

        RequestUtil.sendJson(exchange, 200, json);
    }
}