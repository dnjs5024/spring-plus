package org.example.expert.aop;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.manager.service.ManagerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ManagerRequestLoggingAspect {

    private final HttpServletRequest request;

    private final ManagerService managerService;

    @Around("execution(* org.example.expert.domain.manager.controller.ManagerController.saveManager(..))")
    public Object logManagerRequest(ProceedingJoinPoint pjp) {

        AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();

        String userId = String.valueOf(authUser.getId());
        LocalDateTime requestTime = LocalDateTime.now();
        String requestUrl = request.getRequestURI();

        log.info("Manager Access Log - User ID: {}, Request Time: {}, Request URL: {}, Method: {}",
            userId, requestTime, requestUrl, pjp.getSignature().getName());

        Object resurt = null;

        try {
            resurt = pjp.proceed();
            managerService.saveLog(requestUrl, authUser.getId(), requestTime,
                pjp.getSignature().getName(),true);
        } catch (Throwable e) {
            managerService.saveLog(requestUrl, authUser.getId(), requestTime,
                pjp.getSignature().getName(),false);
            throw new RuntimeException(e);
        }

        return resurt;

    }
}
