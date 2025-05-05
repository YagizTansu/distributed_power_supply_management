package ThermalPowerPlants;

import Mqtt.MqttSubscriber;

public class ThermalPowerPlant {
    private String name;
    private String id;
    private final MqttSubscriber mqttSubscriber;

    public ThermalPowerPlant(String name, String id, MqttSubscriber mqttSubscriber) {
        this.name = name;
        this.id = id;
        this.mqttSubscriber = mqttSubscriber;
    }

    public void subscribeRenewableEnergyProviderRequest() {
        mqttSubscriber.subscribe("renewableEnergyProvider/request");
    }

    // callback
    public void onRenewableEnergyProviderRequest(String message) {
        System.out.println("Received renewable energy provider request: " + message);
    }


    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setId(String id) {
        this.id = id;
    }

}
