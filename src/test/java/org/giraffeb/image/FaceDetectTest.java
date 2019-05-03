package org.giraffeb.image;


import org.apache.commons.io.IOUtils;
import org.giraffeb.facade.FaceDetectFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FaceDetectTest {


    @Autowired
    FaceDetectFacade faceDetectFacade;

    public static final String TEST_URI = "http://post.phinf.naver.net/20161014_169/1476426748865BB0yM_PNG/ITZAMCHAqBlGwfvBq8BnNYKuIq-Q.jpg";

    @Test
    public void faceDectecFromUriImageTest(){

        String base64Image = faceDetectFacade.requestFaceAPIFromUriImage(TEST_URI);
        assertThat(base64Image).isNotNull();
    }

    @Test
    public void faceDectecFromMultiparFileTest(){

        byte[] imageByteArary = null;

        try {
            URL url = new URL(TEST_URI);
            imageByteArary = IOUtils.toByteArray(url);

        } catch (IOException e) {
            e.printStackTrace();
        }

        MultipartFile multiImage = new MockMultipartFile("testImage", imageByteArary);

        String base64Image = faceDetectFacade.requestFaceAPIFromMultiPartFile(multiImage);
        assertThat(base64Image).isNotNull();
    }

}
