package com.example.refresh.resource;

import lombok.Data;

import java.util.List;

@Data
//@ConfigurationProperties(prefix = "demo.server")
public class DemoServer {
    private String name;
    private List<String> favorites;
}
