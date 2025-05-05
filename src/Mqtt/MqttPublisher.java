package Mqtt;

public class MqttPublisher extends MqttBroker {
    MqttPublisher() {}

    public void publish(String topic, String message) {
        super.publish(topic, message);
    }

    public void disconnect() {
        super.disconnect();
    }

    public static void main(String[] args) {
        MqttPublisher publisher = new MqttPublisher();
        publisher.publish("test", "Hello World!");
        publisher.disconnect();
    }
}
