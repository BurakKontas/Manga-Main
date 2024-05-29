package com.aburakkontas.manga_main.application.handlers;

import com.aburakkontas.manga.common.main.queries.*;
import com.aburakkontas.manga.common.main.queries.results.*;
import com.aburakkontas.manga_main.domain.bodies.GenerateMaskBody;
import com.aburakkontas.manga_main.domain.bodies.MADFBody;
import com.aburakkontas.manga_main.domain.repositories.ModelRepository;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class ModelQueryHandlers {

    private final ModelRepository modelRepository;

    public ModelQueryHandlers(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    @QueryHandler
    public GetModelPricesResult getModelPricing(GetModelPricesQuery query) {
        return new GetModelPricesResult(modelRepository.getModelPrices());
    }

    @QueryHandler
    public CleanQueryResult cleanImage(CleanQuery query) {
        var detectronResult = modelRepository.detectron(query.getImage());

        var generateMaskBody = new GenerateMaskBody();
        generateMaskBody.setImage(detectronResult.getOriginalImage());
        generateMaskBody.setBoxes(detectronResult.getInstances().getPredBoxes());

        var mask = modelRepository.generateMask(generateMaskBody);

        var madfBody = new MADFBody();
        madfBody.setMask(mask.getMask());
        madfBody.setOriginalImage(detectronResult.getOriginalImage());

        var madf = modelRepository.madf(madfBody);

        return new CleanQueryResult(
                detectronResult.getInstances().getPredBoxes(),
                base64ToByteArray(madf.getGt()),
                base64ToByteArray(madf.getMask()),
                base64ToByteArray(madf.getOutput()),
                base64ToByteArray(madf.getOutputComp()),
                base64ToByteArray(madf.getOutputImage())
        );

    }

    private static byte[] base64ToByteArray(String base64) {
        return Base64.getDecoder().decode(base64);
    }
}
