package com.ashenloop.core;

public final class SwitchPlate {
    public final float x;
    public final float y;
    public final float radius;
    public boolean litByPlayer;
    public boolean litByShadow;

    public SwitchPlate(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public boolean contains(float px, float py) {
        float dx = px - x;
        float dy = py - y;
        return dx * dx + dy * dy <= radius * radius;
    }

    public void reset() {
        litByPlayer = false;
        litByShadow = false;
    }
}
