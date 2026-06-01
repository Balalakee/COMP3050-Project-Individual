package com.questshaper.game.server;

import com.questshaper.game.util.JsonUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class RequestUtil {

    private RequestUtil() {
    }

    public static void addCorsHeaders(HttpExchange exchange, String methods) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", methods + ", OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    public static boolean handleOptions(HttpExchange exchange, String methods) throws IOException {
        addCorsHeaders(exchange, methods);

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(200, -1);
            exchange.close();
            return true;
        }

        return false;
    }

    public static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();

        if (query == null || query.isEmpty()) {
            return result;
        }

        for (String param : query.split("&")) {
            String[] entry = param.split("=", 2);

            if (entry.length == 2) {
                result.put(
                        decode(entry[0]),
                        decode(entry[1])
                );
            }
        }

        return result;
    }

    public static Integer parseIntParam(
            Map<String, String> params,
            String name,
            int defaultValue) {

        String value = params.get(name);

        if (value == null || value.isEmpty()) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static String extractSession(
            HttpExchange exchange,
            Map<String, String> params) {

        String auth =
                exchange.getRequestHeaders()
                        .getFirst("Authorization");

        if (auth != null &&
            auth.startsWith("Bearer ")) {

            return auth.substring(7);
        }

        return params.get("session");
    }

    public static void sendJson(
            HttpExchange exchange,
            int status,
            String body) throws IOException {

        exchange.getResponseHeaders()
                .set("Content-Type",
                        "application/json; charset=UTF-8");

        send(exchange, status, body);
    }

    public static void sendText(
            HttpExchange exchange,
            int status,
            String body) throws IOException {

        exchange.getResponseHeaders()
                .set("Content-Type",
                        "text/plain; charset=UTF-8");

        send(exchange, status, body);
    }

    public static void sendNoContent(
            HttpExchange exchange) throws IOException {

        exchange.sendResponseHeaders(204, -1);
        exchange.close();
    }

    public static void sendUnauthorized(
            HttpExchange exchange) throws IOException {

        sendJson(exchange, 401,
                "{\"error\":\"unauthorized\"}");
    }

    public static void sendBadRequest(
            HttpExchange exchange,
            String message) throws IOException {

        sendJson(exchange, 400,
                "{\"error\":" +
                        JsonUtil.quote(message) +
                        "}");
    }

    public static void sendMethodNotAllowed(
            HttpExchange exchange) throws IOException {

        sendJson(exchange, 405,
                "{\"error\":\"method not allowed\"}");
    }

    private static String decode(String value) {
        return URLDecoder.decode(
                value.replace("+", "%2B"),
                StandardCharsets.UTF_8
        );
    }

    private static void send(
            HttpExchange exchange,
            int status,
            String body) throws IOException {

        byte[] bytes =
                body.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(
                status,
                bytes.length);

        try (OutputStream os =
                     exchange.getResponseBody()) {

            os.write(bytes);
        }
    }
}