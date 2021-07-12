package rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainRestServices {
    public static void main(String[] args) {

        SpringApplication.run(MainRestServices.class, args);
        System.out.println("Start rest services");
    }
}
