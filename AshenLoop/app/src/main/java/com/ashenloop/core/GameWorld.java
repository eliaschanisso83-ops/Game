package com.ashenloop.core;

public final class GameWorld {
    public GameState state;
    public final Arena arena;
    public final Player player;
    public final SwitchPlate plate;
    public final Mechanism mechanism;
    public final Door door;
    public final Rect exit;

    private final TrajectoryRecorder recorder;
    private Shadow shadow;

    public int cycle;
    public float cycleTime;
    public float cycleCompleteTimer;
    public boolean playerOnPlate;
    public boolean shadowOnPlate;
    public boolean shadowPresent;

    public GameWorld() {
        player = new Player(GameConfig.PLAYER_RADIUS, GameConfig.PLAYER_SPEED);
        plate = new SwitchPlate(GameConfig.PLATE_X, GameConfig.PLATE_Y, GameConfig.PLATE_RADIUS);
        mechanism = new Mechanism(
                GameConfig.MECH_X,
                GameConfig.MECH_Y,
                GameConfig.MECH_RADIUS,
                GameConfig.MECH_INTERACT_RADIUS,
                GameConfig.MECH_CHARGE_TIME);
        door = new Door(new Rect(
                GameConfig.DOOR_X,
                GameConfig.DOOR_Y,
                GameConfig.DOOR_W,
                GameConfig.DOOR_H));
        exit = new Rect(
                GameConfig.EXIT_X,
                GameConfig.EXIT_Y,
                GameConfig.EXIT_W,
                GameConfig.EXIT_H);
        arena = new Arena(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, door);
        recorder = new TrajectoryRecorder();
        resetLevel();
        state = GameState.MENU;
    }

    public Shadow shadow() {
        return shadow;
    }

    public int recordedSamples() {
        return recorder.size();
    }

    public float recordedDuration() {
        return recorder.trajectory().duration();
    }

    public void resetLevel() {
        cycle = 1;
        cycleTime = 0f;
        cycleCompleteTimer = 0f;
        player.reset();
        recorder.clear();
        shadow = null;
        shadowPresent = false;
        mechanism.reset();
        door.close();
        plate.reset();
        playerOnPlate = false;
        shadowOnPlate = false;
        state = GameState.PLAYING;
    }

    public void update(float dt, InputState input) {
        if (dt < 0f) {
            dt = 0f;
        } else if (dt > 0.1f) {
            dt = 0.1f;
        }

        if (input.pausePressed) {
            input.pausePressed = false;
            if (state == GameState.PLAYING) {
                state = GameState.PAUSED;
            } else if (state == GameState.PAUSED) {
                state = GameState.PLAYING;
            }
        }

        switch (state) {
            case MENU:
                if (input.startPressed) {
                    input.startPressed = false;
                    resetLevel();
                }
                break;
            case PLAYING:
                updatePlaying(dt, input);
                break;
            case CYCLE_COMPLETE:
                cycleCompleteTimer += dt;
                if (cycleCompleteTimer >= GameConfig.CYCLE_COMPLETE_DELAY) {
                    beginNextCycle();
                }
                break;
            case LEVEL_COMPLETE:
                if (input.restartPressed) {
                    input.restartPressed = false;
                    resetLevel();
                }
                break;
            case PAUSED:
            default:
                break;
        }
    }

    private void updatePlaying(float dt, InputState input) {
        if (input.restartPressed) {
            input.restartPressed = false;
            resetLevel();
            return;
        }
        if (input.endCyclePressed) {
            input.endCyclePressed = false;
            endCycle();
            return;
        }

        player.move(dt, input.moveX, input.moveY, arena);
        cycleTime += dt;
        recorder.capture(cycleTime, player.x, player.y);

        boolean shadowOn = false;
        if (shadow != null) {
            shadow.sampleAtTime(cycleTime);
            shadowOn = plate.contains(shadow.x, shadow.y);
        }

        playerOnPlate = plate.contains(player.x, player.y);
        shadowOnPlate = shadowOn;
        plate.litByPlayer = playerOnPlate;
        plate.litByShadow = shadowOn;

        mechanism.update(dt, shadowOn);

        if (input.interactPressed) {
            input.interactPressed = false;
            if (mechanism.inInteractRange(player.x, player.y) && mechanism.isCharged()) {
                door.open();
            }
        }

        if (door.open && exit.contains(player.x, player.y)) {
            state = GameState.LEVEL_COMPLETE;
            return;
        }

        if (cycleTime >= GameConfig.MAX_CYCLE_SECONDS) {
            endCycle();
        }
    }

    public void endCycle() {
        Trajectory snapshot = new Trajectory();
        Trajectory recorded = recorder.trajectory();
        for (int i = 0; i < recorded.size(); i++) {
            snapshot.add(recorded.timeAt(i), recorded.xAt(i), recorded.yAt(i));
        }
        shadow = new Shadow(snapshot);
        shadowPresent = snapshot.size() > 0;
        state = GameState.CYCLE_COMPLETE;
        cycleCompleteTimer = 0f;
    }

    private void beginNextCycle() {
        cycle++;
        cycleTime = 0f;
        cycleCompleteTimer = 0f;
        player.reset();
        recorder.clear();
        mechanism.reset();
        door.close();
        plate.reset();
        playerOnPlate = false;
        shadowOnPlate = false;
        state = GameState.PLAYING;
    }
}
