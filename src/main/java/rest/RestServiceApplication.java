package rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestServiceApplication {


    @Autowired
    static RabbitMQHttpAPI rabbitMQHttpAPI;

/*    @Bean
    RabbitMQHttpAPI bookingService() {
        return new RabbitMQHttpAPI();
    }*/

    public static void main(String[] args) {

       // System.out.println(rabbitMQHttpAPI);
        SpringApplication.run(RestServiceApplication.class, args);
    }

}
