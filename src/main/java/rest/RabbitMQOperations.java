package rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import entity.PromoteRequest;
import entity.ExchangeRequest;
import entity.QueueRequest;
import mapping.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Component
public class RabbitMQOperations {
    private static final String HOST = "localhost";
    private String activeVersion = null;

    @Autowired
    private Channel channel;

    @Autowired
    private RabbitMQHttpAPI rabbitMQHttpAPI;

/*    @Bean
    RabbitMQHttpAPI rabbitMQHttpAPI() {
        return new RabbitMQHttpAPI();
    }*/

    @Bean
    Channel createChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        Connection connection = factory.newConnection();
        return connection.createChannel();
    }
/*
    public RabbitMQOperations() throws IOException, TimeoutException {
        channel = createChannel();
        System.out.println("chan created");
    }
*/




    public void createExchange(ExchangeRequest request){
      if (!exchangeExists(request.getExchangeName(), "headers")) {
          Map<String, Object> args = new HashMap<>();
          String alternateExchangeName = Utils.getAlternateExchangeName(request.getExchangeName());
          args.put("alternate-exchange", alternateExchangeName);
          try {
              channel.exchangeDeclare(alternateExchangeName, "headers");
              channel.exchangeDeclare(request.getExchangeName(), "headers", false, false, args);
          } catch (IOException e) {
              e.printStackTrace();
          }
      }


    }


    private boolean exchangeExists(String exchangeName, String type) {
        //System.out.println(rabbitMQHttpAPI);
        //System.out.println(rabbitMQHttpAPI.getExchanges());
        return rabbitMQHttpAPI.getExchanges().contains(new Exchange(exchangeName, type));
    }



    public void createQueue(QueueRequest request) throws IOException {
        Exchange masterExchange = request.getExchange();
        String versionedQueueName = Utils.getVersionedQueueName(request.getQueueName(), request.getVersion());
        String versionedExchangeName = Utils.getVersionedExchangeName(masterExchange.getName(), request.getVersion());

        if(!exchangeExists(masterExchange.getName(), "headers")) {
            createExchange(new ExchangeRequest(masterExchange.getName()));
        }

        if (!exchangeExists(versionedExchangeName, masterExchange.getType())){
            channel.exchangeDeclare(versionedExchangeName, masterExchange.getType(), false, false, null);
            Map<String, Object> args = new HashMap<>();
            args.put("bg-version", request.getVersion());
            channel.exchangeBind(versionedExchangeName, masterExchange.getName(), "", args);
        }

        if (request.getArguments() != null) {
            //
        }
        System.out.println(channel);
        channel.queueDeclare(versionedQueueName, false, false, false, null);
        channel.queueBind(versionedQueueName, versionedExchangeName, request.getRoutingKey(), request.getHeaders());

    }



    public void promote(PromoteRequest request) throws IOException {
        String alternateExchangeName = Utils.getAlternateExchangeName(request.exchangeName);
        JsonArray jsonArray = new JsonParser().parse(rabbitMQHttpAPI.getBindings(alternateExchangeName)).getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            String destination = jsonElement.getAsJsonObject().get("destination").getAsString();
            channel.exchangeUnbind(destination, alternateExchangeName, "#");
            System.out.println(String.format("Queue '%s' was unbinded to exchange '%s' with routing key '%s'", destination, alternateExchangeName, "#"));
        }
        channel.exchangeBind(Utils.getVersionedExchangeName(request.getExchangeName(), request.getVersion()), alternateExchangeName, "#");
        System.out.println(String.format("Queue '%s' was binded to exchange '%s' with routing key '%s'", request.getExchangeName(), alternateExchangeName, "#"));
    }

    public void deleteQueue(QueueRequest request) throws IOException {
        channel.queueDelete(Utils.getVersionedQueueName(request.getQueueName(), request.getVersion()));
    }

    public void deleteExchange(ExchangeRequest request) throws IOException {
        channel.exchangeDelete(Utils.getAlternateExchangeName(request.getExchangeName()));
        channel.exchangeDelete(request.getExchangeName());
    }



    /*


    private boolean activeVersionIsSet(String exchangeName) throws IOException {
        String alternateExchangeName = Utils.getAlternateExchangeName(exchangeName);
        JsonArray jsonArray = new JsonParser().parse(rabbitMQHttpAPI.getBindings(alternateExchangeName)).getAsJsonArray();
        return jsonArray.size() > 0;
    }

    public void promote(String exchangeName, String version) throws IOException, TimeoutException {
        PromoteRequest changeActiveVersionRequest = new PromoteRequest(exchangeName,  version);
        promote(changeActiveVersionRequest);
    }
*/


    /*
        String versionedExchangeName = Utils.getVersionedExchangeName(request.getExchangeName(), request.getVersion());
        channel.exchangeDeclare(versionedExchangeName, request.getType());
        Map<String, Object> args = new HashMap<>();
        args.put("bg-version", request.getVersion());
        channel.exchangeBind(versionedExchangeName, request.getExchangeName(), "", args);

        //master binded to cpq-quote-modify-v1 with name:	cpq-quote-modify, version: v1

        if (!activeVersionIsSet(request.getExchangeName())) {
            changeActiveVersion(request.getExchangeName(), request.getVersion());
            //bind new exchange with it header with its name to alternate exchange
            //changeActiveVersion for this exchangeName and version
        }*/

}
