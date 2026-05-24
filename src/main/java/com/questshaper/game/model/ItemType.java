package com.questshaper.game.model;

public enum ItemType {

    AXE('a', "tool"),
    CYAN_POTION('c', "drink"),
    HEART_POTION('h', "drink"),
    KEY('k', "artifact");

    private final char code;
    private final String itemClass;

    ItemType(char code, String itemClass) {
        this.code = code;
        this.itemClass = itemClass;
    }
}
