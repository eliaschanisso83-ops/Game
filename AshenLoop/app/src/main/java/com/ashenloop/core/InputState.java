package com.ashenloop.core;

public final class InputState {
    public float moveX;
    public float moveY;

    public boolean interactPressed;
    public boolean endCyclePressed;
    public boolean restartPressed;
    public boolean startPressed;
    public boolean pausePressed;

    public void clearEdges() {
        interactPressed = false;
        endCyclePressed = false;
        restartPressed = false;
        startPressed = false;
        pausePressed = false;
    }
}
