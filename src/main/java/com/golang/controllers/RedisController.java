package com.golang.controllers;

import com.golang.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache")
public class RedisController {
    @Autowired
    RedisService redisService;

    @GetMapping("/get")
    public String getValue(@RequestParam String key){
        return String.valueOf(redisService.get(key));
    }

    @GetMapping("/set")
    public String setValue(@RequestParam String key, @RequestParam String value){
        redisService.save(key,value);
        return "Data Mapped";
    }
}
