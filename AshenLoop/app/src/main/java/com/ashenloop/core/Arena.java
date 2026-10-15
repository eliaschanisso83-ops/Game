package com.ashenloop.core;

public final class Arena {
    public final float width;
    public final float height;
    public final Rect[] walls;
    public final Door door;

    public Arena(float width, float height, Door door) {
        this.width = width;
        this.height = height;
        this.door = door;
        this.walls = new Rect[] {
            new Rect(120f, 1250f, 220f, 50f),
            new Rect(560f, 1250f, 220f, 50f),
            new Rect(120f, 1330f, 50f, 220f),
            new Rect(730f, 1330f, 50f, 220f),
            new Rect(250f, 1010f, 60f, 140f),
            new Rect(590f, 1010f, 60f, 140f),
            new Rect(60f, 110f, 140f, 50f),
            new Rect(700f, 110f, 140f, 50f),
            new Rect(0f, 470f, 340f, 50f),
            new Rect(560f, 470f, 340f, 50f)
        };
    }

    public void move(Player p, float vx, float vy, float dt) {
        p.x += vx * dt;
        resolveX(p);
        p.y += vy * dt;
        resolveY(p);
        clamp(p);
    }

    private void resolveX(Player p) {
        for (Rect wall : walls) {
            resolveWallX(p, wall);
        }
        if (!door.open) {
            resolveWallX(p, door.rect);
        }
    }

    private void resolveY(Player p) {
        for (Rect wall : walls) {
            resolveWallY(p, wall);
        }
        if (!door.open) {
            resolveWallY(p, door.rect);
        }
    }

    private void resolveWallX(Player p, Rect wall) {
        if (!wall.intersectsBox(p.x, p.y, p.radius)) {
            return;
        }
        if (p.x < wall.centerX()) {
            p.x = wall.x - p.radius;
        } else {
            p.x = wall.right() + p.radius;
        }
    }

    private void resolveWallY(Player p, Rect wall) {
        if (!wall.intersectsBox(p.x, p.y, p.radius)) {
            return;
        }
        if (p.y < wall.centerY()) {
            p.y = wall.y - p.radius;
        } else {
            p.y = wall.bottom() + p.radius;
        }
    }

    private void clamp(Player p) {
        if (p.x < p.radius) {
            p.x = p.radius;
        } else if (p.x > width - p.radius) {
            p.x = width - p.radius;
        }
        if (p.y < p.radius) {
            p.y = p.radius;
        } else if (p.y > height - p.radius) {
            p.y = height - p.radius;
        }
    }
}
