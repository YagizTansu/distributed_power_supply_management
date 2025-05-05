package Common;

public class EnergyRequest {
    public int amount;
    public long timestamp;

    public EnergyRequest(int amount, long timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    //serialization function
    public String toString() {
        return amount + " " + timestamp;
    }

    //deserialization
    public static EnergyRequest fromString(String s) {
        String[] parts = s.split(" ");
        return new EnergyRequest(Integer.parseInt(parts[0]), Long.parseLong(parts[1]));
    }
}
