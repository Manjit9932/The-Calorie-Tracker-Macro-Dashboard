package com.calorietracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.calorietracker"})
public class CalorieTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalorieTrackerApplication.class, args);
    }

}
