package ThermalPowerPlants;

import Mqtt.PlantMqttSubscriber;
import Mqtt.MqttPublisher;
import ThermalPowerPlants.sensor.PollutionSensor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ThermalPowerPlant {
    private final String id;
    private final PlantMqttSubscriber mqttSubscriber;
    private MqttPublisher mqttPublisher;
    private PollutionSensor pollutionSensor;
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    public ThermalPowerPlant(String id, PlantMqttSubscriber mqttSubscriber,MqttPublisher mqttPublisher) {
        this.id = id;
        this.mqttSubscriber = mqttSubscriber;
        this.mqttPublisher = mqttPublisher;
        this.pollutionSensor = new PollutionSensor();
    }

    public void subscribeRenewableEnergyProviderRequest() {
        mqttSubscriber.subscribe("RenewableEnergyProvider/request");
    }

    public void PublishCo2Message(){
        mqttPublisher.publish("ThermalPowerPlant/" + id, String.valueOf(pollutionSensor.getLatestCO2Value()));
    }

    public void start(){
        System.out.println("Started ThermalPowerPlant with id " + id);
        pollutionSensor.start();
        scheduler.scheduleAtFixedRate(this::PublishCo2Message, 0, 10, java.util.concurrent.TimeUnit.SECONDS);
        subscribeRenewableEnergyProviderRequest();
    }

    public static void main(String[] args) {
        ThermalPowerPlant thermalPowerPlant = new ThermalPowerPlant("ThermalPowerPlant", new PlantMqttSubscriber("ThermalPowerPlant"),new MqttPublisher("ThermalPowerPlant"));
        thermalPowerPlant.start();
    }
}
