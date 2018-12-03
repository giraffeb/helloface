package org.giraffeb.implementation;

import org.giraffeb.template.AbstractFaceEmotion;
import org.giraffeb.utils.FaceApiParse;
import org.giraffeb.utils.FaceApiSend;
import org.giraffeb.utils.FaceImageDraw;
import org.giraffeb.utils.ImageConvertor;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

@Qualifier("fati")
@Component
public class FaceEmotionImplFromMultiPartFile extends AbstractFaceEmotion {


    @Autowired
    FaceApiParse fap;

    @Autowired
    FaceApiSend fas;

    @Autowired
    FaceImageDraw fid;

    @Autowired
    ImageConvertor ic;

    protected MultipartFile multiPartFile;

    protected BufferedImage originalBufferedImage;
    protected byte[] originalImageBytes;
    protected BufferedImage emotionPartsImage;
    protected String faceApiResultString;
    protected BufferedImage resultImage;

    //TODO: faceApiInfo를 파싱할때 rectangle과 emotions를 분리해서 만들기
    protected JSONArray faceApiInfo;
    protected String resultImageString;


    public MultipartFile getMultiPartFile() {
        return multiPartFile;
    }

    public BufferedImage getOriginalBufferedImage() {
        return originalBufferedImage;
    }

    public byte[] getOriginalImageBytes() {
        return originalImageBytes;
    }

    public BufferedImage getEmotionPartsImage() {
        return emotionPartsImage;
    }

    public String getFaceApiResultString() {
        return faceApiResultString;
    }

    public BufferedImage getResultImage() {
        return resultImage;
    }

    public JSONArray getFaceApiInfo() {
        return faceApiInfo;
    }

    public String getResultImageString() {
        return resultImageString;
    }

    public void setMultiPartFile(MultipartFile multiPartFile) {
        this.multiPartFile = multiPartFile;
    }

    @Override
    protected AbstractFaceEmotion convertOriginalBufferedImage() {
        this.originalBufferedImage = ic.convertMultifileToBufferedImage(this.multiPartFile);
        return this;
    }

    @Override
    protected AbstractFaceEmotion convertOriginalImageToByteArray() {
        this.originalImageBytes = ic.bufferedImageToByteArray(this.originalBufferedImage);
        return this;
    }

    @Override
    protected AbstractFaceEmotion sendFaceApi() {
        this.faceApiResultString = fas.faceDetect(this.originalImageBytes);
        return this;
    }

    @Override
    protected AbstractFaceEmotion parseFaceApiResult() {
        this.faceApiInfo = new JSONArray(this.faceApiResultString);
        return this;
    }

    @Override
    protected AbstractFaceEmotion drawFaceRectangle() {
        this.originalBufferedImage = fid.drawFaceRectangles(this.faceApiInfo, this.originalBufferedImage);
        return this;
    }

    @Override
    protected AbstractFaceEmotion drawFaceEmotionParts() {
        this.emotionPartsImage = new BufferedImage(this.originalBufferedImage.getWidth(), this.originalBufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        this.emotionPartsImage = fid.drawFacesData(this.faceApiInfo, this.emotionPartsImage);
        return this;
    }

    @Override
    protected AbstractFaceEmotion mergeRectangleAndEmotionParts() {
        this.resultImage = fid.mergeImages(this.originalBufferedImage, this.emotionPartsImage);
        return this;
    }

    @Override
    protected AbstractFaceEmotion convertImageToByteArray() {
        this.originalImageBytes = ic.bufferedImageToByteArray(this.resultImage);
        return this;
    }

    @Override
    protected AbstractFaceEmotion encoding() {
        this.resultImageString = ic.byteArrayToBase64String(this.originalImageBytes);
        return this;
    }

    @Override
    public String getRestultImageString() {
        return this.resultImageString;
    }
}
