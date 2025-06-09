package org.phuongnq.hibernate_envers.config.audit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ModuleAspect {
  private static final Logger logger = LoggerFactory.getLogger(ModuleAspect.class);

  @Before("execution(* org.phuongnq.hibernate_envers.controller.*.*(..))")
  public void logBefore(JoinPoint joinPoint) {
    String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
    String module = className.replace("Controller", "");
    CurrentModuleAudit.INSTANCE.setModule(module);
    System.out.println("Before method execution");
  }

  @After("execution(* org.phuongnq.hibernate_envers.controller.*.*(..))")
  public void logAfter(JoinPoint joinPoint) {
    CurrentModuleAudit.INSTANCE.remove();
    System.out.println("After method execution");
  }
}
