package com.aburakkontas.manga_main.application.handlers;

import com.aburakkontas.manga.common.main.queries.*;
import com.aburakkontas.manga.common.main.queries.results.*;
import com.aburakkontas.manga.common.payment.commands.DeductCreditCommand;
import com.aburakkontas.manga.common.payment.commands.RefundCreditCommand;
import com.aburakkontas.manga_main.domain.bodies.*;
import com.aburakkontas.manga_main.domain.enums.ModelPricing;
import com.aburakkontas.manga_main.domain.repositories.ModelRepository;
import com.aburakkontas.manga_main.domain.responses.CropImageResponse;
import com.aburakkontas.manga_main.domain.responses.OCRResponse;
import com.aburakkontas.manga_main.domain.responses.TranslateResponse;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

@Component
public class ModelQueryHandlers {

    private final ModelRepository modelRepository;
    private final CommandGateway commandGateway;

    public ModelQueryHandlers(ModelRepository modelRepository, CommandGateway commandGateway) {
        this.modelRepository = modelRepository;
        this.commandGateway = commandGateway;
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

    @QueryHandler
    public OCRQueryResult ocr(OCRQuery query) {
        var cropImageBody = new CropImageBody();
        cropImageBody.setOriginalImage(byteArrayToBase64(query.getImage()));
        cropImageBody.setPredBoxes(query.getPredBoxes());

        var cropImageResponse = modelRepository.cropImage(cropImageBody);

        var images = new ArrayList<String>();
        for (var cropImage : cropImageResponse) {
            images.add(cropImage.getImage());
        }
        var metaBody = new MetaBody();
        metaBody.setImages(images);

        var metaResponses = modelRepository.metaCraft(metaBody);

        var ocrBody = new ArrayList<OCRBody>();
        for(var metaResponse : metaResponses) {
            var ocr = new OCRBody();
            ocr.setImage(metaResponse.getImage());
            ocr.setIndex(metaResponse.getIndex());
            ocr.setOriginalImage(metaResponse.getOriginalImage());
            ocr.setBoxes(metaResponse.getBoxes());
            ocr.setPolys(metaResponse.getPolys());
            ocr.setPolyTxt(metaResponse.getPolyTxt());
            ocrBody.add(ocr);
        }

        var ocrResults = modelRepository.ocr(ocrBody);

        ArrayList<ArrayList<ArrayList<OCRQueryResult.OCRQueryData>>> queryResults = new ArrayList<>();
        for (var ocrResult : ocrResults) {
            var ocrQueryData = mapToOCRQueryData(ocrResult);
            queryResults.add(ocrQueryData);
        }

        return new OCRQueryResult(queryResults);
    }

    @QueryHandler
    public TranslateQueryResult translate(TranslateQuery query) {
        var translateBody = new TranslateBody();
        translateBody.setSource(query.getSource());
        translateBody.setTarget(query.getTarget());
        translateBody.setText(query.getText());
        var data =  modelRepository.translate(translateBody);

        return new TranslateQueryResult(data.getTarget(), data.getSource(), data.getText(), data.getResult());
    }

    @QueryHandler
    public WriteQueryResult write(WriteQuery query) {

        var writeData = new ArrayList<WriteBody.WriteData>();
        for (var data : query.getData()) {
            var writeDataInner = new WriteBody.WriteData(data.getBbox(), data.getText(), data.getColor());
            writeData.add(writeDataInner);
        }

        var writeBody = new WriteBody();
        writeBody.setDebug(query.getDebug());
        writeBody.setImage(byteArrayToBase64(query.getImage()));
        writeBody.setData(writeData);

        var data = modelRepository.write(writeBody);

        var queryDataResults = new ArrayList<WriteQueryResult.WriteResult>();
        for (var dataInner : data.getResults()) {
            var queryDataResult = new WriteQueryResult.WriteResult(dataInner.getColor(), dataInner.getFontSize(), dataInner.getText());
            queryDataResults.add(queryDataResult);
        }

        return new WriteQueryResult(
                data.getDebug(),
                data.getLastImage(),
                queryDataResults
        );
    }


    private static String byteArrayToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static byte[] base64ToByteArray(String base64) {
        return Base64.getDecoder().decode(base64);
    }

    private static ArrayList<ArrayList<OCRQueryResult.OCRQueryData>> mapToOCRQueryData(OCRResponse response) {
        ArrayList<ArrayList<OCRQueryResult.OCRQueryData>> ocrDataList = new ArrayList<>();

        for (OCRResponse.OCRData responseData : response.getOcrData()) {
            ArrayList<OCRQueryResult.OCRQueryData> ocrQueryDataInnerList = new ArrayList<>();
            OCRQueryResult.OCRQueryData ocrQueryData = new OCRQueryResult.OCRQueryData();
            ocrQueryData.setText(responseData.getText());
            ocrQueryData.setImage(responseData.getImage());
            ocrQueryData.setConfidence(responseData.getConfidence());
            ocrQueryDataInnerList.add(ocrQueryData);

            ocrDataList.add(ocrQueryDataInnerList);
        }

        return ocrDataList;
    }
}
