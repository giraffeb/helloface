package org.giraffeb.image;


import com.mortennobel.imagescaling.ResampleFilters;
import com.mortennobel.imagescaling.ResampleOp;
import org.giraffeb.utils.FaceEmotionDataImageDraw;
import org.giraffeb.utils.ImageByteConvertor;
import org.giraffeb.utils.MsFaceAPI;
import org.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageTest {

    static String imagePath = "/Users/parkraewook/IdeaProjects/helloface/src/test/java/org/giraffeb/image/test.jpg";

    @Autowired
    MsFaceAPI msFaceAPI;

    @Autowired
    ImageByteConvertor imageByteConvertor;

    @Autowired
    FaceEmotionDataImageDraw faceEmotionDataImageDraw;

    @Test
    public void initTest(){
        System.out.println("Hello new world");
    }

    @Test
    public void fileToMultipartFileTest(){

        Path imagePath = Paths.get(ImageTest.imagePath);

        String name = "object.jpg";
        String originalFileName = "test.jpg";
        String contentType = "text/plain";

        byte[] content = null;
        try {
            content = Files.readAllBytes(imagePath);
        } catch (final IOException e) {
        }

        MultipartFile result = new MockMultipartFile(name, originalFileName, contentType, content);
        System.out.println(result);

    }

    @Test
    public void multipartFileToImageBufferTest() throws IOException {
        System.out.println(System.getProperty("user.dir"));
        Path path = Paths.get(ImageTest.imagePath);
        File file = path.toFile();

        BufferedImage bufferedImage = ImageIO.read(file);

        System.out.println(file.exists());

    }

    @Test
    public void bufferedImageToByteArrayTest() throws IOException {
        BufferedImage bufferedImage = (BufferedImage) ImageIO.read(new File(ImageTest.imagePath));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);

        byte[] imagebytes = byteArrayOutputStream.toByteArray();
        System.out.println(imagebytes.length);

    }

    @Test
    public void imageResizeTest() throws IOException {
        BufferedImage originalImage = (BufferedImage) ImageIO.read(new File(ImageTest.imagePath));

        int originalImageHeight = originalImage.getHeight();
        int originalImageWidth = originalImage.getWidth();

        int resizedImageHeight = 0;
        int resizedImageWidth = 0;

        double imageRatio = 0;
        if(originalImageHeight > originalImageWidth){
            imageRatio = originalImageWidth / originalImageHeight;

            resizedImageHeight = 480;
            resizedImageWidth = (int)(resizedImageHeight * imageRatio);

        }else{
            imageRatio = originalImageHeight / originalImageWidth;
            resizedImageWidth = 640;
            resizedImageHeight = (int)(resizedImageWidth * imageRatio);
        }

        //resize api
        ResampleOp resizeOp = new ResampleOp((int)resizedImageWidth, (int)resizedImageHeight);
        resizeOp.setFilter(ResampleFilters.getLanczos3Filter());
        BufferedImage scaledImage = resizeOp.filter(originalImage, null);

        ImageIO.write(scaledImage, "jpg", new File("result.jpg"));

    }


    @Test
    public void testImageToAll() throws IOException {

        String uri = "https://mblogthumb-phinf.pstatic.net/MjAxODAzMzBfMjU2/MDAxNTIyMzQ0MTg3MDcz._ZP1g3lm_JUhdT6oannmSAeLb3oBXm5__EUY2ZXrnDMg.eme9ygrGMxaFD18qWjUPYailsXlTDicSLXT1DDR2WUMg.JPEG.agni0613/20180330_022215.jpg?type=w2";


        //#1 uri -> BufferedImage
        BufferedImage userImage = imageByteConvertor.getUriImageToBufferedImage(uri);
        //#2 resize image
        BufferedImage resizedUserImage = imageByteConvertor.resizeBufferedImage(userImage);
        //#3 BufferedImage -> byte[]
        byte[] userImageByteArray = imageByteConvertor.bufferedImageToByteArray(resizedUserImage);
        //#4 byte[] -> send to Ms Face API
        JSONArray faceApiResult = msFaceAPI.sendToFaceDetectAPIWithImageByteArray(userImageByteArray);
        //#5 JSONArray -> draw score from face api result
        BufferedImage rectBufferedImage = faceEmotionDataImageDraw.drawFaceRectangles(faceApiResult, resizedUserImage);
        BufferedImage faceEmotionImage = faceEmotionDataImageDraw.drawFacesData(faceApiResult, rectBufferedImage);

        BufferedImage resultImage = faceEmotionDataImageDraw.mergeImages(rectBufferedImage, faceEmotionImage);


        ImageIO.write(resultImage, "jpg", new File("test.jpg"));
    }


}
