package org.giraffeb.facade;


import org.giraffeb.utils.FaceEmotionDataImageDraw;
import org.giraffeb.utils.ImageByteConvertor;
import org.giraffeb.utils.MsFaceAPI;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

@Component
public class FaceDetectFacade {

    private Logger logger = LoggerFactory.getLogger(FaceDetectFacade.class);

    private MsFaceAPI msFaceAPI;
    private ImageByteConvertor imageByteConvertor;
    private FaceEmotionDataImageDraw faceEmotionDataImageDraw;

    public FaceDetectFacade(MsFaceAPI msFaceAPI, ImageByteConvertor imageByteConvertor, FaceEmotionDataImageDraw faceEmotionDataImageDraw) {
        this.msFaceAPI = msFaceAPI;
        this.imageByteConvertor = imageByteConvertor;
        this.faceEmotionDataImageDraw = faceEmotionDataImageDraw;
    }


    public String requestFaceAPIFromUriImage(String imageUri){

        //#1 uri -> BufferedImage
        BufferedImage userImage = imageByteConvertor.getUriImageToBufferedImage(imageUri);
        String result  = requestFaceAPIFromBufferedImage(userImage);

        return result;
    }

    public String requestFaceAPIFromMultiPartFile(MultipartFile srcMultiPartFile){

        BufferedImage userImage = imageByteConvertor.convertMultifileToBufferedImage(srcMultiPartFile);
        String result = requestFaceAPIFromBufferedImage(userImage);

        return result;
    }


    public String requestFaceAPIFromBufferedImage(BufferedImage originalImage){

        BufferedImage resizedUserImage = imageByteConvertor.resizeBufferedImage(originalImage);
        //#3 BufferedImage -> byte[]
        byte[] userImageByteArray = imageByteConvertor.bufferedImageToByteArray(resizedUserImage);
        //#4 byte[] -> send to Ms Face API
        JSONArray faceApiResult = msFaceAPI.sendToFaceDetectAPIWithImageByteArray(userImageByteArray);
        //#5 JSONArray -> draw rectacgle from face api result
        BufferedImage rectBufferedImage = faceEmotionDataImageDraw.drawFaceRectangles(faceApiResult, resizedUserImage);
        //#5-1 JSONArray -> draw score from face api result
        BufferedImage faceEmotionImage = faceEmotionDataImageDraw.drawFacesData(faceApiResult, rectBufferedImage);
        //#5-2 merge rectangle image + score Image
        BufferedImage resultImage = faceEmotionDataImageDraw.mergeImages(rectBufferedImage, faceEmotionImage);


        //#6 BufferdImage to ByteArray
        byte[] resultImageByteArray = imageByteConvertor.bufferedImageToByteArray(resultImage);
        //#7 encode byteArray to base64 String
        String base64EncodedImageString = imageByteConvertor.byteArrayToBase64String(resultImageByteArray);

        return base64EncodedImageString;
    }
}
