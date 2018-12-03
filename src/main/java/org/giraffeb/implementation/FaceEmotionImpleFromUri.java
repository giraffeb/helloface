package org.giraffeb.implementation;

import org.giraffeb.template.AbstractFaceEmotion;
import org.springframework.stereotype.Component;

@Component
public class FaceEmotionImpleFromUri extends FaceEmotionImplFromMultiPartFile {

    protected String uri;


    public void setUri(String uri){
        this.uri = uri;
    }

    @Override
    protected AbstractFaceEmotion convertOriginalBufferedImage() {
        this.originalBufferedImage = ic.getUriImageToBufferedImage(this.uri);
        return super.convertOriginalBufferedImage();
    }
}
