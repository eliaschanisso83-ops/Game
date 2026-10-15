package com.ashenloop;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.ashenloop.core.Door;
import com.ashenloop.core.GameConfig;
import com.ashenloop.core.GameWorld;
import com.ashenloop.core.Mechanism;
import com.ashenloop.core.Player;
import com.ashenloop.core.Rect;
import com.ashenloop.core.Shadow;
import com.ashenloop.core.SwitchPlate;

public final class Renderer {
    private final Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint stroke = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF tmp = new RectF();

    public float scale;
    public float offsetX;
    public float offsetY;

    public Renderer() {
        stroke.setStyle(Paint.Style.STROKE);
        stroke.setStrokeWidth(6f);
    }

    public void render(Canvas canvas, int viewW, int viewH, GameWorld world) {
        canvas.drawColor(Color.parseColor("#0E0E12"));

        scale = Math.min(viewW / GameConfig.WORLD_WIDTH, viewH / GameConfig.WORLD_HEIGHT);
        offsetX = (viewW - GameConfig.WORLD_WIDTH * scale) * 0.5f;
        offsetY = (viewH - GameConfig.WORLD_HEIGHT * scale) * 0.5f;

        canvas.save();
        canvas.translate(offsetX, offsetY);
        canvas.scale(scale, scale);

        drawFloor(canvas);

        fill.setStyle(Paint.Style.FILL);
        fill.setColor(Color.parseColor("#2A2A33"));
        for (Rect wall : world.arena.walls) {
            rect(canvas, wall);
        }

        drawExit(canvas, world.exit);
        drawPlate(canvas, world.plate);
        drawMechanism(canvas, world.mechanism);
        drawDoor(canvas, world.door);
        drawShadow(canvas, world.shadow());
        drawPlayer(canvas, world.player);

        canvas.restore();
    }

    private void drawFloor(Canvas canvas) {
        fill.setStyle(Paint.Style.FILL);
        fill.setColor(Color.parseColor("#16161C"));
        canvas.drawRect(0f, 0f, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, fill);
    }

    private void drawExit(Canvas canvas, Rect exit) {
        fill.setStyle(Paint.Style.FILL);
        fill.setColor(Color.parseColor("#1E3A2A"));
        rect(canvas, exit);

        stroke.setColor(Color.parseColor("#3EA36A"));
        stroke.setStrokeWidth(8f);
        canvas.drawRect(exit.x, exit.y, exit.right(), exit.bottom(), stroke);
        stroke.setStrokeWidth(6f);
    }

    private void drawPlate(Canvas canvas, SwitchPlate plate) {
        fill.setStyle(Paint.Style.FILL);
        if (plate.litByShadow) {
            fill.setColor(Color.parseColor("#7FD1B9"));
        } else if (plate.litByPlayer) {
            fill.setColor(Color.parseColor("#C9A227"));
        } else {
            fill.setColor(Color.parseColor("#3A3212"));
        }
        canvas.drawCircle(plate.x, plate.y, plate.radius, fill);

        stroke.setColor(Color.parseColor("#C9A227"));
        canvas.drawCircle(plate.x, plate.y, plate.radius, stroke);
    }

    private void drawMechanism(Canvas canvas, Mechanism mech) {
        fill.setStyle(Paint.Style.FILL);
        fill.setColor(Color.parseColor("#2A2A33"));
        canvas.drawCircle(mech.x, mech.y, mech.radius, fill);

        stroke.setColor(Color.parseColor("#6E5A2A"));
        canvas.drawCircle(mech.x, mech.y, mech.interactRadius, stroke);

        stroke.setColor(Color.parseColor("#C9A227"));
        stroke.setStrokeWidth(16f);
        tmp.set(mech.x - mech.radius, mech.y - mech.radius, mech.x + mech.radius, mech.y + mech.radius);
        canvas.drawArc(tmp, -90f, mech.ratio() * 360f, false, stroke);
        stroke.setStrokeWidth(6f);

        if (mech.isCharged()) {
            fill.setColor(Color.parseColor("#C9A227"));
            canvas.drawCircle(mech.x, mech.y, mech.radius * 0.4f, fill);
        }
    }

    private void drawDoor(Canvas canvas, Door door) {
        if (door.open) {
            fill.setStyle(Paint.Style.FILL);
            fill.setColor(Color.parseColor("#1E3A2A"));
            rect(canvas, door.rect);
        } else {
            fill.setStyle(Paint.Style.FILL);
            fill.setColor(Color.parseColor("#6E5A2A"));
            rect(canvas, door.rect);
        }
    }

    private void drawShadow(Canvas canvas, Shadow shadow) {
        if (shadow == null || !shadow.active()) {
            return;
        }
        fill.setStyle(Paint.Style.FILL);
        fill.setColor(Color.argb(120, 127, 209, 185));
        canvas.drawCircle(shadow.x, shadow.y, GameConfig.PLAYER_RADIUS, fill);
    }

    private void drawPlayer(Canvas canvas, Player player) {
        fill.setStyle(Paint.Style.FILL);
        fill.setColor(Color.parseColor("#E8E6DF"));
        canvas.drawCircle(player.x, player.y, player.radius, fill);

        fill.setColor(Color.parseColor("#0E0E12"));
        canvas.drawCircle(player.x, player.y, player.radius * 0.35f, fill);
    }

    private void rect(Canvas canvas, Rect r) {
        canvas.drawRect(r.x, r.y, r.right(), r.bottom(), fill);
    }
}
