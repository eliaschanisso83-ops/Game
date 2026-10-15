package com.ashenloop.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ShadowTest {

    @Test
    public void reproducesRecordedPath() {
        Trajectory t = new Trajectory();
        t.add(0f, 0f, 0f);
        t.add(1f, 100f, 50f);

        Shadow s = new Shadow(t);
        s.sampleAtTime(0.25f);
        assertEquals(25f, s.x, 1e-3f);
        assertEquals(12.5f, s.y, 1e-3f);

        s.sampleAtTime(0.75f);
        assertEquals(75f, s.x, 1e-3f);
        assertEquals(37.5f, s.y, 1e-3f);
    }

    @Test
    public void loopsItsCycle() {
        Trajectory t = new Trajectory();
        t.add(0f, 0f, 0f);
        t.add(1f, 100f, 0f);

        Shadow s = new Shadow(t);
        s.sampleAtTime(0.25f);
        float first = s.x;
        s.sampleAtTime(1.25f);
        assertEquals(first, s.x, 1e-3f);
    }

    @Test
    public void emptyTrajectoryIsInactive() {
        Shadow s = new Shadow(new Trajectory());
        assertFalse(s.active());
    }

    @Test
    public void singleSampleIsActive() {
        Trajectory t = new Trajectory();
        t.add(0f, 42f, 7f);
        Shadow s = new Shadow(t);
        assertTrue(s.active());
        s.sampleAtTime(5f);
        assertEquals(42f, s.x, 1e-3f);
        assertEquals(7f, s.y, 1e-3f);
    }
}
