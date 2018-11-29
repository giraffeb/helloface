package org.giraffeb;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Properties;


/**
 * MS cognitive emotion api를 사용하는 example code
 * 사용하는 환경에 맞게 변경함.
 * */

@Component
public class FaceApiSend {

	//application.properties에 저장함.
	@Value("${ms.cognitive.emotion.key}")
	String apiKey;
	/**
	 * ms cognitive api : 얼굴인식 + 감정인식 api
	 * @param  imgByteArray : 사용자에게 받은 이미지 byte array
	 * */

	public String msEmotionApi(byte[] imgByteArray) {
		HttpClient httpclient = HttpClients.createDefault();
		String result = null;
		Properties msApiProperties = new Properties();
		try {

			URIBuilder builder = new URIBuilder("https://westus.api.cognitive.microsoft.com/emotion/v1.0/recognize");

			URI uri = builder.build();
			HttpPost request = new HttpPost(uri);
			request.setHeader("Content-Type", "application/octet-stream");
			request.setHeader("Ocp-Apim-Subscription-Key", apiKey);

			// Request body
			ByteArrayEntity bEntity = new ByteArrayEntity(imgByteArray);
			request.setEntity(bEntity);

			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				result = EntityUtils.toString(entity);
				System.out.println(result);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return result;
	}

	public JSONArray msEmotionApiToJSonArray(byte[] imgByteArray){
		JSONArray analysedEmotionJsonArray = null;

		String emotionAnalysisResult = msEmotionApi(imgByteArray);
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

			URIBuilder builder = new URIBuilder("https://westus.api.cognitive.microsoft.com/face/v1.0/detect");

			builder.setParameter("returnFaceId", "true");
			builder.setParameter("returnFaceLandmarks", "false");

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
				System.out.println(result);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return result;
	}

}
