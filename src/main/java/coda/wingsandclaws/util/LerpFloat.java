package coda.wingsandclaws.util;

import net.minecraft.util.math.MathHelper;

public class LerpFloat {
    private float min;
    private float max;
    private float current;
    private float previous;
    private boolean clamp = false;

    public LerpFloat() {
        current = previous = 0;
    }

    public LerpFloat(float value) {
        current = previous = value;
    }

    public LerpFloat setLimit(float min, float max) {
        clamp = true;
        this.min = min;
        this.max = max;
        set(current);
        return this;
    }

    public float get(float x) {
        if (x <= 0) return previous;
        if (x >= 1) return current;
        return previous + x * (current - previous);
    }

    public float get() {
        return current;
    }

    public void set(float value) {
        sync();
        current = clamp ? MathHelper.clamp(value, min, max) : value;
    }

    public void add(float value) {
        sync();
        current += value;
        if (clamp) current = MathHelper.clamp(current, min, max);
    }

    public void sync() {
        previous = current;
    }

    public float getPrevious() {
        return previous;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }
}
