package com.ashenloop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ashenloop.core.GameConfig;
import com.ashenloop.core.GameWorld;
import com.ashenloop.core.InputState;

public final class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private final GameWorld world = new GameWorld();
    private final Renderer renderer = new Renderer();
    private final Hud hud;
    private final InputState input = new InputState();
    private final Joystick joystick = new Joystick();
    private final Paint joyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Thread thread;
    private volatile boolean running;
    private float density = 1f;

    public GameView(Context context) {
        super(context);
        hud = new Hud(context);
        density = context.getResources().getDisplayMetrics().density;
        joystick.maxRadius = 120f * density;
        getHolder().addCallback(this);
        setFocusable(true);
        joyPaint.setColor(Color.argb(90, 200, 200, 210));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        running = true;
        thread = new Thread(this, "ashen-loop-render");
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        hud.layout(width, height, density);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        running = false;
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                retry = false;
            }
        }
    }

    @Override
    public void run() {
        long last = System.nanoTime();
        float accumulator = 0f;
        while (running) {
            long now = System.nanoTime();
            float frame = (now - last) / 1_000_000_000f;
            last = now;
            if (frame > 0.25f) {
                frame = 0.25f;
            }
            accumulator += frame;
            input.moveX = joystick.outX;
            input.moveY = joystick.outY;
            int guard = 0;
            while (accumulator >= GameConfig.FIXED_DT && guard < 8) {
                world.update(GameConfig.FIXED_DT, input);
                accumulator -= GameConfig.FIXED_DT;
                guard++;
            }
            input.clearEdges();
            drawFrame();
            try {
                Thread.sleep(2L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void drawFrame() {
        Canvas canvas = null;
        SurfaceHolder holder = getHolder();
        try {
            canvas = holder.lockCanvas();
            if (canvas == null) {
                return;
            }
            renderer.render(canvas, canvas.getWidth(), canvas.getHeight(), world);
            hud.layout(canvas.getWidth(), canvas.getHeight(), density);
            hud.draw(canvas, world);
            drawJoystick(canvas);
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void drawJoystick(Canvas canvas) {
        if (!joystick.active) {
            return;
        }
        canvas.drawCircle(joystick.anchorX, joystick.anchorY, joystick.maxRadius, joyPaint);
        float kx = joystick.anchorX + joystick.outX * joystick.maxRadius;
        float ky = joystick.anchorY + joystick.outY * joystick.maxRadius;
        canvas.drawCircle(kx, ky, 46f * density, joyPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                int hit = hud.hitTest(x, y, world.state);
                if (hit != Hud.NONE) {
                    pressButton(hit);
                    return true;
                }
                joystick.begin(x, y, 120f * density);
                return true;
            }
            case MotionEvent.ACTION_MOVE:
                joystick.move(x, y);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                joystick.end();
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    private void pressButton(int hit) {
        switch (hit) {
            case Hud.START:
                input.startPressed = true;
                break;
            case Hud.END_CYCLE:
                input.endCyclePressed = true;
                break;
            case Hud.INTERACT:
                input.interactPressed = true;
                break;
            case Hud.RESTART:
                input.restartPressed = true;
                break;
            case Hud.PAUSE:
                input.pausePressed = true;
                break;
            default:
                break;
        }
    }
}
