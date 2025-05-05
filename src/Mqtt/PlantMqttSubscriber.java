package Mqtt;

import Mqtt.MqttSubscriber;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import Common.EnergyRequest;

public class PlantMqttSubscriber extends MqttSubscriber {

    public PlantMqttSubscriber(String clientId) {
        super(clientId);
        client.setCallback(this);
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection lost!");
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println("Delivery complete!");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message){
        EnergyRequest req = EnergyRequest.fromString(message.toString());
        System.out.println("Received energy request: " + req);
    }
}
