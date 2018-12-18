package org.giraffeb.implementation;

import org.giraffeb.template.AbstractFaceEmotion;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Qualifier("feiu")
@Component
public class FaceEmotionImpleFromUri extends FaceEmotionImplFromMultiPartFile {

    protected String uri;


    public void setUri(String uri){
        this.uri = uri;
    }

    @Override
    protected AbstractFaceEmotion convertOriginalBufferedImage() {
        this.originalBufferedImage = ic.getUriImageToBufferedImage(this.uri);
        return this;
    }
}