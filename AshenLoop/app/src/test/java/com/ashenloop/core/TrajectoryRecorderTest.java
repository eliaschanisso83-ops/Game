package com.ashenloop.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TrajectoryRecorderTest {

    @Test
    public void capturesOnlyAtSampleInterval() {
        TrajectoryRecorder r = new TrajectoryRecorder();
        r.capture(0f, 0f, 0f);
        r.capture(0.01f, 1f, 1f);
        r.capture(0.05f, 2f, 2f);

        assertEquals(2, r.size());
        assertEquals(0f, r.trajectory().xAt(0), 1e-4f);
        assertEquals(2f, r.trajectory().xAt(1), 1e-4f);
    }

    @Test
    public void stopsAtMaxSamples() {
        TrajectoryRecorder r = new TrajectoryRecorder(new Trajectory(10));
        for (int i = 0; i < 100; i++) {
            r.capture(i * 0.1f, i, i);
        }
        assertEquals(10, r.size());
    }

    @Test
    public void clearResetsRecorder() {
        TrajectoryRecorder r = new TrajectoryRecorder();
        r.capture(0f, 5f, 5f);
        r.capture(0.2f, 6f, 6f);
        r.clear();
        assertEquals(0, r.size());
        r.capture(0f, 1f, 1f);
        assertEquals(1, r.size());
    }
}
