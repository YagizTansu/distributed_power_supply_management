package Mqtt;

public class MqttPublisher extends MqttClient {
    public MqttPublisher(String clientId) {
        super(clientId);
    }

    public void publish(String topic, String message) {
        super.publish(topic, message);
    }

    public void disconnect() {
        super.disconnect();
    }
}
