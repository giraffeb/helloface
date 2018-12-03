package org.giraffeb.controller;

import org.giraffeb.implementation.FaceEmotionImplFromMultiPartFile;

import org.giraffeb.utils.FaceApiSend;
import org.giraffeb.utils.FaceImageDraw;
import org.giraffeb.utils.ImageConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

//TODO : 메소드들을 유틸리티 클래스로 분리할지 판단할 것.
@Controller
public class FaceController {

	@Autowired
    FaceApiSend fas;

	@Autowired
    FaceImageDraw fid;

	@Autowired
    ImageConvertor ic;

	@Qualifier("fati")
	@Autowired
	FaceEmotionImplFromMultiPartFile fati;

    /**
     *
     * @return simple String return
     */
	@RequestMapping("/")
	@ResponseBody
	public String home() {
		return "home";
	}


    /**
     *
     * @param model
     * @return view resource
     */
	@RequestMapping("/face")
	public String index(Model model) {
	    //just example
		model.addAttribute("name", "SpringBlog from Millky");
		return "face";
	}

	/*
	 * TODO: 1) 이미지 리사이즈, API, 이미지 재생성 합치기 기능별로 분리하기 2)
	 */
	@RequestMapping("/faceupload")
	public String upload(@RequestParam("file") MultipartFile file, 	Model model) {

		fati.setMultiPartFile(file);
		fati.doProcess();

		model.addAttribute("file", fati.getRestultImageString());
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


        model.addAttribute("file",  ic.uriUpload(uri).get("file"));

	    return "result";
    }


    @RequestMapping("/testface")
	public String mockTest(@RequestParam("file") MultipartFile file, Model model){
		fati.setMultiPartFile(file);
		fati.doProcess();

		model.addAttribute("file", fati.getRestultImageString());
		// model.addAttribute("result", result);
		return "result";
	}


	//utility methods

}
