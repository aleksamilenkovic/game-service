package com.gameservice.service.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gameservice.annotation.Loggable;
import com.google.common.base.Joiner;

@Component
@Aspect
public class LoggableAspect {

	@Pointcut("execution(public * com.mozzartbet.*.service..*Service+.*(..))")
	public void publicServiceMethod() {

	}

	@Pointcut("bean(*Service*)")
	public void publicServiceClass() {

	}

	@Pointcut("publicServiceMethod() && publicServiceClass()")
	public void publicService() {

	}

	@Around("publicService() && @annotation(loggable)")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint, Loggable loggable) throws Throwable {
		long t1 = System.currentTimeMillis();

		Logger logger = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
		StringBuilder prefix = new StringBuilder(joinPoint.getSignature().getName()).append("()");

		Object result = null;

		try {
			if (loggable.detail()) {
				prefix.append(": ").append(Joiner.on(",").join(joinPoint.getArgs()));
			}
			result = joinPoint.proceed();
			return result;
		} finally {
			long t2 = System.currentTimeMillis();
			if (loggable.detail()) {
				prefix.append(" -> ").append(result);
			}
			logger.info("{} took {} ms", prefix, t2 - t1);
		}
	}

}
