

package com.vitcheu.owner.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class EffectAop {
    @Pointcut("execution(public * com.vitcheu.owner.model.effect.PetPropertiesChangeEffect.takesEffect(..))")
    public void usingPropsPointCut() {
    };

    @Before(value = "usingPropsPointCut()")
    public void beforeMethod(JoinPoint joinPoint) {
        String name = joinPoint.getSignature().getName();
        log.info("\n In method:"+name);
        log.info("Prop is taking effect...\n\n");
    }

    @AfterThrowing(value = "usingPropsPointCut()", throwing = "e")
    public void afterThrow(Throwable e) {
        log.info("\n\nUsing props failed, Throwing: " + e);
    }
}