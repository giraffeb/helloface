package org.giraffeb.utils;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GetImageTest {



//    @Autowired
//    GetImageTemplateImp gi;
//
//    @Autowired
//    FaceApiTemplateImp fat;
//
//    @Autowired
//    ImageConvertor ic;
//
//    @Autowired
//    FaceImageDraw fid;


//    @Test
//    public void hello(){
//
//        Path path = Paths.get("/Users/parkraewook/IdeaProjects/helloface/src/test/java/org/giraffeb/utils/aaa.jpg");
//        String name = "aaa.jpg";
//        String originalFileName = "aaa.jpg";
//        String contentType = "image/jpeg";
//
//        byte[] content = null;
//        try {
//            content = Files.readAllBytes(path);
//        } catch (final IOException e) {
//        }
//        MultipartFile result = new MockMultipartFile(name,
//                originalFileName, contentType, content);
//
//        byte[] bytes = gi.getImage(result);
//        String temp = ic.byteArrayToBase64String(bytes);
//
//        System.out.println(temp);
//
//    }
//
//    @Test
//    public void faceApiTest(){
//        Path path = Paths.get("/Users/parkraewook/IdeaProjects/helloface/src/test/java/org/giraffeb/utils/aaa.jpg");
//        String name = "aaa.jpg";
//        String originalFileName = "aaa.jpg";
//        String contentType = "image/jpeg";
//
//        byte[] content = null;
//        try {
//            content = Files.readAllBytes(path);
//        } catch (final IOException e) {
//        }
//        MultipartFile result = new MockMultipartFile(name,
//                originalFileName, contentType, content);
//
//        byte[] bytes = gi.getImage(result);
//
//        String temp = fat.sendApi(bytes);
//        System.out.println(temp);
//
//    }


//    @Test
//    public void drawRectangleTest() throws IOException {
//        Path path = Paths.get("/Users/parkraewook/IdeaProjects/helloface/src/test/java/org/giraffeb/utils/aaa.jpg");
//        String name = "aaa.jpg";
//        String originalFileName = "aaa.jpg";
//        String contentType = "image/jpeg";
//
//        byte[] content = null;
//        try {
//            content = Files.readAllBytes(path);
//        } catch (final IOException e) {
//        }
//        MultipartFile result = new MockMultipartFile(name,
//                originalFileName, contentType, content);
//
//        BufferedImage originalImg = ic.convertMultifileToBufferedImage(result);
//
//        byte[] bytes = gi.getImage(result);
//        String temp = fat.sendApi(bytes);
//        JSONArray array = fat.parseResult(temp);
//
//        BufferedImage resultImage = fid.drawFaceRectangles(array, originalImg);
//        File target = new File("result.jpg");
//        ImageIO.write(resultImage, "jpg", target);
//
//    }


//    @Test
//    public void totlaFaceApiTest() throws IOException {
//        Path path = Paths.get("/Users/parkraewook/IdeaProjects/helloface/src/test/java/org/giraffeb/utils/aaa.jpg");
//        String name = "aaa.jpg";
//        String originalFileName = "aaa.jpg";
//        String contentType = "image/jpeg";
//
//        byte[] content = null;
//        try {
//            content = Files.readAllBytes(path);
//        } catch (final IOException e) {
//        }
//        MultipartFile result = new MockMultipartFile(name,
//                originalFileName, contentType, content);
//
//        BufferedImage originalImg = ic.convertMultifileToBufferedImage(result);
//
//        byte[] bytes = gi.getImage(result);
//        String temp = fat.sendApi(bytes);
//        JSONArray array = fat.parseResult(temp);
//
//        BufferedImage rectImage = fid.drawFaceRectangles(array, originalImg);
//
//        BufferedImage emotionImage = new BufferedImage(rectImage.getWidth(), rectImage.getHeight(), BufferedImage.TYPE_INT_RGB);
//        emotionImage = fid.drawFacesData(array, emotionImage);
//        BufferedImage resultImage = fid.mergeImages(rectImage, emotionImage);
//
//        File target = new File("result.jpg");
//        ImageIO.write(resultImage, "jpg", target);
//
//
//    }

    @Test
    public void jenkinsTest2(){
        System.out.println("just test commit.");
    }
}
