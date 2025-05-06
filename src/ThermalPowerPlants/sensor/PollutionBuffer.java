package ThermalPowerPlants.sensor;

import Common.Measurement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PollutionBuffer implements Buffer {
    private final LinkedList<Measurement> buffer = new LinkedList<>();
    private final List<Double> averages = new ArrayList<>();
    private final CustomLock lock = new CustomLock();

    private static final int WINDOW_SIZE = 8;
    private static final int OVERLAP = 4; // 50%

    @Override
    public void add(Measurement m) {
        try {
            lock.lock();
            buffer.add(m);

            if (buffer.size() >= WINDOW_SIZE) {
                computeAverage();

                for (int i = 0; i < OVERLAP; i++) {
                    buffer.removeFirst();
                }
            }

            lock.unlock();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void computeAverage() {
        double sum = 0;
        for (Measurement m : buffer.subList(0, WINDOW_SIZE)) {
            sum += m.getCo2Value();
        }
        averages.add(sum / WINDOW_SIZE);
    }

    @Override
    public List<Measurement> readAllAndClean() {
        List<Measurement> result = new ArrayList<>();
        try {
            lock.lock();
            for (Double avg : averages) {
                // Representing average as Measurement (timestamp = now)
                result.add(new Measurement(System.currentTimeMillis(),avg));
            }
            averages.clear();
            lock.unlock();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return result;
    }
}
