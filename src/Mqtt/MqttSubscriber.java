package Mqtt;

public class MqttSubscriber extends MqttBroker {
    MqttSubscriber() {}

    public String subscribe(String topic) {
        return super.subscribe(topic);
    }

    public void disconnect() {
        super.disconnect();
    }
}
