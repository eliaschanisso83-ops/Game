package com.ashenloop.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TrajectoryTest {

    @Test
    public void recordsPositionsInSequence() {
        Trajectory t = new Trajectory();
        t.add(0f, 10f, 20f);
        t.add(0.5f, 30f, 40f);

        assertEquals(2, t.size());
        assertEquals(0f, t.timeAt(0), 1e-4f);
        assertEquals(10f, t.xAt(0), 1e-4f);
        assertEquals(20f, t.yAt(0), 1e-4f);
        assertEquals(0.5f, t.timeAt(1), 1e-4f);
        assertEquals(30f, t.xAt(1), 1e-4f);
        assertEquals(40f, t.yAt(1), 1e-4f);
        assertEquals(0.5f, t.duration(), 1e-4f);
    }

    @Test
    public void keepsTemporalOrderAndRejectsOlderSamples() {
        Trajectory t = new Trajectory();
        assertTrue(t.add(0f, 0f, 0f));
        assertTrue(t.add(1f, 1f, 1f));
        assertFalse(t.add(0.5f, 9f, 9f));

        assertEquals(2, t.size());
        assertTrue(t.isTemporallyOrdered());
    }

    @Test
    public void reproducesInterpolatedPosition() {
        Trajectory t = new Trajectory();
        t.add(0f, 0f, 0f);
        t.add(1f, 100f, 50f);

        float[] out = new float[2];
        t.sampleAt(0.25f, out);
        assertEquals(25f, out[0], 1e-3f);
        assertEquals(12.5f, out[1], 1e-3f);

        t.sampleAt(0.75f, out);
        assertEquals(75f, out[0], 1e-3f);
        assertEquals(37.5f, out[1], 1e-3f);
    }

    @Test
    public void loopsWhenTimeExceedsDuration() {
        Trajectory t = new Trajectory();
        t.add(0f, 0f, 0f);
        t.add(1f, 100f, 0f);

        float[] a = new float[2];
        float[] b = new float[2];
        t.sampleAt(0.25f, a);
        t.sampleAt(1.25f, b);
        assertEquals(a[0], b[0], 1e-3f);
        assertEquals(a[1], b[1], 1e-3f);
    }

    @Test
    public void respectsMaxSampleCapacity() {
        Trajectory t = new Trajectory(10);
        for (int i = 0; i < 100; i++) {
            t.add(i * 0.1f, i, i);
        }
        assertEquals(10, t.size());
        assertEquals(10, t.maxSize());
    }
}
