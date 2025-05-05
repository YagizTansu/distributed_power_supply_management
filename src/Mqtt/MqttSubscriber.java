package Mqtt;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public abstract class MqttSubscriber extends MqttClient implements MqttCallback {
    MqttSubscriber(String clientId) {
        super(clientId);
    }

    public String subscribe(String topic) {
        return super.subscribe(topic);
    }

    public void disconnect() {
        super.disconnect();
    }

    @Override
    public abstract void messageArrived(String topic, MqttMessage message);
}
