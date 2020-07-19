package mapping;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Exchange {

    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
    @JsonProperty("durable")
    private String durable;
    @JsonProperty("auto-delete")
    private String auto_delete;

    public Exchange(){

    }

    public Exchange(String name, String type) {
        this.name = name;
        this.type = type;
    }
}
