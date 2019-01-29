package com.example.refresh.support;

import lombok.Data;

@Data
public class RefreshBean {

    private String name;

    public RefreshBean(String name){
        this.name = name;
    }

}
