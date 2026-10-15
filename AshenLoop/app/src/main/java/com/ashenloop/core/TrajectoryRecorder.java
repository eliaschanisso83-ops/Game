package com.ashenloop.core;

public final class TrajectoryRecorder {
    private final Trajectory trajectory;
    private float nextSampleTime;
    private boolean started;

    public TrajectoryRecorder() {
        this(new Trajectory(GameConfig.MAX_SAMPLES));
    }

    public TrajectoryRecorder(Trajectory trajectory) {
        this.trajectory = trajectory;
    }

    public void clear() {
        trajectory.clear();
        nextSampleTime = 0f;
        started = false;
    }

    public boolean capture(float t, float x, float y) {
        if (trajectory.size() >= trajectory.maxSize()) {
            return false;
        }
        if (!started || t + 1e-6f >= nextSampleTime) {
            started = true;
            boolean ok = trajectory.add(t, x, y);
            if (ok) {
                nextSampleTime = t + GameConfig.SAMPLE_INTERVAL;
            }
            return ok;
        }
        return false;
    }

    public Trajectory trajectory() {
        return trajectory;
    }

    public int size() {
        return trajectory.size();
    }
}
