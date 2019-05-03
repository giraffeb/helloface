package org.giraffeb.controller;

import org.giraffeb.facade.FaceDetectFacade;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



/**
 * @auther giraffeb
 *
 * kakao 플러스 친구 server
 * 메시지 상호 api들을 정의함.
 * */

@Controller
public class KakaoTalkFriendController {

	private Logger logger = LoggerFactory.getLogger(KakaoTalkFriendController.class);

	private FaceDetectFacade faceDetectFacade;

	public KakaoTalkFriendController(FaceDetectFacade faceDetectFacade) {
		this.faceDetectFacade = faceDetectFacade;
	}

	@Value("${spring.application.name}")
	private String appName;

	@Value("${my.domain.address}")
	private String domainName;

	@RequestMapping(path="/test", method=RequestMethod.GET)
	public String getTestPage(){
		return "test";
	}

	/**
	 * 사용자가 보낸 메시지를 타입별로 다르게 응답함.
	 *
	 * @param json
	 * parameter json 형태
	 * {
		"text": "안녕하세요.",
		"photo": {
					"url": "https://hello.photo.src",
					"width": 640,
					"height": 480
					},
			"message_button": {
								"label": "반갑습니다.",
								"url": "http://hello.world.com/example"
								}
		}
	 *
	 * @return String emoition api 요청 주소
	 * */

	/**
	 * 기능 리스트를 버튼으로 제공해 주는 api인데, 필요 없음. 기능 제공 안함.
	 */

	/**
	 * kakao smart chatting api
	 * 사용자가 미리 설정된 키워드를 사용자에게 보여주기 위한 api
	 *
	 * @return String message list
	 * */
	@RequestMapping(path="/keyboard", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> homeKeyboard(Model model){
		Map<String, Object> jsonObject =  new HashMap<String, Object>();
		jsonObject.put("type", "buttons");
		ArrayList<String> myList = new ArrayList<>();
		myList.add("선택 1");
		myList.add("선택 2");
		myList.add("선택 3");
		jsonObject.put("buttons", myList);

		return jsonObject;
	}



	@RequestMapping(path="/message", method=RequestMethod.POST, produces={"application/json; charset=UTF-8"})
	public @ResponseBody String getTest2(@RequestBody String json){
		logger.info(json);

		JSONObject map = new JSONObject(json);
		logger.info(map.get("user_key").toString());
		logger.info(map.get("type").toString());
		logger.info(map.get("content").toString());
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("message", new JSONObject());
		jsonObj.getJSONObject("message").put("text", "메시지를 성공적으로 받았습니다.");
		
		
		String type = (String)map.get("type");
		if(type.equals("photo")){
			String uri = (String)map.get("content");
			jsonObj.getJSONObject("message").put("text", "표정을 분석했습니다.");

			jsonObj.getJSONObject("message").put("message_button", new JSONObject());
			jsonObj.getJSONObject("message").getJSONObject("message_button").put("label", "반갑습니다.");
			jsonObj.getJSONObject("message").getJSONObject("message_button").put("url","https://"+this.domainName+"/"+this.appName+"/emotion?uri="+uri);

        }

		return jsonObj.toString();
	}


	/**
	 * 사용자에게 받은 이미지 주소로 표정분석을 api로 요청 후 결과받음.
	 * 문제점 : 단계도 더 거쳐야하고 느림.
	 * 			- 위의 단계에서 처리하는 것도 좋겠음.
	 *
	 * 	-> 이미지를 가져올때 multipartFile or uri
	 * 	-> 동일
	 * 	-> 결과물 인코딩 방식 동일.
	 * @param params "uri":value 카카오톡에 전송된 이미지 주소를 가지고 있음.
	 * @return Map<String, Object>"file" : base64-encoded-img
	 * */
	@RequestMapping(path="/emotion", method=RequestMethod.GET)
	public String message(@RequestParam Map<String, Object> params, Model model){
		String uri = (String)params.get("uri");
		logger.info("URI-> "+uri);

		model.addAttribute("file", faceDetectFacade.requestFaceAPIFromUriImage(uri));

		return "emotion";
	}
}
