package com.ashenloop.core;

public final class Trajectory {
    private float[] ts;
    private float[] xs;
    private float[] ys;
    private int count;
    private final int maxSamples;

    public Trajectory() {
        this(GameConfig.MAX_SAMPLES);
    }

    public Trajectory(int maxSamples) {
        this.maxSamples = maxSamples > 0 ? maxSamples : 1;
        this.ts = new float[Math.min(16, this.maxSamples)];
        this.xs = new float[this.ts.length];
        this.ys = new float[this.ts.length];
    }

    public void clear() {
        count = 0;
    }

    public boolean add(float t, float x, float y) {
        if (count >= maxSamples) {
            return false;
        }
        if (count > 0 && t < ts[count - 1]) {
            return false;
        }
        if (count == ts.length) {
            grow();
        }
        ts[count] = t;
        xs[count] = x;
        ys[count] = y;
        count++;
        return true;
    }

    private void grow() {
        int cap = Math.min(maxSamples, ts.length * 2);
        if (cap <= ts.length) {
            cap = Math.min(maxSamples, ts.length + 1);
        }
        float[] nt = new float[cap];
        float[] nx = new float[cap];
        float[] ny = new float[cap];
        System.arraycopy(ts, 0, nt, 0, count);
        System.arraycopy(xs, 0, nx, 0, count);
        System.arraycopy(ys, 0, ny, 0, count);
        ts = nt;
        xs = nx;
        ys = ny;
    }

    public int size() {
        return count;
    }

    public int maxSize() {
        return maxSamples;
    }

    public float timeAt(int i) {
        return ts[i];
    }

    public float xAt(int i) {
        return xs[i];
    }

    public float yAt(int i) {
        return ys[i];
    }

    public boolean isTemporallyOrdered() {
        for (int i = 1; i < count; i++) {
            if (ts[i] < ts[i - 1]) {
                return false;
            }
        }
        return true;
    }

    public float duration() {
        return count == 0 ? 0f : ts[count - 1];
    }

    public void sampleAt(float t, float[] out) {
        if (count == 0) {
            out[0] = 0f;
            out[1] = 0f;
            return;
        }
        if (count == 1) {
            out[0] = xs[0];
            out[1] = ys[0];
            return;
        }
        float dur = ts[count - 1];
        float time = t;
        if (dur > 0f) {
            time = t % dur;
            if (time < 0f) {
                time += dur;
            }
        } else {
            time = 0f;
        }
        int i = segment(time);
        float t0 = ts[i];
        float t1 = ts[i + 1];
        float f = t1 > t0 ? (time - t0) / (t1 - t0) : 0f;
        if (f < 0f) {
            f = 0f;
        } else if (f > 1f) {
            f = 1f;
        }
        out[0] = xs[i] + (xs[i + 1] - xs[i]) * f;
        out[1] = ys[i] + (ys[i + 1] - ys[i]) * f;
    }

    private int segment(float time) {
        int lo = 0;
        int hi = count - 1;
        while (lo < hi) {
            int mid = (lo + hi + 1) >>> 1;
            if (ts[mid] <= time) {
                lo = mid;
            } else {
                hi = mid - 1;
            }
        }
        if (lo >= count - 1) {
            return count - 2;
        }
        return lo;
    }
}
