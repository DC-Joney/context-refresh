package com.example.refresh.service;

import com.example.refresh.support.RefreshBean;
import com.example.refresh.web.RefreshAutowired;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RefreshAutowired
public class DemoService {

    @Autowired(required = false)
    private RefreshBean refreshBean;


    public void demo(){
        log.info(refreshBean);
    }

}
