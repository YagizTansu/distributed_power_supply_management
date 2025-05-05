package Mqtt;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttClient {
    protected org.eclipse.paho.client.mqttv3.MqttClient client;
    private final String broker = "tcp://localhost:1883";

    public MqttClient(String clientId) {
        try {
            client = new org.eclipse.paho.client.mqttv3.MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            client.connect(options);
        } catch (MqttException e) {
            System.err.println("Error initializing MQTT client: " + e.getMessage());
        }
    }

    public void publish(String topic, String message) {
        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(0);
            client.publish(topic, mqttMessage);
        } catch (MqttException e) {
            System.err.println("Error publishing message: " + e.getMessage());
        }
    }

    public String subscribe(String topic) {
        try {
            client.subscribe(topic);
            return "Subscribed successfully to " + topic;
        } catch (MqttException e) {
            return "Error subscribing: " + e.getMessage();
        }
    }


    public void disconnect() {
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
                client.close();
            }
        } catch (MqttException e) {
            System.err.println("Error disconnecting: " + e.getMessage());
        }
    }

//    public static void main(String[] args) {
//        MqttPublisher publisher = new MqttPublisher();
//        publisher.publish("test", "Hello World!");
//        publisher.disconnect();
//    }
}