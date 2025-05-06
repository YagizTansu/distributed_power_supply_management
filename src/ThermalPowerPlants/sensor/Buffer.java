package ThermalPowerPlants.sensor;

import Common.Measurement;
import java.util.List;


public interface Buffer {
    void add(Measurement m);
    List<Measurement> readAllAndClean();
}
