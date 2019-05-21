package com.example.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.domain.CourseReg;
import com.example.service.DBAccessServiceInterface;
import com.example.service.ErrorCheckServiceInterface;
import com.example.service.MakeModelServiceInterface;

/**講座登録コントローラー*/
@Controller
@SessionAttributes({"adminForm", "userForm","searchForm"})
public class AdminController implements AdminControllerInterface {
	
	@Autowired
	private DBAccessServiceInterface DSI; // データベースやり取り用のサービスクラス
	
	@Autowired
	private ErrorCheckServiceInterface ESI; // エラーチェック用のサービスクラス
	
	@Autowired
	private MakeModelServiceInterface MSI; // フォーム完成用のサービスクラス
	
	/**講座登録用のフォーム*/
	@Override
	@ModelAttribute("adminForm")
	public AdminForm setAdminForm() {
		return new AdminForm();
	}
	
	/**ユーザ用のフォーム*/
	@Override
	@ModelAttribute("userForm")
	public UserForm setUserForm() {
		return new UserForm();
	}
	
	/**講座検索用のフォーム*/
	@Override
	@ModelAttribute("searchForm")
	public SearchForm setSearchForm() {
		return new SearchForm();
	}
	
	/**講座登録メニュー画面*/
	@Override
	@RequestMapping("/menu")
	public String menu() {
		return "admin/menu";
	}

	/**講座修正削除画面へ*/
	@Override
	@RequestMapping(value="/input", params  = "courseBtn")
	public String goToCourseUdp(@ModelAttribute("userForm") UserForm form) {
		return "admin/course/search";
	}

	/**講座登録の入力画面*/
	@Override
	@RequestMapping(value = "/input")
	public String input() {
		return "admin/input";
	}

	/**入力画面からメニューに戻るためにリダイレクト*/
	@Override
	@RequestMapping(value = "/conf", params  = "backBtn", method = RequestMethod.POST)
	public String inputBack(HttpSession session) {
		session.removeAttribute("adminForm");
		return "redirect:/conf?back";
	}

	/**入力画面からメニューに戻る*/
	@Override
	@RequestMapping(value = "/conf", params  = "back")
	public String backMenu() {
		return "admin/menu";
	}

	/**入力確認ボタンを押した場合*/
	@Override
	@RequestMapping(value = "/conf", params  = "confBtn", method = RequestMethod.POST)
	public String conf(@Validated @ModelAttribute("adminForm") AdminForm form,
			BindingResult result) {
		
		// フォーム完成
		form = MSI.makeForm(form);
		
		CourseReg courseReg = new CourseReg();
		BeanUtils.copyProperties(form, courseReg);
		
		// エラーチェック
		result = ESI.resultCheck(courseReg, form, result);
		
		if(result.hasErrors()) {
			return "admin/input"; // エラーがあれば次の画面に行かない
		}
		return "admin/conf";
	}

	/**確認画面から入力画面に戻る*/
	@Override
	@RequestMapping(value = "/insert", params  = "backBtn", method = RequestMethod.POST)
	public String confBack(@ModelAttribute("adminForm") AdminForm form) {
		return "admin/input";
	}

	/**登録ボタンを押した場合*/
	@Override
	@RequestMapping(value = "/insert", params  = "insertBtn", method = RequestMethod.POST)
	public String insert(@ModelAttribute("adminForm") AdminForm form, HttpSession session) {
		CourseReg courseReg = new CourseReg();
		BeanUtils.copyProperties(form, courseReg);
		DSI.insertCourseReg(courseReg); // データベースに入力
		session.removeAttribute("adminForm"); // セッション除去
		return "admin/end";
	}

	/**終了画面からメニュー画面に*/
	@Override
	@RequestMapping(value = "/end", params  = "menuBtn")
	public String endMenu() {
		return "admin/menu";
	}

	/**終了画面から入力画面に*/
	@Override
	@RequestMapping(value = "/end", params  = "inputBtn")
	public String endInput() {
		return "admin/input";
	}

}
