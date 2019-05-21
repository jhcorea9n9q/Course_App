package com.example.web;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.example.domain.CourseUser;
import com.example.service.DBAccessServiceInterface;
import com.example.service.ErrorCheckServiceInterface;

/**ユーザ登録・ログイン担当コントローラー*/
@Controller
@SessionAttributes("userForm")
public class UserController implements UserControllerInterface {
	
	@Autowired
	private DBAccessServiceInterface DSI;
	
	@Autowired
	private ErrorCheckServiceInterface ESI;

	/**ユーザ用のフォーム*/
	@Override
	@ModelAttribute("userForm")
	public UserForm setForm() {
		return new UserForm();
	}

	/**ログイン画面*/
	@Override
	@RequestMapping("/login")
	public String login() {
		return "user/login";
	}

	/**ログインボタンを押した場合の処理*/
	@Override
	@RequestMapping(value = "/login/try", method = RequestMethod.POST)
	public String tryLogin(@Validated @ModelAttribute("userForm") UserForm form,
			BindingResult result) {
		CourseUser domain = new CourseUser();
		BeanUtils.copyProperties(form, domain);
		
		CourseUser userData = DSI.accountCheck(domain);
		
		result = ESI.loginCheck(userData, form, result);

		if(result.hasErrors()) {
			return "user/login";
		}
		
		// 権限の確認
		if(userData.getAuthority()==0) {
			return "admin/menu";
		}
		
		return "user/login"; // 実際は成功
	}

	/**ユーザ登録画面*/
	@Override
	@RequestMapping("/signin")
	public String signin() {
		return "user/signin";
	}

	/**ユーザ登録からログイン画面に戻るためにリダイレクト*/
	@Override
	@RequestMapping(value = "/signin/conf", params = "backBtn")
	public String signinBack(SessionStatus sess) {
		sess.setComplete();
		return "redirect:/signin/conf?back";
	}

	/**ユーザ登録からログイン画面に戻る*/
	@Override
	@RequestMapping(value = "/signin/conf", params = "back")
	public String backLogin() {
		return "user/login";
	}

	/**ユーザ登録確認ボタンを押した場合*/
	@Override
	@RequestMapping(value = "/signin/conf", params = "confBtn", 
									method = RequestMethod.POST)
	public String signinConf(@Validated @ModelAttribute("userForm") 
											UserForm form, BindingResult result) {
		CourseUser domain = new CourseUser();
		BeanUtils.copyProperties(form, domain);
		
		// エラーチェック
		result = ESI.userErrorCheck(domain, form, result);
		
		if(result.hasErrors()) {
			return "user/signin"; // エラーがあれば次の画面に行かない
		}
		
		return "user/signinConf";
	}

	/**確認画面からユーザ登録画面に戻る*/
	@Override
	@RequestMapping(value = "/signin/insert", params  = "backBtn", 
								method = RequestMethod.POST)
	public String backToSignin(@ModelAttribute("userForm") UserForm form) {
		return "user/signin";
	}

	/**入力確認ボタンを押して、データベースに入力*/
	@Override
	@RequestMapping(value = "/signin/insert", params  = "insertBtn", 
								method = RequestMethod.POST)
	public String userInsert(@ModelAttribute("userForm") UserForm form) {
		CourseUser domain = new CourseUser();
		BeanUtils.copyProperties(form, domain);
		DSI.insertUserData(domain); // データベースに入力
		return "user/signinEnd";
	}

	/**登録完了画面から講座登録画面へ*/
	@Override
	@RequestMapping(value = "/menu", params = "course", 
		method = RequestMethod.POST)
	public String endToCourseReg(@ModelAttribute("userForm") UserForm form) {
		return "admin/menu";
	}

	/**登録完了画面でログアウト後、ユーザセッションを画面からなくすためにリダイレクト*/
	@Override
	@RequestMapping(value = "/menu", params = "logout", 
			method = RequestMethod.POST)
	public String endLogout(SessionStatus sess) {
		sess.setComplete();
		return "redirect:/menu?end";
	}

	/**ログアウト完了*/
	@Override
	@RequestMapping(value = "/menu", params = "end")
	public String end() {
		return "user/login";
	}

}
