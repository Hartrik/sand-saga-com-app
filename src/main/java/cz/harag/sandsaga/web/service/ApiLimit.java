package cz.harag.sandsaga.web.service;

/**
 * @author Patrik Harag
 * @version 2024-02-10
 */
public class ApiLimit {

    public static long EPOCH_DURATION_DAY = 86_400_000;  // day

    private final long duration;
    private final long maxActions;
    private long epochStart = System.currentTimeMillis();
    private long epochActions = 0;

    public ApiLimit(long epochDuration, long maxActions) {
        this.duration = epochDuration;
        this.maxActions = maxActions;
    }

    private synchronized void cleanIfNeeded() {
        long now = System.currentTimeMillis();
        long duration = now - epochStart;
        if (duration > this.duration) {
            // start new epoch now
            epochStart = now;
            epochActions = 0;
        }
    }

    public synchronized boolean next() {
        cleanIfNeeded();
        if (epochActions < maxActions) {
            epochActions++;
            return true;
        } else {
            return false;
        }
    }

    public synchronized double countUsedRatio() {
        cleanIfNeeded();
        return 1.0 * epochActions / maxActions;
    }
}
