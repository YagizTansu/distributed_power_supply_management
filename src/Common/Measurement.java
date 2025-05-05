package Common;

public class Measurement {
    public long timestamp;
    public double co2Value;

    public Measurement(long timestamp, int co2Value) {
        this.timestamp = timestamp;
        this.co2Value = co2Value;
    }

    public double getCo2Value() {
        return co2Value;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
