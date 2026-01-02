package com.golang.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAspect.class);

    @Before("within(@org.springframework.web.bind.annotation.RestController *)")
    public void logApiCall(JoinPoint joinPoint) {
        LOGGER.info("API call intercepted at controller layer for {}", joinPoint.getSignature());
    }
}
