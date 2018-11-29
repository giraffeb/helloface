package org.giraffeb;

import com.mortennobel.imagescaling.DimensionConstrain;
import com.mortennobel.imagescaling.ResampleOp;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * 이미지 주소에서 이미지를 받고
 * 적당한 크기로 리사이징해서
 * ms cognitive api서버로 요청함.
 *
 *
 * */

//TODO : 메소드들을 유틸리티 클래스로 분리할지 판단할 것.
@Controller
public class FaceController {

	public static int fileCounter = 0;

	final private static int MAX_EMOTION_SIZE = 3;

	@Autowired
	FaceApiSend fas;

	@RequestMapping("/")
	@ResponseBody
	public String home() {
		return "home";
	}

	@RequestMapping("/face")
	public String index(Model model) {
		model.addAttribute("name", "SpringBlog from Millky");
		return "face";
	}

	/*
	 * TODO: 1) 이미지 리사이즈, API, 이미지 재생성 합치기 기능별로 분리하기 2)
	 */
	@RequestMapping("/faceupload")
	public String upload(@RequestParam("file") MultipartFile file, 	Model model) {

        String base64EncodedImageByteArray;
        byte[] resultImageByteArr;
        BufferedImage userImage = null;

        try{
            userImage = ImageIO.read(file.getInputStream());

        }catch(Exception e){
            e.printStackTrace();
        }
        resultImageByteArr = getEmotionImageByteArrayFromUserImage(userImage);
        base64EncodedImageByteArray = byteArrayToBase64String(resultImageByteArr);

		model.addAttribute("file", base64EncodedImageByteArray);
		// model.addAttribute("result", result);
		return "result";
	}


	@RequestMapping("/faceuri")
    public String getUri(@RequestParam("uri") String uri, Model model){
//        byte[] resultImageByteArr;
//        String base64EncodedImageByteArray;
//
//
//        BufferedImage userImage = getUriImageToBufferedImage(uri);
//        resultImageByteArr = getEmotionImageByteArrayFromUserImage(userImage);
//        base64EncodedImageByteArray = byteArrayToBase64String(resultImageByteArr);
//
//        model.addAttribute("file", base64EncodedImageByteArray);


        model.addAttribute("file",  uriUpload(uri).get("file"));

	    return "result";
    }


	/**
     * 카카오톡 서버에서 받은 이미지 주소로
     * 이미지를 저장하고 적당하게 리사이징해서
     * ms api로 넘김.
     *
     * */
	public Map<String, Object> uriUpload(String uri) {
		HashMap<String, Object> model = new HashMap<String, Object>();

        BufferedImage userImage = getUriImageToBufferedImage(uri);

        byte[] resultImageByteArr = getEmotionImageByteArrayFromUserImage(userImage);
        String base64EncodedImageByteArray = byteArrayToBase64String(resultImageByteArr);

		model.put("file", base64EncodedImageByteArray);
		return model;
	}

	//utility methods

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

    /**
     * MS Emotion Api로 가져온 JSONArray값을 이용해서
     * 얼굴위치에 사각형과 사각형 식별 번호를 그림.
     * 그려진 새로운 BufferedImage를 반환
     * */
    public BufferedImage drawFaceRectangles(JSONArray analysedEmotionJsonArray, BufferedImage targetImage){
	    int limitEmotionSize = MAX_EMOTION_SIZE;

	    int width = targetImage.getWidth();
        int height = targetImage.getHeight();

        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
	    Graphics2D canvas = (Graphics2D)newImage.getGraphics();
	    canvas.drawImage(targetImage, 0,0, null);

	    //분석된 표정이 3개 미만일 경우에는 그 숫자만큼 그리기위함
        if (analysedEmotionJsonArray.length() < MAX_EMOTION_SIZE) {
            limitEmotionSize = analysedEmotionJsonArray.length();
        }
        //
        JSONObject detactedFaceRect;
        for (int i = 0; i < limitEmotionSize; i++) {
            detactedFaceRect = analysedEmotionJsonArray.getJSONObject(i).getJSONObject("faceRectangle");

            canvas.setColor(Color.RED);//사각형 색 지정
            canvas.setStroke(new BasicStroke(4)); //사각형 선 두께 지정
            canvas.setFont(new Font("TimesRoman", Font.PLAIN, 30)); //사각형 번호 폰트 스타일, 사이즈 지정
            canvas.drawString(String.valueOf(i + 1), (Integer) detactedFaceRect.get("left"), (Integer) detactedFaceRect.get("top") + 30); //사각형 번호 부여, 좌표지정
            canvas.drawRect((Integer) detactedFaceRect.get("left"), (Integer) detactedFaceRect.get("top"), (Integer) detactedFaceRect.get("width"), (Integer) detactedFaceRect.get("height")); //사각형 그리기
        }

	    return newImage;
    }

