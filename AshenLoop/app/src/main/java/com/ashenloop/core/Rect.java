package com.ashenloop.core;

public final class Rect {
    public final float x;
    public final float y;
    public final float w;
    public final float h;

    public Rect(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public float right() {
        return x + w;
    }

    public float bottom() {
        return y + h;
    }

    public float centerX() {
        return x + w * 0.5f;
    }

    public float centerY() {
        return y + h * 0.5f;
    }

    public boolean contains(float px, float py) {
        return px >= x && px <= x + w && py >= y && py <= y + h;
    }

    public boolean intersectsBox(float bx, float by, float br) {
        return bx + br > x && bx - br < x + w && by + br > y && by - br < y + h;
    }
}
