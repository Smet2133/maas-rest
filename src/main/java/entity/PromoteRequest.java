package entity;


import lombok.Data;

@Data
public class PromoteRequest {
    public String exchangeName;
    public String version;

    public PromoteRequest(String exchangeName, String version) {
        this.exchangeName = exchangeName;
        this.version = version;
    }
}
