package com.it.wecodeyou;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.it.wecodeyou.board.model.ArticleVO;
import com.it.wecodeyou.board.model.BoardVO;
import com.it.wecodeyou.board.model.ShowNoticeVO;
import com.it.wecodeyou.board.service.ArticleService;
import com.it.wecodeyou.board.service.BoardService;
import com.it.wecodeyou.curriculum.model.CurriculumVO;
import com.it.wecodeyou.curriculum.service.CurriculumService;
import com.it.wecodeyou.curriculum.service.ICurriculumService;
import com.it.wecodeyou.product.service.ProductService;
import com.it.wecodeyou.sub_product.model.SubProductVO;
import com.it.wecodeyou.sub_product.service.SubProductService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	@Autowired
	private SubProductService spservice;
	   
	@Autowired
	private ProductService pservice;

	@Autowired
	private ArticleService aservice;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		// 32부터 81 사이 48과 75를 제외한 8개를 랜덤 배출
		ArrayList<SubProductVO> ten_list = new ArrayList<SubProductVO>();
		int[] nums = new int[8];
		for (int i = 0; i < nums.length; i++) {	
			int num = (int)(Math.random() * 48)+1;			
			nums[i] = num;
			for (int j = 0; j < i; j++) {
				if(nums[i] == nums[j] || num == 15 || num == 16 || num == 42 || num == 43) {
					i--;break;
				}	
			}
		}	
		for (int i = 0; i < nums.length; i++) {			
			ten_list.add(spservice.showAllPro().get(nums[i]));
		}
		for (int i = 0; i < ten_list.size(); i++) {
			ten_list.get(i).setSpBook(pservice.getOneInfo(ten_list.get(i).getSpProId()).getProductName());
		}
		model.addAttribute("sp_data", ten_list);
		// 과목별 인기강의 포장
		
		ArrayList<ArticleVO> before_list = aservice.NotiList(8);
		ArrayList<ShowNoticeVO> noti_list = new ArrayList<>();		
		for (int i = 0; i < before_list.size(); i++) {
			ShowNoticeVO snvo = new ShowNoticeVO();
			snvo.setArticleNo(before_list.get(i).getArticleNo());
			snvo.setArticleTitle(before_list.get(i).getArticleTitle());
			java.sql.Date d = new java.sql.Date(before_list.get(i).getArticleCreatedAt().getTime());
			snvo.setArticleCreatedAt(d);
			noti_list.add(snvo);
		}
		model.addAttribute("noti_list", noti_list);
		
		
		//model.addAttribute("serverTime", formattedDate );
		return "home";
	}
	
	@GetMapping("/greeting")
	public String greetingPage() {
		return "intro/greeting";
	}
	@GetMapping("/intro")
	public String introPage() {
		return "intro/intro";
	}
	@GetMapping("/location")
	public String locationPage() {
		return "intro/location";
	}
	@GetMapping("/helpsite")
	public String helpsitePage() {
		return "help-site/help-site";
	}
	
	@GetMapping("/test01")
	public String test01() {
		return "test/test01";
	}

	@GetMapping("/test02")
	public String test02() {
		return "test/test02";
	}
	@RequestMapping("/daumeditor/img-popup")
	public String imagePopup() {
		return "/include/image";
	}
	
	
	/*
	 *	이미지 업로드 사용법
	 *	폼 속성에 >>  encType=multipart/form-data << 추가
	 *	 인풋 태그 추가 <input  type="file" name="Filedata"/> 
	 */
	@PostMapping("/singleUploadImageAjax")
	public @ResponseBody HashMap singleUploadImageAjax(
			@RequestParam("filedata") MultipartFile multipartFile, HttpSession session) {
		System.out.println("POST: /singleUploadImageAjax 파일 업로드 ");
		System.out.println(multipartFile);
		HashMap fileInfo = new HashMap(); //CallBack할 때 이미지 정보를 담을 Map
		
		if(multipartFile != null && !(multipartFile.getOriginalFilename().equals(""))) {
			System.out.println("확장자, 파일 명 확인");
			//확장자 제한
			String originalName = multipartFile.getOriginalFilename(); //실제 파일명
			String originalNameExtension = originalName.substring(originalName.lastIndexOf(".")+1).toLowerCase();
			if(!(originalNameExtension.equals("jpg") || originalNameExtension.equals("gif") ||
					originalNameExtension.equals("png") || originalNameExtension.equals("bmp"))){
				System.out.println("파일확장자 : " + originalNameExtension);
				fileInfo.put("result", -1);
				return fileInfo;
			}
			
			long fileSize = multipartFile.getSize();
			long limitFileSize = 5*1024*1024;
			if(limitFileSize < fileSize) {
				System.out.println("파일 사이즈 : " + fileSize);
				fileInfo.put("result", -2);
			}
			//저장경로
			String defaultPath = session.getServletContext().getRealPath("/");
			String path = defaultPath + File.separator + "upload" + File.separator + "board" + File.separator + "images" + File.separator + "";
			
			File file = new File(path);
			//디렉토리 없을시 생성
			if(!file.exists()) {
				System.out.println("업로드폴더 생성");
				file.mkdirs();
			}
			//파일 저장명 처리
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			String today=formatter.format(new Date());
			String modifyName = today + "-" + UUID.randomUUID().toString().substring(20) + "." + originalNameExtension;
			
			//Multipart 처리
			try {
				multipartFile.transferTo(new File(path + modifyName));
				
				//로그
				System.out.println("파일 업로드 정보");
				System.out.println("path : " + path);
				System.out.println("originalName: " + originalName);
				System.out.println("modifyName: "+ modifyName);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("이미지 파일 업로드 실패 -singleUploadimageAjax");
			}
			
			// CallBack - Map에 담기
			String imageurl = session.getServletContext().getContextPath() + "/upload/board/images/" + modifyName; // separator와는 다름! 
			fileInfo.put("imageurl", imageurl); // 상대파일경로(사이즈변환이나 변형된 파일) 
			fileInfo.put("filename", modifyName); // 파일명 
			fileInfo.put("filesize", fileSize); // 파일사이즈 
			fileInfo.put("imagealign", "C"); // 이미지정렬(C:center)
			fileInfo.put("originalurl", imageurl); // 실제파일경로 
			fileInfo.put("thumburl", imageurl); // 썸네일파일경로(사이즈변환이나 변형된 파일)
			
			fileInfo.put("result", 1); // -1, -2를 제외한 아무거나 싣어도 됨 }
			}
		return fileInfo; // @ResponseBody 어노테이션을 사용하여 Map을  
	}
	

	
	
}
