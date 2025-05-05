package ThermalPowerPlants;

import Common.EnergyRequest;
import Common.BidMessage;

import Mqtt.MqttSubscriber;
import Mqtt.MqttPublisher;
import ThermalPowerPlants.sensor.PollutionSensor;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ThermalPowerPlant implements MqttCallback {
    private final String id;
    private final MqttSubscriber mqttSubscriber;
    private final MqttPublisher mqttPublisher;
    private final PollutionSensor pollutionSensor;
    private boolean isBusy = false;
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    public ThermalPowerPlant(String id) {
        this.id = id;
        this.mqttSubscriber = new MqttSubscriber(id + "_subscriber");
        this.mqttPublisher  = new MqttPublisher(id + "_publisher");
        this.pollutionSensor = new PollutionSensor();
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

    public void PublishCo2Message(){
        mqttPublisher.publish("pollution/" + id, String.valueOf(pollutionSensor.getAverageCO2Value()));
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
        this.pollutionSensor.start();

        scheduler.scheduleAtFixedRate(this::PublishCo2Message, 0, 1, java.util.concurrent.TimeUnit.SECONDS);
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
