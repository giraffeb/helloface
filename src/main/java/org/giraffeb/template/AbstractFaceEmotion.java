package org.giraffeb.template;

public abstract class AbstractFaceEmotion {


    protected abstract AbstractFaceEmotion convertOriginalBufferedImage();
    protected abstract AbstractFaceEmotion convertOriginalImageToByteArray();
    protected abstract AbstractFaceEmotion sendFaceApi();
    protected abstract AbstractFaceEmotion parseFaceApiResult();

    //TODO: drawFaceRectangle(), drawFaceEmotionParts() 그릴때 데이터구조를 해석하는 부분 없이 단순하게 만들기.
    protected abstract AbstractFaceEmotion drawFaceRectangle();
    protected abstract AbstractFaceEmotion drawFaceEmotionParts();
    protected abstract AbstractFaceEmotion mergeRectangleAndEmotionParts();
    protected abstract AbstractFaceEmotion convertImageToByteArray();
    protected abstract AbstractFaceEmotion encoding();

    public abstract String getRestultImageString();

    public void doProcess(){
        this.convertOriginalBufferedImage()
            .convertOriginalImageToByteArray()
            .sendFaceApi()
            .parseFaceApiResult()
            .drawFaceRectangle()
            .drawFaceEmotionParts()
            .mergeRectangleAndEmotionParts()
            .convertImageToByteArray()
            .encoding();
    }

}
