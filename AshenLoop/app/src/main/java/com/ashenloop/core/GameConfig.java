package com.ashenloop.core;

public final class GameConfig {
    private GameConfig() {}

    public static final float WORLD_WIDTH = 900f;
    public static final float WORLD_HEIGHT = 1800f;

    public static final float PLAYER_RADIUS = 26f;
    public static final float PLAYER_SPEED = 320f;

    public static final float START_X = 450f;
    public static final float START_Y = 1650f;

    public static final float SAMPLE_INTERVAL = 0.05f;
    public static final int MAX_SAMPLES = 4096;
    public static final float MAX_CYCLE_SECONDS = 180f;

    public static final float PLATE_X = 700f;
    public static final float PLATE_Y = 620f;
    public static final float PLATE_RADIUS = 110f;

    public static final float MECH_X = 450f;
    public static final float MECH_Y = 900f;
    public static final float MECH_RADIUS = 70f;
    public static final float MECH_INTERACT_RADIUS = 150f;
    public static final float MECH_CHARGE_TIME = 0.6f;

    public static final float DOOR_X = 340f;
    public static final float DOOR_Y = 470f;
    public static final float DOOR_W = 220f;
    public static final float DOOR_H = 50f;

    public static final float EXIT_X = 300f;
    public static final float EXIT_Y = 300f;
    public static final float EXIT_W = 300f;
    public static final float EXIT_H = 160f;

    public static final float CYCLE_COMPLETE_DELAY = 1.6f;
    public static final float FIXED_DT = 1f / 60f;
}
