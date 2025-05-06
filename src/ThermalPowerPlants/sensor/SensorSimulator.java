package ThermalPowerPlants.sensor;

import Common.Measurement;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class SensorSimulator extends Thread {
    private final Buffer buffer;
    private final long intervalMillis;

    public SensorSimulator(Buffer buffer, long intervalMillis) {
        this.buffer = buffer;
        this.intervalMillis = intervalMillis;
    }

    @Override
    public void run() {
        while (true) {
            try {
                double randomValue = 300 + Math.random() * 200;
                long timestamp = System.currentTimeMillis();
                buffer.add(new Measurement(timestamp,randomValue));
                Thread.sleep(intervalMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}