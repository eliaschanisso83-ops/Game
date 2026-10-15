package com.ashenloop.core;

public final class Shadow {
    public final Trajectory trajectory;
    public float x;
    public float y;
    public final float[] scratch = new float[2];

    public Shadow(Trajectory trajectory) {
        this.trajectory = trajectory;
        if (trajectory.size() > 0) {
            this.x = trajectory.xAt(0);
            this.y = trajectory.yAt(0);
        }
    }

    public void sampleAtTime(float cycleTime) {
        trajectory.sampleAt(cycleTime, scratch);
        x = scratch[0];
        y = scratch[1];
    }

    public boolean active() {
        return trajectory.size() > 0;
    }
}
