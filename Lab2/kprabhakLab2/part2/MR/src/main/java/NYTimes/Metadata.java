package NYTimes;

public class Metadata {
    private int hits;
    private float offset;
    private float time;
    public int getHits() {
        return hits;
    }

    public float getOffset() {
        return offset;
    }

    public float getTime() {
        return time;
    }

    // Setter Methods

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }

    public void setTime(float time) {
        this.time = time;
    }
}