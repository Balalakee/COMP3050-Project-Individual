package com.questshaper.game.util;

import java.util.HashMap;
import java.util.Map;

public final class JsonUtil {

    private JsonUtil() {
    }

    public static Map<String, String> parseStringObject(String body) {
        Map<String, String> result = new HashMap<>();

        if (body == null) {
            return result;
        }

        String text = body.trim();

        if (!text.startsWith("{") ||
            !text.endsWith("}")) {
            return result;
        }

        text =
                text.substring(
                        1,
                        text.length() - 1
                ).trim();

        if (text.isEmpty()) {
            return result;
        }

        int index = 0;

        while (index < text.length()) {

            ParsedString key =
                    parseString(text, index);

            if (key == null) {
                return result;
            }

            index =
                    skipWhitespace(
                            text,
                            key.nextIndex);

            if (index >= text.length() ||
                text.charAt(index) != ':') {
                return result;
            }

            index =
                    skipWhitespace(
                            text,
                            index + 1);

            ParsedString value =
                    parseString(text, index);

            if (value == null) {
                return result;
            }

            result.put(
                    key.value,
                    value.value);

            index =
                    skipWhitespace(
                            text,
                            value.nextIndex);

            if (index >= text.length()) {
                break;
            }

            if (text.charAt(index) != ',') {
                return result;
            }

            index =
                    skipWhitespace(
                            text,
                            index + 1);
        }

        return result;
    }

    public static String quote(String value) {
        StringBuilder out =
                new StringBuilder("\"");

        if (value == null) {
            value = "";
        }

        for (int i = 0;
             i < value.length();
             i++) {

            char ch = value.charAt(i);

            if (ch == '"' || ch == '\\') {
                out.append('\\').append(ch);
            } else if (ch == '\n') {
                out.append("\\n");
            } else if (ch == '\r') {
                out.append("\\r");
            } else if (ch == '\t') {
                out.append("\\t");
            } else {
                out.append(ch);
            }
        }

        out.append('"');

        return out.toString();
    }

    private static ParsedString parseString(
            String text,
            int index) {

        index =
                skipWhitespace(
                        text,
                        index);

        if (index >= text.length() ||
            text.charAt(index) != '"') {
            return null;
        }

        StringBuilder value =
                new StringBuilder();

        index++;

        while (index < text.length()) {

            char ch =
                    text.charAt(index++);

            if (ch == '"') {
                return new ParsedString(
                        value.toString(),
                        index);
            }

            if (ch == '\\') {

                if (index >= text.length()) {
                    return null;
                }

                char escaped =
                        text.charAt(index++);

                switch (escaped) {

                    case '"':
                    case '\\':
                    case '/':
                        value.append(escaped);
                        break;

                    case 'n':
                        value.append('\n');
                        break;

                    case 'r':
                        value.append('\r');
                        break;

                    case 't':
                        value.append('\t');
                        break;

                    default:
                        value.append(escaped);
                        break;
                }

            } else {
                value.append(ch);
            }
        }

        return null;
    }

    private static int skipWhitespace(
            String text,
            int index) {

        while (index < text.length() &&
               Character.isWhitespace(
                       text.charAt(index))) {

            index++;
        }

        return index;
    }

    private static class ParsedString {

        private final String value;
        private final int nextIndex;

        private ParsedString(
                String value,
                int nextIndex) {

            this.value = value;
            this.nextIndex = nextIndex;
        }
    }
}