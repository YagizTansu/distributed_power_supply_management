package ThermalPowerPlants.sensor;

import Common.Measurement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PollutionBuffer implements Buffer {

    private final Queue<Measurement> buffer;
    int MAX_SIZE = 10;
    int WINDOW_SIZE = 5;

    public PollutionBuffer() {
        buffer = new LinkedList<>();
    }


    @Override
    public void addMeasurement(Measurement measurement) {
        if (buffer.size() >= MAX_SIZE) {
            buffer.poll(); // Remove oldest
        }
        buffer.offer(measurement);
    }

    @Override
    public List<Double> readAndClearBuffer() {
        List<Double> values = new ArrayList<>();

        while (!buffer.isEmpty()) {
            values.add(buffer.poll().getCo2Value());
        }

        return values;
    }

    public List<Double> computingSlidedWindowAverage() {

        List<Double> averages = new ArrayList<>();
        List<Measurement> list = new ArrayList<>(buffer);

        for (int i = 0; i <= list.size() - WINDOW_SIZE; i++) {
            double sum = 0.0;
            for (int j = i; j < i + WINDOW_SIZE; j++) {
                sum += list.get(j).getCo2Value();
            }
            averages.add(sum / WINDOW_SIZE);
        }

        return averages;
    }
}
