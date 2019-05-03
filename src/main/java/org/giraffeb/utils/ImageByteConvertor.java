package org.giraffeb.utils;

import com.mortennobel.imagescaling.DimensionConstrain;
import com.mortennobel.imagescaling.ResampleOp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

@Component
public class ImageByteConvertor {


    private Logger logger = LoggerFactory.getLogger(ImageByteConvertor.class);

    /**
     * uri주소의 이미지를 받아와서
     * BufferedImage객체로 반환함.
     * */
    public BufferedImage getUriImageToBufferedImage(String uri) {
        MultipartFile f = null;
        BufferedImage bfdimg = null;
        try {
            URL url = new URL(uri);
            InputStream is = url.openStream();
            bfdimg = ImageIO.read(is);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bfdimg;
    }

    /**
     *
     * @param file
     * @return
     */
    public BufferedImage convertMultifileToBufferedImage(MultipartFile file) {
        BufferedImage userImage = null;

        try{
            userImage = ImageIO.read(file.getInputStream());

        }catch(Exception e){
            e.printStackTrace();
        }

        return userImage;
    }

    /**
     *
     * @param file
     * @return
     */
    public BufferedImage convertFileToBufferedImage(File file) {
        BufferedImage userImage = null;

        try{
            userImage = ImageIO.read(file);

        }catch(Exception e){
            e.printStackTrace();
        }

        return userImage;
    }

    /**
     * byte[]를 base64로 인코딩한 문자열 반환.
     * */
    public String byteArrayToBase64String(byte[] byteArr){
        return Base64.getEncoder().encodeToString(byteArr);
    }

    /**
     * BufferedImage를 byte[]로 변환함.
     * */
    public byte[] bufferedImageToByteArray(BufferedImage img){

        byte[] byteArr = null;
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            ImageIO.write(img, "jpg", baos);
            baos.flush();
            byteArr = baos.toByteArray();
            baos.close();
        }catch(Exception e ){
            e.printStackTrace();
        }
        return byteArr;
    }

    /**
     * 사용자가 보낸 이미지가 가로 640, 세로 480보다 크면 리사이징함.
     * BufferedImage를 반환.
     * */
    public BufferedImage resizeBufferedImage(BufferedImage img){
        BufferedImage resizedImage = null;
        ByteArrayOutputStream baos = null;


        int userImageHeight = img.getHeight();
        int userImageWidth = img.getWidth();

        double ratio = 0;
        int resizedUserImageHeight, resizedUserImageWidth;


        if (userImageWidth > userImageHeight) {
            ratio = (double) userImageHeight / (double) userImageWidth;
            resizedUserImageHeight = (int) (640 * ratio);
            resizedUserImageWidth = 640;

        } else {
            ratio = (double) userImageWidth / (double) userImageHeight;
            resizedUserImageHeight = 480;
            resizedUserImageWidth = (int) (480 * ratio);

        }
        DimensionConstrain dc = DimensionConstrain.createAbsolutionDimension(resizedUserImageWidth, resizedUserImageHeight);
        ResampleOp op = new ResampleOp(dc);
        resizedImage = op.doFilter(img, resizedImage, resizedUserImageWidth, resizedUserImageHeight);

        return resizedImage;
    }

}
