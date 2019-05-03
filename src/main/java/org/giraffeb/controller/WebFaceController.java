package org.giraffeb.controller;

import org.giraffeb.facade.FaceDetectFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * 이미지 주소에서 이미지를 받고
 * 적당한 크기로 리사이징해서
 * ms cognitive api서버로 요청함.
 *
 *
 * */


@Controller
public class WebFaceController {

	private Logger logger = LoggerFactory.getLogger(WebFaceController.class);

	private FaceDetectFacade faceDetectFacade;

	public WebFaceController(FaceDetectFacade faceDetectFacade) {
		this.faceDetectFacade = faceDetectFacade;
	}


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


	@RequestMapping("/faceupload")
	public String upload(@RequestParam("file") MultipartFile file, 	Model model) {

		model.addAttribute("file", faceDetectFacade.requestFaceAPIFromMultiPartFile(file));

		return "result";
	}


	@RequestMapping("/faceuri")
    public String getUri(@RequestParam("uri") String uri, Model model){

        model.addAttribute("file",  faceDetectFacade.requestFaceAPIFromUriImage(uri));

	    return "result";
    }


}
