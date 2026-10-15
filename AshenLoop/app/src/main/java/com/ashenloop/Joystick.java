package com.ashenloop;

public final class Joystick {
    public float anchorX;
    public float anchorY;
    public float curX;
    public float curY;
    public float maxRadius;
    public boolean active;
    public float outX;
    public float outY;

    public void begin(float x, float y, float radius) {
        anchorX = x;
        anchorY = y;
        curX = x;
        curY = y;
        maxRadius = radius;
        active = true;
        outX = 0f;
        outY = 0f;
    }

    public void move(float x, float y) {
        if (!active) {
            return;
        }
        curX = x;
        curY = y;
        float dx = curX - anchorX;
        float dy = curY - anchorY;
        float mag = (float) Math.sqrt(dx * dx + dy * dy);
        if (mag < 1e-4f) {
            outX = 0f;
            outY = 0f;
            return;
        }
        float clamped = Math.min(mag, maxRadius);
        float nx = dx / mag * (clamped / maxRadius);
        float ny = dy / mag * (clamped / maxRadius);
        outX = nx;
        outY = ny;
    }

    public void end() {
        active = false;
        outX = 0f;
        outY = 0f;
    }
}
