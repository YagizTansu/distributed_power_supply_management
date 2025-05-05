package RenewableEnergyProvider;

import Common.EnergyRequest;
import Mqtt.MqttPublisher;

public class RenewableEnergyProvider {
    private final MqttPublisher mqttPublisher;

    public RenewableEnergyProvider(MqttPublisher mqttPublisher) {
        this.mqttPublisher = mqttPublisher;
    }

    private int random(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    public void publishRequest() {
        int kWh = random(5000, 15000);
        EnergyRequest req = new EnergyRequest(kWh, System.currentTimeMillis());

        mqttPublisher.publish("RenewableEnergyProvider/request", req.toString());
    }




}
