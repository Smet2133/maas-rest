package rest;

import entity.PromoteRequest;
import entity.ExchangeRequest;
import entity.QueueRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping(path = "/api/maas")
public class MaaSController {

	//RabbitMQHttpAPI rabbitMQHttpAPI = new RabbitMQHttpAPI();
	//RabbitMQOperations rabbitMQOperations = new RabbitMQOperations();

	@Autowired
	RabbitMQHttpAPI rabbitMQHttpAPI;

	@Autowired
	RabbitMQOperations rabbitMQOperations;

    public MaaSController() throws IOException, TimeoutException {
    }

    @GetMapping(path = "/exchanges")
	public ResponseEntity getExchanges() throws IOException {
    	return ResponseEntity.ok(rabbitMQHttpAPI.getExchanges());
	}


	@GetMapping(path = "/queues")
	public ResponseEntity getQueues() throws IOException {
		return ResponseEntity.ok(rabbitMQHttpAPI.getQueues());
	}

	@PostMapping(path = "/exchange")
	public Object createExchange( @RequestBody ExchangeRequest request) throws IOException, TimeoutException {
		rabbitMQOperations.createExchange(request);
		return "OK";
	}

    @DeleteMapping(path = "/exchange")
    public Object deleteExchange( @RequestBody ExchangeRequest request) throws IOException, TimeoutException {
        rabbitMQOperations.deleteExchange(request);
        return "OK";
    }

/*	@PostMapping(path = "/exchange/master")
	public Object createMasterExchange( @RequestBody MasterExchangeCreationRequest request) throws IOException, TimeoutException {
		rabbitMQOperations.createMasterExchange(request);
		return "OK";
	}*/



	@PostMapping(path = "/queue")
	public Object createQueue( @RequestBody QueueRequest request) throws IOException, TimeoutException {
		rabbitMQOperations.createQueue(request);
		return "OK";
	}

    @DeleteMapping(path = "/queue")
    public Object deleteQueue( @RequestBody QueueRequest request) throws IOException, TimeoutException {
        rabbitMQOperations.deleteQueue(request);
        return "OK";
    }

    @PostMapping(path = "/promote")
    public Object changeActiveVersion( @RequestBody PromoteRequest request) throws IOException, TimeoutException {
        rabbitMQOperations.promote(request);
        return "OK";
    }

}
