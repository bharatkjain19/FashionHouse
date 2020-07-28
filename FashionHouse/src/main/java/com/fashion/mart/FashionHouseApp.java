package com.fashion.mart;
 
import java.sql.SQLException;

import org.h2.tools.Server;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
 
@SpringBootApplication
@EnableScheduling
@Component
public class FashionHouseApp
{
    @Autowired
    JobLauncher jobLauncher;
      
    @Autowired
    Job job;
      
    public static void main(String[] args) {

        SpringApplication.run(FashionHouseApp.class, args);
    }

    /**
     * 
     * This will enable other apps to interact with h2 database with tcp port 8090
     * 
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "8090");
    }
}