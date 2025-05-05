package Common;

public class BidMessage {
    public String plant_id;
    public double amount;
    public long timestamp;

    public BidMessage(String plant_id,double amount, long timestamp) {
        this.plant_id = plant_id;
        this.amount = amount;
        this.timestamp = timestamp;
    }
}
