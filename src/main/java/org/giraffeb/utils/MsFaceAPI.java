package org.giraffeb.utils;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Properties;


/**
 * MS cognitive emotion api를 사용하는 example code
 * 사용하는 환경에 맞게 변경함.
 * */

@Component
public class MsFaceAPI {

	private Logger logger = LoggerFactory.getLogger(MsFaceAPI.class);

	//application.properties에 저장함.
	@Value("${ms.cognitive.emotion.key}")
	String apiKey;

	/**
	 * faceDetect에서 가져온 결과값을 분석해서 Json Array로 변경해주기
	 * @param imgByteArray
	 * @return
	 */
	public JSONArray sendToFaceDetectAPIWithImageByteArray(byte[] imgByteArray){
		JSONArray analysedEmotionJsonArray = null;

		String emotionAnalysisResult = faceDetect(imgByteArray);

		if(emotionAnalysisResult != null){
			analysedEmotionJsonArray = new JSONArray(emotionAnalysisResult);
		}
		return analysedEmotionJsonArray;
	}

	/**
	 *
	 * ms cognitive api : 언굴인식 api
	 *
	 * @param  img : 사용자가 보낸 이미지 byte array
	 *
	 * */
	public String faceDetect(byte[] img) {
		Properties msApiProperties = new Properties();
		HttpClient httpclient = HttpClients.createDefault();
		String result = null;
		try {

			URIBuilder builder = new URIBuilder("https://eastus.api.cognitive.microsoft.com/face/v1.0/detect");

			builder.setParameter("returnFaceId", "true");
			builder.setParameter("returnFaceLandmarks", "false");
			builder.setParameter("returnFaceAttributes", "emotion");

			URI uri = builder.build();
			HttpPost request = new HttpPost(uri);
			request.setHeader("Content-Type", "application/octet-stream");
			request.setHeader("Ocp-Apim-Subscription-Key", apiKey);

			// Request body
			ByteArrayEntity reqEntity = new ByteArrayEntity(img);
			request.setEntity(reqEntity);

			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				result = EntityUtils.toString(entity);
				logger.info(result);
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return result;
	}

}
