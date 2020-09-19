package com.koreait.matzip.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.koreait.matzip.Const;
import com.koreait.matzip.ViewRef;
import com.koreait.matzip.user.model.UserDTO;
import com.koreait.matzip.user.model.UserVO;

/*
 *  클래스이름 위에 @.... 적는건 99% bean 등록이라고 보면됨
 */

// Controller (맵핑담당)
@Controller
@RequestMapping("/user")
public class UserController {
	
	
	// UserService 클래스 보면 @Service 라고 해놨기에 걔를 찾아냄
	@Autowired  
	private UserService service;
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String login(Model model, @RequestParam(required = false, defaultValue="5") int err) {
		model.addAttribute(Const.TITLE,"로그인");
		model.addAttribute(Const.VIEW,"user/login");
		
		model.addAttribute("msg",err); // 학원 마지막 시간에 일단 login POST에서 날라온값 받아오는 역활 해놨음 
		return ViewRef.TEMP_DEFAULT;
	}
	
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String login(UserDTO param) {
		int result = service.login(param);
		
		if(result == 1) {
			return "redirect:/rest/map";	// redirect: 는 실제로 스프링 내부에 구현되어있음 
									// ( 쉽게 그냥 response.sendRedirect 로 생각하면 될듯 ) ViewResolver는 jsp로 가는거니까 X!!
		}
		
		return "redirect:/user/login?err=" + result;
	}
	
	
	
	@RequestMapping(value="/join", method = RequestMethod.GET)
	public String join(Model model, @RequestParam(required = false, defaultValue="0") int err) { // @RequestParam(value="err", required=false) Integer error 이런식으로 (참고로 Integer준이유는 NULL 값 까지 받기위해서)
															// post에서 get으로 보냈을시 쿼리스트링 값이 틀리면 위에주석방식대로 지정해줘야됨 
															// 참고로 쿼리스트링은 문자열이 날라오는데 int 매개변수 주면 굳이 Integer.parseInt 안해줘도됨
		if(err > 0) {
			model.addAttribute("msg","에러가 발생하였습니다");
		}
		
		model.addAttribute(Const.TITLE,"회원가입");
		model.addAttribute(Const.VIEW,"user/join");
		System.out.println("err값 : " + err);
		return ViewRef.TEMP_DEFAULT;
	}
	
	
	
	@RequestMapping(value="/join", method = RequestMethod.POST)
	public String join(UserVO param) { //join jsp에서 submit을 하면 스프링이 알아서 UserVO에 있는 이름값과 (셋터) 를 확인한후 바로 넣어줌
										// 이제 앞으로 getParameter 할필요없이 스피링이 지알아서 처리함  (값이 안들어갔다면 이름이 다르거나, 셋터메소드 없다는 뜻 )
		int result = service.join(param);
		
		if(result == 1) {
			return "redirect:/user/login";	// 즉, 이경우 login 이란 이름이 2개인데 그중에 get으로감 ( BoardWeb4 생각하면됨 / 같은맵핑이름 2개면 GET 메소드로감 )
		}
		
		return "redirect:/user/join?error=" + result;
	}
	
}
