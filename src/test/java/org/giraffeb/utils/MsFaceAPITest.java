package org.giraffeb.utils;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MsFaceAPITest {

    public static final String TEST_URI = "http://post.phinf.naver.net/20161014_169/1476426748865BB0yM_PNG/ITZAMCHAqBlGwfvBq8BnNYKuIq-Q.jpg";
    public static final String WRONG_IMAGE_URI = "https://store.storeimages.cdn-apple.com/8756/as-images.apple.com/is/image/AppleInc/aos/published/images/a/ir/airpods/wireless/airpods-wireless-charge-case-201903?wid=300&hei=390&fmt=png-alpha&qlt=80&.v=1551389040514";

    @Autowired
    public MsFaceAPI msFaceAPI;

    public static Logger logger = LoggerFactory.getLogger(MsFaceAPI.class);

    @Test
    public void sendToFaceDetectAPIWithImageByteArrayTest() {

        byte[] imageBytes = null;
         try {
             URL url = new URL(TEST_URI);
             imageBytes = IOUtils.toByteArray(url);

        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray result = msFaceAPI.sendToFaceDetectAPIWithImageByteArray(imageBytes);
        logger.info(result.toString());
        assertThat(result).isNotEmpty();
    }

    @Test
    public void faceDetectAPITest(){
        byte[] imageBytes = null;
        try {
            URL url = new URL(TEST_URI);
            imageBytes = IOUtils.toByteArray(url);

        } catch (IOException e) {
            e.printStackTrace();
        }
        String plainResult = msFaceAPI.faceDetect(imageBytes);
        logger.info(plainResult);
        assertThat(plainResult).isNotEqualTo("[]");

    }


}