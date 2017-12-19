package org.giraffeb;

import com.mortennobel.imagescaling.DimensionConstrain;
import com.mortennobel.imagescaling.ResampleOp;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
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
 * @auther giraffeb
 *
 * 이미지 주소에서 이미지를 받고
 * 적당한 크기로 리사이징해서
 * ms cognitive api서버로 요청함.
 *
 *
 * */


@Controller
public class FaceController {

	public static int fileCounter = 0;

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

		String result = null;
		String img64 = null;
		BufferedImage bfdImage = null, resizedImage = null;
		ByteArrayOutputStream baos = null;
		double ratio = 0;
		byte[] byteArr = null;

		try {
			bfdImage = ImageIO.read(file.getInputStream());
			int height = bfdImage.getHeight();
			int width = bfdImage.getWidth();
			int resizedHeight = 0, resizedWidth = 0;
			System.out.println("height : " + height + ", width : " + width);

			if (width > 640 || height > 640) {
				if (width > height) {
					ratio = (double) height / (double) width;
					DimensionConstrain dc = DimensionConstrain.createAbsolutionDimension(640, (int) (640 * ratio));
					ResampleOp op = new ResampleOp(dc);
					resizedHeight = (int) (640 * ratio);
					resizedWidth = 640;
					resizedImage = op.doFilter(bfdImage, resizedImage, resizedWidth, resizedHeight);
					System.out.println("ratio : " + ratio + ", value : " + 640 * ratio);

				} else {
					ratio = (double) width / (double) height;
					DimensionConstrain dc = DimensionConstrain.createAbsolutionDimension((int) (480 * ratio), 480);
					ResampleOp op = new ResampleOp(dc);
					resizedHeight = 480;
					resizedWidth = (int) (480 * ratio);
					resizedImage = op.doFilter(bfdImage, resizedImage, resizedWidth, resizedHeight);
					System.out.println("ratio : " + ratio + ", value : " + 640 * ratio);
				}
				baos = new ByteArrayOutputStream();
				ImageIO.write(resizedImage, "jpg", baos);
				baos.flush();
				byteArr = baos.toByteArray();
				baos.close();

			} else {
				byteArr = file.getBytes();
				System.out.println("pass");
			}

			img64 = Base64.getEncoder().encodeToString(byteArr);

			// EMOTION API다녀오기 : JSON type String
			// 인식된 얼굴에 번호 아이디 부여, 좌표값 : 인식된 얼굴의 위치를 나타내는 사각형 , 각 얼굴의 표정 정보
			result = fas.faceSend(byteArr);
			if (result == null) {
				//TODO : 얼굴인식이 실패한 경우 처리
				//이렇게 if-else말고 아래서서 정리하고 만들어도 된다.
				
			} 
			else {

				JSONArray ja = new JSONArray(result);
				JSONObject obj = null;
				Graphics gh = null;
				int maxEmotionSize = 3;
				if (resizedImage == null) {
					resizedWidth = width;
					resizedHeight = height;
					resizedImage = ImageIO.read(file.getInputStream());
				}
				gh = (Graphics2D) resizedImage.getGraphics();
				Graphics2D gh2 = (Graphics2D) gh;

				if (ja.length() < 3) {
					maxEmotionSize = ja.length();
				}

				for (int i = 0; i < maxEmotionSize; i++) {
					obj = ja.getJSONObject(i);
					JSONObject rect = (JSONObject) obj.get("faceRectangle");
					System.out.println("#" + i + " " + rect);
					System.out.println("#" + (Integer) rect.get("left") + ", #" + (Integer) rect.get("top") + ", #"
							+ (Integer) rect.get("width") + ", #" + (Integer) rect.get("height"));
					gh2.setColor(Color.RED);
					gh2.setStroke(new BasicStroke(4));
					gh2.setFont(new Font("TimesRoman", Font.PLAIN, 30));
					gh2.drawString(String.valueOf(i + 1), (Integer) rect.get("left"), (Integer) rect.get("top") + 30);
					gh2.drawRect((Integer) rect.get("left"), (Integer) rect.get("top"), (Integer) rect.get("width"),
							(Integer) rect.get("height"));

				}
				// 얼굴 표정 분석 텍스트 -> 이미지화
				// fontsize : 20
				JSONObject sc = ja.getJSONObject(0).getJSONObject("scores");
				BufferedImage scoreImage = new BufferedImage(resizedWidth, 20 * 9 * maxEmotionSize,
						BufferedImage.TYPE_INT_BGR);
				Graphics2D scgh = (Graphics2D) scoreImage.getGraphics();
				scgh.setColor(Color.WHITE);
				scgh.setFont(new Font("TimesRoman", Font.PLAIN, 20));
				int y = 0;
				int distance = 20;

				JSONArray rs = new JSONArray(result);
				for (int i = 0; i < maxEmotionSize; i++) {
					JSONObject tempObj = rs.getJSONObject(i).getJSONObject("scores");
					String key = null;
					y += distance;
					scgh.drawString("id : " + String.valueOf(i + 1), 0, y);
					for (Iterator<String> itr = tempObj.keys(); itr.hasNext();) {
						key = itr.next();
						y += distance;
						Double value = Double.parseDouble(String.format("%.2f", tempObj.get(key)));
						String tempstr = key + " : " + String.valueOf(value);
						scgh.drawString(tempstr, 0, y);

						System.out.println(tempstr);
					}
				}

				ByteArrayOutputStream baost = new ByteArrayOutputStream();

				BufferedImage mergedImage = new BufferedImage(resizedWidth, resizedHeight + y,
						BufferedImage.TYPE_INT_RGB);
				Graphics2D mg = (Graphics2D) mergedImage.getGraphics();
				mg.setBackground(Color.WHITE);
				mg.drawImage(resizedImage, 0, 0, null);
				mg.drawImage(scoreImage, 0, resizedImage.getHeight(), null);

				// System.out.println(ja);
				baos = new ByteArrayOutputStream();
				ImageIO.write(mergedImage, "jpg", baos);
				baos.flush();
				byteArr = baos.toByteArray();
				baos.close();
				img64 = Base64.getEncoder().encodeToString(byteArr);
			}
			// System.out.println(img64);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		model.addAttribute("file", img64);
		// model.addAttribute("result", result);
		return "result";
	}


