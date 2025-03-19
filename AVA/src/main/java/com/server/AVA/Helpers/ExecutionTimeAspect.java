package com.server.AVA.Helpers;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ExecutionTimeAspect {
    private static final Logger log = LoggerFactory.getLogger(ExecutionTimeAspect.class);
    private static final String POINTCUT_EXPRESSION_IMPLEMENTATIONS =
            "execution(* com.server.AVA.Implementations.*.*(..))";

    @Around(POINTCUT_EXPRESSION_IMPLEMENTATIONS)
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        ExecutionTimeAspect.log.info("Execution time of {} (): {}ms", joinPoint.getSignature().getName(), endTime - startTime);
        return proceed;
    }
}
