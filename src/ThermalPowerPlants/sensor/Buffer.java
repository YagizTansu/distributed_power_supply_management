package ThermalPowerPlants.sensor;

import Common.Measurement;
import java.util.List;

public interface Buffer {
    void addMeasurement(Measurement measurement);
    List<Double> readAndClearBuffer();
}
