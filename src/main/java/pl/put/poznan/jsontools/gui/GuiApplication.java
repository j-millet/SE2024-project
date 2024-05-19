package pl.put.poznan.jsontools.gui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "pl.put.poznan.jsontools.rest")
public class GuiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuiApplication.class, args);
    }
}