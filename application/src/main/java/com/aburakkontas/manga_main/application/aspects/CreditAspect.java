package com.aburakkontas.manga_main.application.aspects;

import com.aburakkontas.manga.common.payment.commands.DeductCreditCommand;
import com.aburakkontas.manga.common.payment.commands.RefundCreditCommand;
import com.aburakkontas.manga.common.payment.queries.HasCreditQuery;
import com.aburakkontas.manga.common.payment.queries.results.HasCreditQueryResult;
import com.aburakkontas.manga_main.domain.enums.ModelPricing;
import com.aburakkontas.manga_main.domain.exceptions.ExceptionWithErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Aspect
@Component
public class CreditAspect {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CreditAspect(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @Around("execution(public * com.aburakkontas.manga_main.application.handlers.ModelQueryHandlers.*(..))")
    public Object handleCredits(ProceedingJoinPoint joinPoint) throws Throwable {
        if (joinPoint.getSignature().getName().equals("getModelPricing")) {
            return joinPoint.proceed();
        }

        Object[] args = joinPoint.getArgs();
        UUID userId = extractUserId(args);
        ModelPricing price = getModelPricing(joinPoint.getSignature().getName());

        verifyAndProcessCredits(userId, price);
        return joinPoint.proceed();
    }

    private UUID extractUserId(Object[] args) {
        var map = objectMapper.convertValue(args[0], Map.class);
        return map.containsKey("userId") ? UUID.fromString((String) map.get("userId")) : null;
    }

    private ModelPricing getModelPricing(String methodName) {
        Map<String, ModelPricing> pricingMap = Map.of(
                "cleanImage", ModelPricing.CLEAN,
                "ocr", ModelPricing.OCR,
                "translate", ModelPricing.TRANSLATE,
                "write", ModelPricing.WRITE
        );
        return pricingMap.getOrDefault(methodName, null);
    }

    private void verifyAndProcessCredits(UUID userId, ModelPricing price) throws ExceptionWithErrorCode {
        if (userId == null || price == null) {
            throw new ExceptionWithErrorCode("UserId or price not found", HttpServletResponse.SC_BAD_REQUEST);
        }

        checkCredit(userId, price);
        try {
            deductCredit(userId, price);
        } catch (Exception e) {
            refundCredit(userId, price);
            throw e;
        }
    }

    private void checkCredit(UUID userId, ModelPricing price) {
        HasCreditQuery query = new HasCreditQuery(userId, price.getPrice());
        HasCreditQueryResult result = queryGateway.query(query, HasCreditQueryResult.class).join();
        if (!result.isHasCredit()) {
            throw new ExceptionWithErrorCode("Not enough credit", HttpServletResponse.SC_PAYMENT_REQUIRED);
        }
    }

    private void deductCredit(UUID userId, ModelPricing price) {
        DeductCreditCommand command = new DeductCreditCommand(userId, price.getPrice());
        commandGateway.sendAndWait(command);
    }

    private void refundCredit(UUID userId, ModelPricing price) {
        RefundCreditCommand command = new RefundCreditCommand(userId, price.getPrice());
        commandGateway.sendAndWait(command);
    }
}
