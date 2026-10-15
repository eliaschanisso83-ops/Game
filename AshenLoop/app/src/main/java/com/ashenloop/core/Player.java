package com.ashenloop.core;

public final class Player {
    public float x;
    public float y;
    public final float radius;
    public final float speed;

    public Player(float radius, float speed) {
        this.radius = radius;
        this.speed = speed;
        reset();
    }

    public void reset() {
        x = GameConfig.START_X;
        y = GameConfig.START_Y;
    }

    public void move(float dt, float ix, float iy, Arena arena) {
        float mag = (float) Math.sqrt(ix * ix + iy * iy);
        if (mag > 1f) {
            ix /= mag;
            iy /= mag;
        }
        arena.move(this, ix * speed, iy * speed, dt);
    }
}
