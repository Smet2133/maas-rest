package entity;

import lombok.Data;

@Data
public class ExchangeRequest {
    private String exchangeName;
    //private String type;
    //private String version;


    public ExchangeRequest(String exchangeName) {
        this.exchangeName = exchangeName;
    }
}
