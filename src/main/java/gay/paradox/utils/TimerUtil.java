package gay.paradox.utils;

public class TimerUtil {
    private long lastMs = System.currentTimeMillis();

    public boolean hasReached(float f) {
        return (float)(System.currentTimeMillis() - lastMs) > f;
    }

    public float getElapsedTime() {
        return System.currentTimeMillis() - this.lastMs;
    }

    public void reset() {
        this.lastMs = System.currentTimeMillis();
    }
}