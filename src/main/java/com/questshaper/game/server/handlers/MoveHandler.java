package com.questshaper.game.server.handlers;

import com.questshaper.game.model.Player;
import com.questshaper.game.service.GameService;
import com.questshaper.game.server.RequestUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

public class MoveHandler implements HttpHandler {

    private final GameService game;

    public MoveHandler(GameService game) {
        this.game = game;
    }

    boolean is_blocking(int y, int x) {
        return game.isBlockingForTests(y, x);
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

        Integer dy =
                RequestUtil.parseIntParam(params, "dy", 0);

        Integer dx =
                RequestUtil.parseIntParam(params, "dx", 0);

        if (dy == null || dx == null) {
            RequestUtil.sendNoContent(exchange);
            return;
        }

        boolean moved =
                game.move(player, dy, dx);

        if (!moved) {
            RequestUtil.sendNoContent(exchange);
            return;
        }

        String response =
                "{\"y\":" + player.getY() +
                ",\"x\":" + player.getX() + "}";

        RequestUtil.sendJson(exchange, 200, response);
    }
}