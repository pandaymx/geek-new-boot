package com.ppmb.core.log.presentation.aspect;

import com.ppmb.core.log.domain.event.OpLogEvent;
import com.ppmb.core.log.presentation.annotation.OpLog;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tools.jackson.databind.ObjectMapper;

@Aspect
@Component
public class OpLogAspect {

    private static final Logger log = LoggerFactory.getLogger(OpLogAspect.class);

    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    public OpLogAspect(ApplicationEventPublisher eventPublisher, ObjectMapper objectMapper) {
        this.eventPublisher = eventPublisher;
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(opLogAnnotation)")
    public Object aroundOpLog(ProceedingJoinPoint joinPoint, OpLog opLogAnnotation)
            throws Throwable {
        Instant operateTime = Instant.now();
        String traceId = MDC.get("traceId");

        // Obtain request attributes
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        String requestUrl = request != null ? request.getRequestURI() : "";
        String requestMethod = request != null ? request.getMethod() : "";

        // Serialize request parameters (simple approach, you might want to sanitize sensitive data
        // here)
        String requestParam = "";
        try {
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                requestParam = objectMapper.writeValueAsString(args);
            }
        } catch (Exception e) {
            log.warn("Failed to serialize request params for OpLog", e);
        }

        // Obtain user info from Spring Security Context
        String operatorId = "";
        String operatorName = "";
        String tenantId = ""; // No default tenant ID extractor in this context yet
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            operatorName = authentication.getName();
            // Assuming UserDetails or similar is stored, for now we just use name
            operatorId = operatorName;
        }

        Integer status = 200;
        String errorMsg = "";

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable t) {
            status = 500;
            errorMsg = t.getMessage();
            throw t;
        } finally {
            // Build the Domain Event
            OpLogEvent event =
                    new OpLogEvent(
                            traceId,
                            tenantId,
                            operatorId,
                            operatorName,
                            opLogAnnotation.title(),
                            opLogAnnotation.action(),
                            requestUrl,
                            requestMethod,
                            requestParam,
                            status,
                            errorMsg,
                            operateTime);

            // Publish the event to be asynchronously handled
            eventPublisher.publishEvent(event);
        }

        return result;
    }
}
