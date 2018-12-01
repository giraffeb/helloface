package org.giraffeb;

import org.giraffeb.controller.FaceController;
import org.giraffeb.utils.FaceApiSend;
import org.giraffeb.utils.FaceImageDraw;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class JustTest {

    @Autowired
    FaceApiSend faceApiSend;

    @Autowired
    FaceController faceController;

    @Autowired
    FaceImageDraw fid;


    @Test
    public void HelloTest(){
        System.out.println("Hello this is me");
    }

//    @Test
//    public void emotionApiTest(){
//        String uri = "https://search.pstatic.net/common?type=a&size=120x150&quality=95&direct=true&src=http%3A%2F%2Fsstatic.naver.net%2Fpeople%2F43%2F201806251404067001.jpg";
//        BufferedImage img = faceController.getUriImageToBufferedImage(uri);
//        byte[] buf = faceController.bufferedImageToByteArray(img);
//        String result = faceApiSend.faceDetect(buf);
//
//        System.out.println(result);
//
//    }


    @Test
    public void parseJson(){
        String temp = "[{\"faceId\":\"48a8fa7b-f213-4a48-afaf-b3ebba99ec4f\",\"faceRectangle\":{\"top\":451,\"left\":448,\"width\":173,\"height\":173},\"faceAttributes\":{\"emotion\":{\"anger\":0.0,\"contempt\":0.004,\"disgust\":0.0,\"fear\":0.0,\"happiness\":0.001,\"neutral\":0.983,\"sadness\":0.012,\"surprise\":0.0}}},{\"faceId\":\"24b30e71-2666-4a92-b043-6aeaa6ee904b\",\"faceRectangle\":{\"top\":67,\"left\":343,\"width\":172,\"height\":172},\"faceAttributes\":{\"emotion\":{\"anger\":0.0,\"contempt\":0.001,\"disgust\":0.0,\"fear\":0.0,\"happiness\":0.0,\"neutral\":0.997,\"sadness\":0.002,\"surprise\":0.0}}},{\"faceId\":\"80eebce9-bbf9-479b-8ae0-8a8832e893fe\",\"faceRectangle\":{\"top\":458,\"left\":0,\"width\":169,\"height\":172},\"faceAttributes\":{\"emotion\":{\"anger\":0.0,\"contempt\":0.0,\"disgust\":0.0,\"fear\":0.0,\"happiness\":0.0,\"neutral\":0.982,\"sadness\":0.017,\"surprise\":0.001}}},{\"faceId\":\"a1f6bc12-4b5a-4522-b41b-afaf342354eb\",\"faceRectangle\":{\"top\":73,\"left\":84,\"width\":169,\"height\":169},\"faceAttributes\":{\"emotion\":{\"anger\":0.0,\"contempt\":0.002,\"disgust\":0.0,\"fear\":0.0,\"happiness\":0.073,\"neutral\":0.922,\"sadness\":0.002,\"surprise\":0.0}}},{\"faceId\":\"d7b43eb1-3351-4978-9975-dc63b72f89b6\",\"faceRectangle\":{\"top\":75,\"left\":613,\"width\":156,\"height\":156},\"faceAttributes\":{\"emotion\":{\"anger\":0.0,\"contempt\":0.0,\"disgust\":0.0,\"fear\":0.0,\"happiness\":0.0,\"neutral\":0.988,\"sadness\":0.012,\"surprise\":0.0}}},{\"faceId\":\"c62970c0-a1bf-46e7-a79d-dbc5bc96125f\",\"faceRectangle\":{\"top\":476,\"left\":284,\"width\":149,\"height\":149},\"faceAttributes\":{\"emotion\":{\"anger\":0.0,\"contempt\":0.002,\"disgust\":0.0,\"fear\":0.0,\"happiness\":0.0,\"neutral\":0.84,\"sadness\":0.157,\"surprise\":0.0}}},{\"faceId\":\"77d279f9-6657-46b1-b89d-674f87e7ae4c\",\"faceRectangle\":{\"top\":462,\"left\":712,\"width\":137,\"height\":148},\"faceAttributes\":{\"emotion\":{\"anger\":0.0,\"contempt\":0.0,\"disgust\":0.0,\"fear\":0.0,\"happiness\":0.0,\"neutral\":0.991,\"sadness\":0.002,\"surprise\":0.007}}}]";
//        JSONObject obj = new JSONObject(temp);

        JSONArray arr = new JSONArray(temp);


        for(int i=0;i<arr.length();i++){
            JSONObject obj = (JSONObject) arr.get(i);
            System.out.println(obj);

        }
    }

    @Test
    public void drawEmotionDataTest() throws IOException {
        String temp = "[{\"faceId\":\"48a8fa7b-f213-4a48-afaf-b3ebba99ec4f\",\"faceRectangle\":{\"top\":451,\"left\":448,\"width\":173,\"height\":173},\"faceAttributes\":{\"emotion\":{\"anger\":0.0,\"contempt\":0.004,\"disgust\":0.0,\"fear\":0.0,\"happiness\":0.001,\"neutral\":0.983,\"sadness\":0.012,\"surprise\":0.0}}},{\"faceId\":\"24b30e71-2666-4a92-b043-6aeaa6ee904b\",\"faceRectangle\":{\"top\":67,\"left\":343,\"width\":172,\"height\":172},\"faceAttributes\":{\"emotion\":{\"anger\":0.0,\"contempt\":0.001,\"disgust\":0.0,\"fear\":0.0,\"happiness\":0.0,\"neutral\":0.997,\"sadness\":0.002,\"surprise\":0.0}}},{\"faceId\":\"80eebce9-bbf9-479b-8ae0-8a8832e893fe\",\"faceRectangle\":{\"top\":458,\"left\":0,\"width\":169,\"height\":172},\"faceAttributes\":{\"emotion\":{\"anger\":0.0,\"contempt\":0.0,\"disgust\":0.0,\"fear\":0.0,\"happiness\":0.0,\"neutral\":0.982,\"sadness\":0.017,\"surprise\":0.001}}},{\"faceId\":\"a1f6bc12-4b5a-4522-b41b-afaf342354eb\",\"faceRectangle\":{\"top\":73,\"left\":84,\"width\":169,\"height\":169},\"faceAttributes\":{\"emotion\":{\"anger\":0.0,\"contempt\":0.002,\"disgust\":0.0,\"fear\":0.0,\"happiness\":0.073,\"neutral\":0.922,\"sadness\":0.002,\"surprise\":0.0}}},{\"faceId\":\"d7b43eb1-3351-4978-9975-dc63b72f89b6\",\"faceRectangle\":{\"top\":75,\"left\":613,\"width\":156,\"height\":156},\"faceAttributes\":{\"emotion\":{\"anger\":0.0,\"contempt\":0.0,\"disgust\":0.0,\"fear\":0.0,\"happiness\":0.0,\"neutral\":0.988,\"sadness\":0.012,\"surprise\":0.0}}},{\"faceId\":\"c62970c0-a1bf-46e7-a79d-dbc5bc96125f\",\"faceRectangle\":{\"top\":476,\"left\":284,\"width\":149,\"height\":149},\"faceAttributes\":{\"emotion\":{\"anger\":0.0,\"contempt\":0.002,\"disgust\":0.0,\"fear\":0.0,\"happiness\":0.0,\"neutral\":0.84,\"sadness\":0.157,\"surprise\":0.0}}},{\"faceId\":\"77d279f9-6657-46b1-b89d-674f87e7ae4c\",\"faceRectangle\":{\"top\":462,\"left\":712,\"width\":137,\"height\":148},\"faceAttributes\":{\"emotion\":{\"anger\":0.0,\"contempt\":0.0,\"disgust\":0.0,\"fear\":0.0,\"happiness\":0.0,\"neutral\":0.991,\"sadness\":0.002,\"surprise\":0.007}}}]";

        JSONArray array = new JSONArray(temp);
        BufferedImage bi = new BufferedImage(600,600,BufferedImage.TYPE_INT_ARGB);
        BufferedImage result = fid.drawFacesData(array, bi);

        File file = new File("temp.jpg");
        ImageIO.write(result, "jpg", file);

    }

}
