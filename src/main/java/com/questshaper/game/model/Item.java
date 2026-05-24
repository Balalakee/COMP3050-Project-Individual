package com.questshaper.game.model;

public class Item {

    private final ItemType type;

    public Item(ItemType type) {
        this.type = type;
    }

    public ItemType getType() {
        return type;
    }
}
