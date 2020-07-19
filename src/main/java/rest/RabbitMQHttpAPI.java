package rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import mapping.Exchange;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
//@AllArgsConstructor
public class RabbitMQHttpAPI {

	String hostname = "localhost";
	String rabbitmqURL = "http://" + hostname + ":15672";
	String login = "guest";
	String password = "guest";

	ObjectMapper mapper = new ObjectMapper();

	public RabbitMQHttpAPI() {
	}

	public List<Exchange> getExchanges()  {
		ArrayList<Exchange> exchanges = new ArrayList<>();
		JsonArray jsonArray = new JsonParser().parse(getContent("/api/exchanges")).getAsJsonArray();
		for (JsonElement jsonElement : jsonArray) {
			//System.out.println(jsonElement.toString());
			try {
				exchanges.add(mapper.readValue(jsonElement.toString(), Exchange.class));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		//System.out.println(exchanges);
		return exchanges;
	}


	public String getQueues() throws IOException{
		return getContent( "/api/queues");
	}

    public String getBindings(String alternateExchangeName) throws IOException{
        return getContent("/api/exchanges/%2f/" + alternateExchangeName + "/bindings/source");
        //return getContent("/api/bindings/%2f/e/"+ alternateExchangeName + "/q/queue");
    }

	private String getContent(String api)  {
		URL url = null;
		try {
			url = new URL(rabbitmqURL + api);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			connection.setRequestMethod("GET");
		} catch (ProtocolException e) {
			e.printStackTrace();
		}
		connection.setRequestProperty("Authorization", getAuthHeaderValue());

		StringBuilder content = null;
		try (BufferedReader in = new BufferedReader(
				new InputStreamReader(connection.getInputStream()))) {

			String line;
			content = new StringBuilder();

			while ((line = in.readLine()) != null) {

				content.append(line);
				content.append(System.lineSeparator());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (content != null){
			return content.toString();
		} else {
			return "no content";
		}
	}


	private String getAuthHeaderValue(){
		String auth = login + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
		return "Basic " + new String(encodedAuth);
	}
}
