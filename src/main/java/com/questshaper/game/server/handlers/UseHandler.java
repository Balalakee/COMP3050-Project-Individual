package com.questshaper.game.server.handlers;

import com.questshaper.game.model.Player;
import com.questshaper.game.service.GameService;
import com.questshaper.game.server.RequestUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

public class UseHandler implements HttpHandler {

    private final GameService game;

    public UseHandler(GameService game) {
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

        Integer dy =
                RequestUtil.parseIntParam(params, "dy", 0);

        Integer dx =
                RequestUtil.parseIntParam(params, "dx", 0);

        if (dy == null || dx == null) {
            RequestUtil.sendNoContent(exchange);
            return;
        }

        if (!game.use(player, dy, dx)) {
            RequestUtil.sendNoContent(exchange);
            return;
        }

        RequestUtil.sendText(exchange, 200, "");
    }
}