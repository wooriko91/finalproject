package com.it.wecodeyou.curriculum.controlller;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.it.wecodeyou.curriculum.model.CurriculumVO;
import com.it.wecodeyou.curriculum.service.ICurriculumService;
import com.it.wecodeyou.episode.service.IEpisodeService;
import com.it.wecodeyou.member.service.IMemberService;
import com.it.wecodeyou.off.service.IOffService;
import com.it.wecodeyou.on.service.IOnService;
import com.it.wecodeyou.product.model.ProductVO;
import com.it.wecodeyou.product.service.IProductService;
import com.it.wecodeyou.review.model.ReviewVO;
import com.it.wecodeyou.review.model.ShowDetailVO;
import com.it.wecodeyou.review.service.ReviewService;
import com.it.wecodeyou.schedule.service.IScheduleService;
import com.it.wecodeyou.sub_product.service.SubProductService;
import com.it.wecodeyou.tag.service.ITagService;

@RestController
@RequestMapping("/curriculum")
public class CurriculumController {

   @Autowired
   private ICurriculumService service;
      
   @Autowired
   private IProductService pservice;
   
   @Autowired
   private IEpisodeService eservice;

   @Autowired
   private ReviewService rservice;
   
   @Autowired
   private SubProductService spservice;
   
   @Autowired
   private IOnService onservice;

   @Autowired
   private IOffService offservice;
   
   @Autowired
   private ITagService tagservice;

   @Autowired
   private IMemberService mservice;
   
   @Autowired
   private IScheduleService scheduleservice;
   
	//커리큘럼소개 main 요청 (==> 온라인, 오프라인 통합 main임. 맵핑 주소 이름 변경 요망)
	@GetMapping("/on_main")
	public ModelAndView curriculumOnMain(ModelAndView mv, HttpServletRequest req) {
	
	System.out.println("서비스 전 컨트롤러"+req.getParameter("seq"));
	mv.setViewName("curriculum/onMain");
	CurriculumVO cvo = service.showCurriculum(Integer.parseInt(req.getParameter("seq")));
	mv.addObject("c", cvo);
	System.out.println("서비스 후 컨트롤러"+cvo);
	return mv;
	      
	}

	
	//커리큘럼소개 sub 요청 (==> 온라인 detatil page, 오프라인 detatil 페이지 구분. 맵핑 요망)
	@GetMapping("/sub")
	public ModelAndView curriculumSub(ModelAndView mv, HttpServletRequest req) throws SQLException {
		ProductVO pvo = pservice.getOneByName(req.getParameter("s"));
		
		ArrayList<ReviewVO> r_list = rservice.getAllReviewByLec(pvo.getProductNo());
		ArrayList<ShowDetailVO> sd_list = new ArrayList<ShowDetailVO>();
		for (int i = 0; i < r_list.size(); i++) {
			ShowDetailVO sdvo = new ShowDetailVO();
			sdvo.setReviewNo(r_list.get(i).getReviewNo());
			sdvo.setContent(r_list.get(i).getContent());
			sdvo.setReviewCreatedAt(r_list.get(i).getReviewCreatedAt());
			sdvo.setReviewProductNo(r_list.get(i).getReviewProductNo());
			sdvo.setReviewStar(r_list.get(i).getReviewStar());
			// 이름에 * 넣기
			String hiddenName = mservice.checkLogin(r_list.get(i).getReviewUser()).getUserName();
			if(hiddenName.length()<3) {
				hiddenName = hiddenName.substring(0,1)+"*";
			}else if(hiddenName.length()==3) {
				hiddenName = hiddenName.substring(0,1)+"*"+hiddenName.substring(2,3);
			}else {// 3보다 크면
				hiddenName = hiddenName.substring(0,1)+"**"+hiddenName.substring(3,hiddenName.length());				
			}
			sdvo.setUserName(hiddenName); 
			sd_list.add(sdvo);
		}
		mv.addObject("sd_list",sd_list);
		
		int sum = 0;
		for (int i = 0; i < r_list.size(); i++) {
			sum += r_list.get(i).getReviewStar();
		}
		float avg = 0;
		if(r_list.size() != 0) {
			avg = sum/r_list.size(); 
		}
		
		mv.addObject("eventList",scheduleservice.getEvent(offservice.getInfoByProductNo(pvo.getProductNo())));
		
		mv.addObject("tag", tagservice.searchTags(pvo.getProductNo()));
		mv.addObject("sub_pro", spservice.showSubPro(pvo.getProductNo()));
		mv.addObject("s", req.getParameter("s"));
		mv.addObject("pro",pvo);
		mv.addObject("review_num",r_list.size());	// 수강후기 갯수
		mv.addObject("avg",avg);	// 별점 평균
		System.out.println(pvo.getProductNo());
		if(pvo.getProductType().equals("1")) {
			mv.setViewName("curriculum/offDetail");
			mv.addObject("off",offservice.getInfoByProductNo(pvo.getProductNo()));
			mv.addObject("episodeList",scheduleservice.getOneSchedule(offservice.getInfoByProductNo((pvo.getProductNo()))));
			System.out.println("현장강의 선택");
		}else {
			
			//에피소드 리스트 
			mv.addObject("episodeList",eservice.getAllEpisode1(pvo.getProductNo()));
			mv.addObject("sensei",onservice.getAuthor(pvo.getProductNo()));
			mv.setViewName("curriculum/onDetail");
			System.out.println("온라인 선택");
		}
		return mv;
		
	}
	
	//online detail 페이지 임시 겟 맵핑 
	@GetMapping("/on_detail")
	public ModelAndView on_detatil() {
		ModelAndView mv = new ModelAndView();
	    mv.setViewName("curriculum/onDetail");
	    
	    return mv;
	}
   
	//online detail 페이지 임시 겟 맵핑 
	@GetMapping("/off_detail")
	public ModelAndView odd_detatil() {
		ModelAndView mv = new ModelAndView();
	    mv.setViewName("curriculum/offDetail");
	    return mv;
	}
   
	@GetMapping("/purchase")
	public ModelAndView purchasePage(HttpServletRequest req) {
		ModelAndView mv = new ModelAndView();
		int pro_no = Integer.parseInt(req.getParameter("pro_no"));
		mv.addObject("sub_info",spservice.showSubPro(pro_no));
		mv.addObject("pro_info",pservice.getOneInfo(pro_no));
		mv.setViewName("curriculum/purchasePage");
		return mv;
	}
	
}




