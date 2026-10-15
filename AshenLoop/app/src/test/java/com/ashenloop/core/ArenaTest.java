package com.ashenloop.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArenaTest {

    private Arena newArena() {
        Door door = new Door(new Rect(
                GameConfig.DOOR_X,
                GameConfig.DOOR_Y,
                GameConfig.DOOR_W,
                GameConfig.DOOR_H));
        return new Arena(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, door);
    }

    @Test
    public void clampsPlayerInsideWorldBounds() {
        Arena arena = newArena();
        Player p = new Player(GameConfig.PLAYER_RADIUS, GameConfig.PLAYER_SPEED);

        p.x = 40f;
        p.y = 40f;
        arena.move(p, -5000f, -5000f, 1f);
        assertEquals(p.radius, p.x, 1e-3f);
        assertEquals(p.radius, p.y, 1e-3f);

        arena.move(p, 5000f, 5000f, 1f);
        assertEquals(arena.width - p.radius, p.x, 1e-3f);
        assertEquals(arena.height - p.radius, p.y, 1e-3f);
    }

    @Test
    public void closedDoorBlocksPlayer() {
        Arena arena = newArena();
        Player p = new Player(GameConfig.PLAYER_RADIUS, GameConfig.PLAYER_SPEED);

        p.x = 450f;
        p.y = 560f;
        arena.move(p, 0f, -60f, 1f);

        float expected = GameConfig.DOOR_Y + GameConfig.DOOR_H + p.radius;
        assertEquals(expected, p.y, 1e-3f);
    }

    @Test
    public void openDoorLetsPlayerThrough() {
        Arena arena = newArena();
        arena.door.open();
        Player p = new Player(GameConfig.PLAYER_RADIUS, GameConfig.PLAYER_SPEED);

        p.x = 450f;
        p.y = 560f;
        arena.move(p, 0f, -120f, 1f);

        assertTrue(p.y < GameConfig.DOOR_Y);
    }
}