    /**
     * MS Emotion Api로 가져온 JSONArray값을 이용해서
     * 사각형 식별번호에 해당하는 항목 값들을 그림
     * BufferedImage로 반환
     * */
    public BufferedImage drawEmotionScores(JSONArray analysedEmotionJsonArray, BufferedImage targetImage){
        int limitEmotionSize = MAX_EMOTION_SIZE;
        int fontSize = 20;
        int fontYPosition = 0;

        int width = targetImage.getWidth();
        int scoreListLength = analysedEmotionJsonArray.getJSONObject(0).getJSONObject("scores").length() + 1;//분석항목 수 + 사각형 식별번호 수

        if (analysedEmotionJsonArray.length() < MAX_EMOTION_SIZE) {
            limitEmotionSize = analysedEmotionJsonArray.length();
        }

        BufferedImage scoreImage = new BufferedImage(width, fontSize * scoreListLength * limitEmotionSize, BufferedImage.TYPE_INT_BGR);

        Graphics2D canvas = (Graphics2D) scoreImage.getGraphics();
        canvas.setColor(Color.WHITE);
        canvas.setFont(new Font("TimesRoman", Font.PLAIN, 20));

        for (int i = 0; i < limitEmotionSize; i++) {
            JSONObject tempObj = analysedEmotionJsonArray.getJSONObject(i).getJSONObject("scores");

            fontYPosition += fontSize;
            canvas.drawString("id : " + String.valueOf(i + 1), 0, fontYPosition);

            Iterator<String> itemIterator = tempObj.keys();
            String emotionItem = null;
            while(itemIterator.hasNext()){
                emotionItem = itemIterator.next();
                fontYPosition += fontSize;

                Double score = Double.parseDouble(String.format("%.2f", tempObj.get(emotionItem)));
                String itemNameWithScoreString = emotionItem + " : " + String.valueOf(score);

                canvas.drawString(itemNameWithScoreString, 0, fontYPosition);
            }
        }
        return scoreImage;
    }

    /**
     * 얼굴인식 사각형이 그려진 BufferedImage와 항목값이 그려진 BufferedImage를
     * 하나의 BufferedImage로 병합함.
     * BufferedImage를 반환
     * */
    public BufferedImage mergeImages(BufferedImage rectangleDrawnImage, BufferedImage scoreImage) {
        int width = rectangleDrawnImage.getWidth();
        int height = rectangleDrawnImage.getHeight() + scoreImage.getHeight();

        BufferedImage mergedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D canvas = (Graphics2D) mergedImage.getGraphics();
        canvas.setBackground(Color.WHITE);
        canvas.drawImage(rectangleDrawnImage, 0, 0, null);
        canvas.drawImage(scoreImage, 0, rectangleDrawnImage.getHeight(), null);

        return mergedImage;
    }

    /**
     * userImage를 multipartFile로 업로드하는경우와
     * uri로 전송받는 경우 2가지를 분리하기 위한 목적
     * */
    public byte[] getEmotionImageByteArrayFromUserImage(BufferedImage userImage){

        BufferedImage resizedUserImage = null;
        byte[] imgByteArr = null;

        int userImageHeight = 0 , userImageWidth = 0;

        try {

            userImageHeight = userImage.getHeight();
            userImageWidth = userImage.getWidth();
			/*
			* 사용자가보낸 이미지의 크기가 640보다 크다면
			* 리사이징 한다.
			* */
            if (userImageWidth > 640 || userImageHeight > 640) {
                resizedUserImage = resizeBufferedImage(userImage);
            }
			/*
			* 이미지크기가 640보다 작다면 그냥 보낸다.
			* */
            //TODO-LIST : 표정분석이 안되는 이미지 혹은 너무 작은 이미지에 대한 예외처리가 필요.
            else {
                resizedUserImage = userImage;
            }
            imgByteArr = bufferedImageToByteArray(resizedUserImage);
            // EMOTION API다녀오기 : JSON type String
            // 인식된 얼굴에 번호 아이디 부여, 좌표값 : 인식된 얼굴의 위치를 나타내는 사각형 , 각 얼굴의 표정 정보
            JSONArray analysedEmotionJsonArray = fas.msEmotionApiToJSonArray(imgByteArr);

            //결과로 받은 String : json plain text를 객체로 변환하는 과정.
            //+그래픽 객체로 이미지들을 그리는 과정.
            if (analysedEmotionJsonArray == null) {
                //TODO : 얼굴인식이 실패한 경우 처리해야함.
                //이렇게 if-else말고 아래서서 정리하고 만들어도 된다.
            }
            else {

                //BufferedImage를 편집하기 위해 그래픽 객체를 얻음.
                //resizedImage위에 사각형을 그리기 위해서 다시 그래픽 객체를 얻어옴.
                BufferedImage rectangleDrawnImage = drawFaceRectangles(analysedEmotionJsonArray, resizedUserImage);

                // 얼굴 표정 분석 텍스트 -> 이미지화
                // fontsize : 20
                //scoreImage 덩어리와 아리
                BufferedImage scoreImage = drawEmotionScores(analysedEmotionJsonArray, resizedUserImage);

                //얼굴인식 사각형이 그려진 resizedImage와 점수가 적힌 scoreImage를 합치기 위한 BufferedImage
                BufferedImage mergedImage = mergeImages(rectangleDrawnImage, scoreImage);

                //byte[] return
                imgByteArr = bufferedImageToByteArray(mergedImage);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgByteArr;
    }

}
