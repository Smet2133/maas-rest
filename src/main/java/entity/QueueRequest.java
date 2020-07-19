package entity;


import lombok.Data;
import mapping.Exchange;

import java.util.List;
import java.util.Map;

@Data
public class QueueRequest {
    private String queueName;

    private Exchange exchange;

    //private String exchangeName;
    //private String exchangeType;
    private String routingKey;
    private Map<String, Object> headers;
    private Map<String, Object> arguments;
    private String version;

}
