package com.ashenloop.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MechanismTest {

    @Test
    public void switchPlateDetectsOverlap() {
        SwitchPlate plate = new SwitchPlate(700f, 620f, 110f);
        assertTrue(plate.contains(700f, 620f));
        assertTrue(plate.contains(700f, 720f));
        assertFalse(plate.contains(700f, 740f));
    }

    @Test
    public void mechanismChargesOnlyWhenShadowOnPlate() {
        Mechanism m = new Mechanism(450f, 900f, 70f, 150f, 0.6f);
        m.update(0.3f, false);
        assertFalse(m.isCharged());

        m.update(0.3f, true);
        assertFalse(m.isCharged());

        m.update(0.3f, true);
        assertTrue(m.isCharged());
    }

    @Test
    public void mechanismResets() {
        Mechanism m = new Mechanism(450f, 900f, 70f, 150f, 0.6f);
        m.update(1f, true);
        assertTrue(m.isCharged());
        m.reset();
        assertFalse(m.isCharged());
        assertTrue(m.ratio() == 0f);
    }

    @Test
    public void interactRangeChecksDistance() {
        Mechanism m = new Mechanism(450f, 900f, 70f, 150f, 0.6f);
        assertTrue(m.inInteractRange(450f, 900f));
        assertTrue(m.inInteractRange(450f, 1040f));
        assertFalse(m.inInteractRange(450f, 1060f));
    }
}
