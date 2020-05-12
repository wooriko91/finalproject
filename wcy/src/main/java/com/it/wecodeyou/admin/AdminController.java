package com.it.wecodeyou.admin;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.it.wecodeyou.member.service.IMemberService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private IMemberService memberService;
	
   	//admin page 호출
   	@GetMapping("")
	public ModelAndView admin(ModelAndView mv) throws SQLException {
   		System.out.println("/admin : GET 요청 발생!");
   		//
   		
   		mv.setViewName("admin/adminpage");
   		return mv;
   	}
   	
   	//admin page 호출
   	@GetMapping("/user")
	public ModelAndView user(ModelAndView mv) throws SQLException {
   		System.out.println("/user : GET 요청 발생!");
   		//
   		
   		mv.setViewName("admin/user");
   		return mv;
   	}
   	
   	//admin page 호출
   	@GetMapping("/etc")
	public ModelAndView etc(ModelAndView mv) throws SQLException {
   		System.out.println("/etc : GET 요청 발생!");
   		//
   		
   		mv.setViewName("admin/etc");
   		return mv;
   	}

}