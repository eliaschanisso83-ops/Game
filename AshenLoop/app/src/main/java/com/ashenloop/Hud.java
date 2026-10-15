package com.ashenloop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.ashenloop.core.GameState;
import com.ashenloop.core.GameWorld;

public final class Hud {
    public static final int NONE = 0;
    public static final int START = 1;
    public static final int END_CYCLE = 2;
    public static final int INTERACT = 3;
    public static final int RESTART = 4;
    public static final int PAUSE = 5;

    private final Context context;
    private final Paint text = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint box = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint border = new Paint(Paint.ANTI_ALIAS_FLAG);

    public final RectF btnStart = new RectF();
    public final RectF btnEndCycle = new RectF();
    public final RectF btnInteract = new RectF();
    public final RectF btnRestart = new RectF();
    public final RectF btnPause = new RectF();

    private float density = 1f;
    private int width;
    private int height;

    public Hud(Context context) {
        this.context = context;
        text.setColor(Color.parseColor("#E8E6DF"));
        box.setColor(Color.parseColor("#20202A"));
        border.setStyle(Paint.Style.STROKE);
        border.setColor(Color.parseColor("#C9A227"));
    }

    public void layout(int w, int h, float d) {
        this.width = w;
        this.height = h;
        this.density = d;
        float margin = 16f * d;
        float bh = 52f * d;

        btnPause.set(w - margin - bh, margin, w - margin, margin + bh);
        btnRestart.set(margin, margin, margin + bh, margin + bh);

        float ew = 230f * d;
        btnEndCycle.set((w - ew) * 0.5f, margin, (w + ew) * 0.5f, margin + bh);

        float iw = 160f * d;
        float ih = 160f * d;
        btnInteract.set(w - margin - iw, h - margin - ih, w - margin, h - margin);

        float sw = 240f * d;
        float sh = 68f * d;
        btnStart.set((w - sw) * 0.5f, h * 0.62f, (w + sw) * 0.5f, h * 0.62f + sh);

        text.setTextSize(16f * d);
    }

    public int hitTest(float x, float y, GameState state) {
        switch (state) {
            case MENU:
                if (btnStart.contains(x, y)) {
                    return START;
                }
                break;
            case PLAYING:
                if (btnEndCycle.contains(x, y)) {
                    return END_CYCLE;
                }
                if (btnInteract.contains(x, y)) {
                    return INTERACT;
                }
                if (btnRestart.contains(x, y)) {
                    return RESTART;
                }
                if (btnPause.contains(x, y)) {
                    return PAUSE;
                }
                break;
            case PAUSED:
                if (btnRestart.contains(x, y)) {
                    return RESTART;
                }
                if (btnPause.contains(x, y)) {
                    return PAUSE;
                }
                break;
            case LEVEL_COMPLETE:
                if (btnRestart.contains(x, y)) {
                    return RESTART;
                }
                break;
            default:
                break;
        }
        return NONE;
    }

    public void draw(Canvas canvas, GameWorld world) {
        drawStatus(canvas, world);
        switch (world.state) {
            case MENU:
                drawTitle(canvas);
                button(canvas, btnStart, context.getString(R.string.btn_start));
                break;
            case PLAYING:
                button(canvas, btnEndCycle, context.getString(R.string.btn_end_cycle));
                button(canvas, btnInteract, context.getString(R.string.btn_interact));
                button(canvas, btnRestart, "R");
                button(canvas, btnPause, "II");
                break;
            case PAUSED:
                centered(canvas, context.getString(R.string.msg_paused), height * 0.45f, 28f * density);
                button(canvas, btnRestart, "R");
                button(canvas, btnPause, ">");
                break;
            case CYCLE_COMPLETE:
                centered(canvas, context.getString(R.string.msg_cycle_complete), height * 0.45f, 26f * density);
                break;
            case LEVEL_COMPLETE:
                centered(canvas, context.getString(R.string.msg_level_complete), height * 0.45f, 30f * density);
                button(canvas, btnRestart, "R");
                break;
            default:
                break;
        }
    }

    private void drawStatus(Canvas canvas, GameWorld world) {
        if (world.state == GameState.MENU) {
            return;
        }
        text.setTextSize(15f * density);
        float x = 20f * density;
        float y = 96f * density;
        float gap = 24f * density;
        canvas.drawText("CICLO " + world.cycle, x, y, text);

        String shadow = world.shadowPresent
                ? context.getString(R.string.hud_shadow_ready, String.format("%.1f", world.recordedDuration()))
                : context.getString(R.string.hud_shadow_none);
        canvas.drawText(shadow, x, y + gap, text);
        canvas.drawText(context.getString(R.string.hud_charge, Math.round(world.mechanism.ratio() * 100f)), x, y + gap * 2f, text);
        canvas.drawText(world.door.open ? context.getString(R.string.hud_door_open) : context.getString(R.string.hud_door_closed), x, y + gap * 3f, text);

        text.setTextSize(14f * density);
        text.setColor(Color.parseColor("#8FD8C4"));
        if (world.shadowOnPlate) {
            canvas.drawText(context.getString(R.string.msg_shadow_charging), x, y + gap * 4f, text);
        } else if (world.playerOnPlate) {
            canvas.drawText(context.getString(R.string.msg_stand_plate), x, y + gap * 4f, text);
        } else if (world.shadowPresent && world.mechanism.isCharged() && !world.door.open) {
            canvas.drawText(context.getString(R.string.msg_interact_hint), x, y + gap * 4f, text);
        }
        text.setColor(Color.parseColor("#E8E6DF"));
    }

    private void drawTitle(Canvas canvas) {
        centered(canvas, context.getString(R.string.title), height * 0.3f, 40f * density);
        text.setTextSize(16f * density);
        text.setColor(Color.parseColor("#B8B4A6"));
        centered(canvas, context.getString(R.string.subtitle), height * 0.3f + 44f * density, 16f * density);
        text.setColor(Color.parseColor("#E8E6DF"));
    }

    private void button(Canvas canvas, RectF r, String label) {
        float radius = 10f * density;
        canvas.drawRoundRect(r, radius, radius, box);
        border.setStrokeWidth(3f * density);
        canvas.drawRoundRect(r, radius, radius, border);
        text.setTextSize(15f * density);
        float cx = r.centerX();
        float cy = r.centerY() - (text.descent() + text.ascent()) * 0.5f;
        float tw = text.measureText(label);
        canvas.drawText(label, cx - tw * 0.5f, cy, text);
    }

    private void centered(Canvas canvas, String s, float y, float size) {
        text.setTextSize(size);
        float tw = text.measureText(s);
        canvas.drawText(s, (width - tw) * 0.5f, y, text);
    }
}
