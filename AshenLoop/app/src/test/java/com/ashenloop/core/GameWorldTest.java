package com.ashenloop.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GameWorldTest {

    @Test
    public void startsInMenu() {
        GameWorld w = new GameWorld();
        assertEquals(GameState.MENU, w.state);
    }

    @Test
    public void startButtonBeginsPlaying() {
        GameWorld w = new GameWorld();
        InputState in = new InputState();
        in.startPressed = true;
        w.update(GameConfig.FIXED_DT, in);
        assertEquals(GameState.PLAYING, w.state);
        assertEquals(1, w.cycle);
    }

    @Test
    public void playerOnPlateDoesNotChargeMechanism() {
        GameWorld w = new GameWorld();
        w.resetLevel();
        InputState in = new InputState();
        w.player.x = GameConfig.PLATE_X;
        w.player.y = GameConfig.PLATE_Y;
        w.update(0.1f, in);

        assertTrue(w.playerOnPlate);
        assertFalse(w.shadowOnPlate);
        assertEquals(0f, w.mechanism.charge, 1e-4f);
    }

    @Test
    public void shadowChargesMechanismNextCycleAndDoorOpens() {
        GameWorld w = new GameWorld();
        w.resetLevel();
        InputState in = new InputState();

        w.player.x = GameConfig.PLATE_X;
        w.player.y = GameConfig.PLATE_Y;
        for (int i = 0; i < 60; i++) {
            w.player.x = GameConfig.PLATE_X;
            w.player.y = GameConfig.PLATE_Y;
            w.update(GameConfig.FIXED_DT, in);
        }
        w.endCycle();
        assertTrue(w.shadowPresent);

        int guard = 0;
        while (w.state != GameState.PLAYING && guard++ < 1000) {
            w.update(GameConfig.FIXED_DT, in);
        }
        assertEquals(GameState.PLAYING, w.state);
        assertEquals(2, w.cycle);
        assertEquals(0f, w.mechanism.charge, 1e-4f);

        for (int i = 0; i < 40; i++) {
            w.update(GameConfig.FIXED_DT, in);
        }
        assertTrue(w.shadowOnPlate);
        assertTrue(w.mechanism.isCharged());

        w.player.x = GameConfig.MECH_X;
        w.player.y = GameConfig.MECH_Y;
        in.interactPressed = true;
        w.update(GameConfig.FIXED_DT, in);
        assertTrue(w.door.open);
    }

    @Test
    public void reachingExitWithOpenDoorCompletesLevel() {
        GameWorld w = new GameWorld();
        w.resetLevel();
        w.door.open();
        InputState in = new InputState();
        w.player.x = GameConfig.EXIT_X + GameConfig.EXIT_W * 0.5f;
        w.player.y = GameConfig.EXIT_Y + GameConfig.EXIT_H * 0.5f;
        w.update(GameConfig.FIXED_DT, in);
        assertEquals(GameState.LEVEL_COMPLETE, w.state);
    }

    @Test
    public void exitIsLockedWhenDoorClosed() {
        GameWorld w = new GameWorld();
        w.resetLevel();
        InputState in = new InputState();
        w.player.x = GameConfig.EXIT_X + GameConfig.EXIT_W * 0.5f;
        w.player.y = GameConfig.EXIT_Y + GameConfig.EXIT_H * 0.5f;
        w.update(GameConfig.FIXED_DT, in);
        assertEquals(GameState.PLAYING, w.state);
    }

    @Test
    public void restartClearsEverything() {
        GameWorld w = new GameWorld();
        w.resetLevel();
        InputState in = new InputState();
        w.mechanism.update(1f, true);
        w.door.open();
        w.player.x = 123f;
        w.player.y = 456f;

        w.resetLevel();

        assertEquals(1, w.cycle);
        assertFalse(w.shadowPresent);
        assertNull(w.shadow());
        assertFalse(w.door.open);
        assertEquals(0f, w.mechanism.charge, 1e-4f);
        assertEquals(GameConfig.START_X, w.player.x, 1e-3f);
        assertEquals(GameConfig.START_Y, w.player.y, 1e-3f);
        assertEquals(0, w.recordedSamples());
    }

    @Test
    public void endingCycleRecordsShadowSnapshot() {
        GameWorld w = new GameWorld();
        w.resetLevel();
        InputState in = new InputState();
        for (int i = 0; i < 30; i++) {
            w.player.y -= 5f;
            w.update(GameConfig.FIXED_DT, in);
        }
        assertTrue(w.recordedSamples() > 0);
        w.endCycle();
        assertEquals(GameState.CYCLE_COMPLETE, w.state);
        assertTrue(w.shadowPresent);
        assertTrue(w.shadow().active());
    }
}
