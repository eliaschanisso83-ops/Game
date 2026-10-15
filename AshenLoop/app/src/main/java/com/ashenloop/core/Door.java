package com.ashenloop.core;

public final class Door {
    public final Rect rect;
    public boolean open;

    public Door(Rect rect) {
        this.rect = rect;
    }

    public void close() {
        open = false;
    }

    public void open() {
        open = true;
    }
}
