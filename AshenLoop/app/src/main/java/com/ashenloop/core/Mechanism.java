package com.ashenloop.core;

public final class Mechanism {
    public final float x;
    public final float y;
    public final float radius;
    public final float interactRadius;
    public final float chargeTime;
    public float charge;

    public Mechanism(float x, float y, float radius, float interactRadius, float chargeTime) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.interactRadius = interactRadius;
        this.chargeTime = chargeTime;
    }

    public void update(float dt, boolean shadowOnPlate) {
        if (shadowOnPlate) {
            charge += dt;
            if (charge > chargeTime) {
                charge = chargeTime;
            }
        }
    }

    public boolean isCharged() {
        return charge >= chargeTime - 1e-6f;
    }

    public float ratio() {
        if (chargeTime <= 0f) {
            return 1f;
        }
        float r = charge / chargeTime;
        return r < 0f ? 0f : (r > 1f ? 1f : r);
    }

    public boolean inInteractRange(float px, float py) {
        float dx = px - x;
        float dy = py - y;
        return dx * dx + dy * dy <= interactRadius * interactRadius;
    }

    public void reset() {
        charge = 0f;
    }
}