	/**
     * 카카오톡 서버에서 받은 이미지 주소로
     * 이미지를 저장하고 적당하게 리사이징해서
     * ms api로 넘김.
     *
     * */
	public Map<String, Object> uriUpload(String uri) {
		MultipartFile file = null;
		
		HashMap<String, Object> model = new HashMap<String, Object>();
		String result = null;
		String img64 = null;
		BufferedImage bfdImage = null, resizedImage = null;
		ByteArrayOutputStream baos = null;
		double ratio = 0;
		byte[] byteArr = null;

		try {
			file = getUriImageToByteArr(uri);
			bfdImage = ImageIO.read(file.getInputStream());
			int height = bfdImage.getHeight();
			int width = bfdImage.getWidth();
			int resizedHeight = 0, resizedWidth = 0;
			System.out.println("height : " + height + ", width : " + width);

			if (width > 640 || height > 640) {
				if (width > height) {
					ratio = (double) height / (double) width;
					DimensionConstrain dc = DimensionConstrain.createAbsolutionDimension(640, (int) (640 * ratio));
					ResampleOp op = new ResampleOp(dc);
					resizedHeight = (int) (640 * ratio);
					resizedWidth = 640;
					resizedImage = op.doFilter(bfdImage, resizedImage, resizedWidth, resizedHeight);
					System.out.println("ratio : " + ratio + ", value : " + 640 * ratio);

				} else {
					ratio = (double) width / (double) height;
					DimensionConstrain dc = DimensionConstrain.createAbsolutionDimension((int) (480 * ratio), 480);
					ResampleOp op = new ResampleOp(dc);
					resizedHeight = 480;
					resizedWidth = (int) (480 * ratio);
					resizedImage = op.doFilter(bfdImage, resizedImage, resizedWidth, resizedHeight);
					System.out.println("ratio : " + ratio + ", value : " + 640 * ratio);
				}
				baos = new ByteArrayOutputStream();
				ImageIO.write(resizedImage, "jpg", baos);
				baos.flush();
				byteArr = baos.toByteArray();
				baos.close();

			} else {
				byteArr = file.getBytes();
				System.out.println("pass");
			}

			img64 = Base64.getEncoder().encodeToString(byteArr);

			// EMOTION API다녀오기 : JSON type String
			// 인식된 얼굴에 번호 아이디 부여, 좌표값 : 인식된 얼굴의 위치를 나타내는 사각형 , 각 얼굴의 표정 정보
			result = fas.faceSend(byteArr);
			if (result == null) {
				//TODO : 얼굴인식이 실패한 경우 처리
				//이렇게 if-else말고 아래서서 정리하고 만들어도 된다.
				
			} 
			else {

				JSONArray ja = new JSONArray(result);
				JSONObject obj = null;
				Graphics gh = null;
				int maxEmotionSize = 3;
				if (resizedImage == null) {
					resizedWidth = width;
					resizedHeight = height;
					resizedImage = ImageIO.read(file.getInputStream());
				}
				gh = (Graphics2D) resizedImage.getGraphics();
				Graphics2D gh2 = (Graphics2D) gh;

				if (ja.length() < 3) {
					maxEmotionSize = ja.length();
				}

				for (int i = 0; i < maxEmotionSize; i++) {
					obj = ja.getJSONObject(i);
					JSONObject rect = (JSONObject) obj.get("faceRectangle");
					System.out.println("#" + i + " " + rect);
					System.out.println("#" + (Integer) rect.get("left") + ", #" + (Integer) rect.get("top") + ", #"
							+ (Integer) rect.get("width") + ", #" + (Integer) rect.get("height"));
					gh2.setColor(Color.RED);
					gh2.setStroke(new BasicStroke(4));
					gh2.setFont(new Font("TimesRoman", Font.PLAIN, 30));
					gh2.drawString(String.valueOf(i + 1), (Integer) rect.get("left"), (Integer) rect.get("top") + 30);
					gh2.drawRect((Integer) rect.get("left"), (Integer) rect.get("top"), (Integer) rect.get("width"),
							(Integer) rect.get("height"));

				}
				// 얼굴 표정 분석 텍스트 -> 이미지화
				// fontsize : 20
				JSONObject sc = ja.getJSONObject(0).getJSONObject("scores");
				BufferedImage scoreImage = new BufferedImage(resizedWidth, 20 * 9 * maxEmotionSize,
						BufferedImage.TYPE_INT_BGR);
				Graphics2D scgh = (Graphics2D) scoreImage.getGraphics();
				scgh.setColor(Color.WHITE);
				scgh.setFont(new Font("TimesRoman", Font.PLAIN, 20));
				int y = 0;
				int distance = 20;

				JSONArray rs = new JSONArray(result);
				for (int i = 0; i < maxEmotionSize; i++) {
					JSONObject tempObj = rs.getJSONObject(i).getJSONObject("scores");
					String key = null;
					y += distance;
					scgh.drawString("id : " + String.valueOf(i + 1), 0, y);
					for (Iterator<String> itr = tempObj.keys(); itr.hasNext();) {
						key = itr.next();
						y += distance;
						Double value = Double.parseDouble(String.format("%.2f", tempObj.get(key)));
						String tempstr = key + " : " + String.valueOf(value);
						scgh.drawString(tempstr, 0, y);

						System.out.println(tempstr);
					}
				}

				ByteArrayOutputStream baost = new ByteArrayOutputStream();

				BufferedImage mergedImage = new BufferedImage(resizedWidth, resizedHeight + y,
						BufferedImage.TYPE_INT_RGB);
				Graphics2D mg = (Graphics2D) mergedImage.getGraphics();
				mg.setBackground(Color.WHITE);
				mg.drawImage(resizedImage, 0, 0, null);
				mg.drawImage(scoreImage, 0, resizedImage.getHeight(), null);

				// System.out.println(ja);
				baos = new ByteArrayOutputStream();
				ImageIO.write(mergedImage, "jpg", baos);
				baos.flush();
				byteArr = baos.toByteArray();
				baos.close();
				img64 = Base64.getEncoder().encodeToString(byteArr);
				
			}
			// System.out.println(img64);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		model.put("file", img64);
		// model.addAttribute("result", result);
		return model;
	}
	
	public MultipartFile getUriImageToByteArr(String uri) {
		MultipartFile f = null;
		try {
			URL url = new URL(uri);
			InputStream is = url.openStream();
			f = new MockMultipartFile("temp1", is);
			
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return f;
	}

}
