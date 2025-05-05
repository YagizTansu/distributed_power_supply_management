package Mqtt;

import Common.EnergyRequest;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public  class MqttSubscriber extends MqttClient {
    public MqttSubscriber(String clientId) {
        super(clientId);
    }

    public String subscribe(String topic) {
        return super.subscribe(topic);
    }

    public void disconnect() {
        super.disconnect();
    }

    public void setCallback(MqttCallback callback) {
        client.setCallback(callback);
    }
}
