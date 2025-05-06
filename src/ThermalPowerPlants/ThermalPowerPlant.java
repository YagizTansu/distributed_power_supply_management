package ThermalPowerPlants;

import Common.EnergyRequest;
import Common.BidMessage;

import Common.Measurement;
import Mqtt.MqttSubscriber;
import Mqtt.MqttPublisher;
import ThermalPowerPlants.sensor.PollutionBuffer;
import ThermalPowerPlants.sensor.SensorSimulator;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;
public class ThermalPowerPlant implements MqttCallback {
    private final String id;
    private final MqttSubscriber mqttSubscriber;
    private final MqttPublisher mqttPublisher;
    private PollutionBuffer pollutionBuffer ;
    private SensorSimulator sensorSimulator;
    private boolean isBusy = false;


    public ThermalPowerPlant(String id) {
        this.id = id;
        this.mqttSubscriber = new MqttSubscriber(id + "_subscriber");
        this.mqttPublisher  = new MqttPublisher(id + "_publisher");

        this.pollutionBuffer = new PollutionBuffer();
        this.sensorSimulator = new SensorSimulator(pollutionBuffer, 1000);
    }

    public BidMessage calculateBid(EnergyRequest request){
        double randomFactor = 0.1 + Math.random() * 0.8;
        double kWh = request.amount * randomFactor;
        BidMessage electionMsg = new BidMessage(this.id, kWh, System.currentTimeMillis());

        System.out.println("Plant id " + id + " bid Price: " + kWh );
        return electionMsg;
    }

    public void subscribeRenewableEnergyProviderRequest() {
        mqttSubscriber.subscribe("RenewableEnergyProvider/request");
    }

    public void  startsPublishPollution(){
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    List<Measurement> co2Averages = pollutionBuffer.readAllAndClean();
                    for (Measurement m : co2Averages) {
                        String payload = String.format("{\"average\": %.2f, \"timestamp\": %d}",
                                m.getCo2Value(), m.getTimestamp());
                        mqttPublisher.publish("pollution/" + id,payload);
                    }
                } catch (Exception e) {
                    System.err.println("Error publishing pollution: " + e.getMessage());
                    break;
                }
            }
        }).start();
    }

    private void sendElectionMessageToNextPlant(BidMessage electionMsg) {
    }

    public void startElection(BidMessage bidMsg) {
        if (isBusy) return;
        sendElectionMessageToNextPlant(bidMsg);
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        EnergyRequest req = EnergyRequest.fromString(mqttMessage.toString());
        BidMessage bidMessage = calculateBid(req);
        startElection(bidMessage);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println("Delivery complete!");
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection lost!");
    }

    public void start(){
        System.out.println("Started ThermalPowerPlant with id " + id);
        subscribeRenewableEnergyProviderRequest();

        this.mqttSubscriber.setCallback(this);
        this.sensorSimulator.start();
        startsPublishPollution();
    }

    public static void main(String[] args) {
        ThermalPowerPlant thermalPowerPlant1 = new ThermalPowerPlant("plant_1");
        ThermalPowerPlant thermalPowerPlant2 = new ThermalPowerPlant("plant_2");
        ThermalPowerPlant thermalPowerPlant3 = new ThermalPowerPlant("plant_3");
        ThermalPowerPlant thermalPowerPlant4 = new ThermalPowerPlant("plant_4");

        thermalPowerPlant1.start();
        thermalPowerPlant2.start();
        thermalPowerPlant3.start();
        thermalPowerPlant4.start();

    }
}
