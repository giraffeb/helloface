package org.giraffeb.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;

@Component
public class FaceEmotionDataImageDraw {

    private Logger logger = LoggerFactory.getLogger(FaceEmotionDataImageDraw.class);

    final private static int MAX_EMOTION_SIZE = 7;


    public BufferedImage drawFaceRectangles(JSONArray analysedEmotionJsonArray, BufferedImage targetImage){
        int limitEmotionSize = MAX_EMOTION_SIZE;

        int width = targetImage.getWidth();
        int height = targetImage.getHeight();

        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        Graphics2D canvas = (Graphics2D)newImage.getGraphics();
        canvas.drawImage(targetImage, 0,0, null);

        //최대 인식 개수를 MAX_EMOTION_SIZE로 정하고 그 아래일 경우만 그림.
        if (analysedEmotionJsonArray.length() < MAX_EMOTION_SIZE) {
            limitEmotionSize = analysedEmotionJsonArray.length();
        }

        JSONObject detactedFaceRect = null;

        for (int i = 0; i < limitEmotionSize; i++) {
            detactedFaceRect = analysedEmotionJsonArray.getJSONObject(i)
                                                        .getJSONObject("faceRectangle");

            canvas.setColor(Color.RED);//사각형 색 지정
            canvas.setStroke(new BasicStroke(4)); //사각형 선 두께 지정
            canvas.setFont(new Font("TimesRoman", Font.PLAIN, 30)); //사각형 번호 폰트 스타일, 사이즈 지정
            canvas.drawString(String.valueOf(i + 1), (Integer) detactedFaceRect.get("left"), (Integer) detactedFaceRect.get("top") + 30); //사각형 번호 부여, 좌표지정
            canvas.drawRect((Integer) detactedFaceRect.get("left"), (Integer) detactedFaceRect.get("top"), (Integer) detactedFaceRect.get("width"), (Integer) detactedFaceRect.get("height")); //사각형 그리기
        }

        return newImage;
    }



    public BufferedImage drawFacesData(JSONArray analysedEmotionJsonArray, BufferedImage targetImage){
        int maxFaceNumber = MAX_EMOTION_SIZE;
        int fontSize = 20;
        int fontYPosition = 0;

        //점수를 부분을 붙일 타겟이미지.
        int width = targetImage.getWidth();
        int faceDectectedEmotionLength = analysedEmotionJsonArray.getJSONObject(0).getJSONObject("faceAttributes").getJSONObject("emotion").length() + 1;//분석항목 수 + 사각형 식별번호 수

        if (analysedEmotionJsonArray.length() < MAX_EMOTION_SIZE) {
            maxFaceNumber = analysedEmotionJsonArray.length();
        }

        BufferedImage faceEmotionInfoAreaImage = new BufferedImage(width, fontSize * faceDectectedEmotionLength * maxFaceNumber, BufferedImage.TYPE_INT_BGR);

        Graphics2D canvas = (Graphics2D) faceEmotionInfoAreaImage.getGraphics();
        canvas.setColor(Color.WHITE);
        canvas.setFont(new Font("TimesRoman", Font.PLAIN, 20));

        for (int i = 0; i < maxFaceNumber; i++) {
            JSONObject emotionInfoObject = analysedEmotionJsonArray.getJSONObject(i)
                                                        .getJSONObject("faceAttributes")
                                                        .getJSONObject("emotion");

            fontYPosition += fontSize;
            canvas.drawString("id : " + String.valueOf(i + 1), 0, fontYPosition);

            Iterator<String> emotionItemIterator = emotionInfoObject.keys();
            String emotionItem = null;

            while(emotionItemIterator.hasNext()){
                emotionItem = emotionItemIterator.next();
                fontYPosition += fontSize;

                Double emotionScore = Double.parseDouble(String.format("%.2f", emotionInfoObject.get(emotionItem)));
                String emotionFieldAndValue = emotionItem + " : " + String.valueOf(emotionScore);

                canvas.drawString(emotionFieldAndValue, 0, fontYPosition);
            }
        }
        return faceEmotionInfoAreaImage;
    }


    /**
     * 얼굴인식 사각형이 그려진 BufferedImage와 항목값이 그려진 BufferedImage를
     * 하나의 BufferedImage로 병합함.
     * BufferedImage를 반환
     * */
    public BufferedImage mergeImages(BufferedImage targetImage, BufferedImage scoreAreaImage) {
        int width = targetImage.getWidth();
        int height = targetImage.getHeight() + scoreAreaImage.getHeight();

        BufferedImage mergedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D canvas = (Graphics2D) mergedImage.getGraphics();
        canvas.setBackground(Color.WHITE);
        canvas.drawImage(targetImage, 0, 0, null);
        canvas.drawImage(scoreAreaImage, 0, targetImage.getHeight(), null);

        return mergedImage;
    }



}
