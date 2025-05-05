package ThermalPowerPlants.sensor;

import Common.Measurement;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class PollutionSensor extends Thread {
    private final PollutionBuffer buffer;
    private final Random random;
    private final AtomicBoolean running;
    private static final int MIN_CO2_VALUE = 200;
    private static final int MAX_CO2_VALUE = 800;
    private static final int MEASUREMENT_INTERVAL_MS = 1000;
    
    public PollutionSensor() {
        this.buffer = new PollutionBuffer();
        this.random = new Random();
        this.running = new AtomicBoolean(false);
    }
    
    @Override
    public void run() {
        running.set(true);
        
        try {
            while (running.get()) {
                // Generate a random CO2 value between MIN and MAX
                int co2Value = MIN_CO2_VALUE + random.nextInt(MAX_CO2_VALUE - MIN_CO2_VALUE + 1);
                Measurement measurement = new Measurement(System.currentTimeMillis(), co2Value);
                
                // Add to buffer
                buffer.addMeasurement(measurement);
                
                // Sleep for the measurement interval
                Thread.sleep(MEASUREMENT_INTERVAL_MS);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Pollution sensor thread interrupted: " + e.getMessage());
        }
    }
    
    @Override
    public void start() {
        if (!isAlive()) {
            running.set(true);
            super.start();
        }
    }
    
    public void stopSensor() {
        running.set(false);
        this.interrupt();
    }
    
    public List<Double> getRecentMeasurements() {
        return buffer.readAndClearBuffer();
    }
    
    public List<Double> getAverageMeasurements() {
        return buffer.computingSlidedWindowAverage();
    }
    
    public double getLatestCO2Value() {
        List<Double> values = buffer.computingSlidedWindowAverage();
        if (values.isEmpty()) {
            return 0.0;
        }
        return values.get(values.size() - 1);
    }
}
