package RenewableEnergyProvider;

import Common.EnergyRequest;
import Mqtt.MqttPublisher;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class RenewableEnergyProvider {
    private final MqttPublisher mqttPublisher;
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

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
        System.out.println("Published energy request: " + req.toString());
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::publishRequest, 0, 10, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        MqttPublisher mqttPublisher = new MqttPublisher();
        RenewableEnergyProvider renewableEnergyProvider = new RenewableEnergyProvider(mqttPublisher);
        renewableEnergyProvider.start();
    }
}
