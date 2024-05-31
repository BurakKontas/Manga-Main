package com.aburakkontas.manga_main.application.aspects;

import com.aburakkontas.manga.common.payment.commands.DeductCreditCommand;
import com.aburakkontas.manga.common.payment.commands.RefundCreditCommand;
import com.aburakkontas.manga_main.domain.enums.ModelPricing;
import com.aburakkontas.manga_main.domain.exceptions.ExceptionWithErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Aspect
@Component
public class CreditAspect {

    private final CommandGateway commandGateway;

    public CreditAspect(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Around("execution(public * com.aburakkontas.manga_main.application.handlers.ModelQueryHandlers.*(..))")
    public Object handleCredits(ProceedingJoinPoint joinPoint) throws Throwable {
        if (joinPoint.getSignature().getName().equals("getModelPricing")) {
            return joinPoint.proceed();
        }
        Object[] args = joinPoint.getArgs();
        UUID userId = getUserIdFromArgs(args);
        ModelPricing price = getModelPricingFromMethod(joinPoint.getSignature().getName());

        if (userId != null && price != null) {
            try {
                deductCredit(userId, price);
                return joinPoint.proceed();
            } catch (Exception e) {
                refundCredit(userId, price);
                throw e;
            }
        } else {
            throw new ExceptionWithErrorCode("UserId or price not found", 400);
        }
    }

    private UUID getUserIdFromArgs(Object[] args) {
        var arg = args[0];
        var mapper = new ObjectMapper();
        var map = mapper.convertValue(arg, Map.class);
        if (map.containsKey("userId")) {
            return UUID.fromString((String) map.get("userId"));
        }
        return null;
    }

    private ModelPricing getModelPricingFromMethod(String methodName) {
        return switch (methodName) {
            case "cleanImage" -> ModelPricing.CLEAN;
            case "ocr" -> ModelPricing.OCR;
            case "translate" -> ModelPricing.TRANSLATE;
            case "write" -> ModelPricing.WRITE;
            default -> throw new IllegalArgumentException("Method not found");
        };
    }

    private void deductCredit(UUID userId, ModelPricing price) {
        var command = new DeductCreditCommand(userId, price.getPrice());
        commandGateway.sendAndWait(command);
    }

    private void refundCredit(UUID userId, ModelPricing price) {
        var command = new RefundCreditCommand(userId, price.getPrice());
        commandGateway.sendAndWait(command);
    }
}
