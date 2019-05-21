package com.example.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.example.domain.CourseReg;
import com.example.service.DBAccessServiceInterface;
import com.example.service.ErrorCheckServiceInterface;
import com.example.service.MakeModelServiceInterface;

/**講座管理コントローラー*/
@Controller
@SessionAttributes({"userForm", "searchForm", "duForm"})
public class CourseController implements CourseControllerInterface {
	
	@Autowired
	private DBAccessServiceInterface DSI; // データベースやり取り用のサービスクラス
	
	@Autowired
	private ErrorCheckServiceInterface ESI; // エラーチェック用のサービスクラス
	
	@Autowired
	private MakeModelServiceInterface MSI; // フォーム完成用のサービスクラス
	
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
	
	/**講座削除修正用のフォーム*/
	@Override
	@ModelAttribute("duForm")
	public DUForm setDUForm() {
		return new DUForm();
	}
	
	/**講座検索画面から講座管理メニューへ*/
	@Override
	@RequestMapping(value = "/course/list", params  = "menuBtn")
	public String searchMenu() {
		return "admin/menu";
	}

	/**講座検索の結果画面*/
	@Override
	@RequestMapping(value = "/course/list", params  = "searchBtn", method = RequestMethod.POST)
	public String searchList(@Validated @ModelAttribute("searchForm") SearchForm form, 
			BindingResult result, Model model) {
		
		if(result.hasErrors()) {
			return "admin/course/search";
		}
		
		List<CourseReg> courseList = DSI.selectCourseList(form); // 画面で使用するためのリスト
		List<String> stateList = new ArrayList<>();
		List<String> dateTimeList = new ArrayList<>();
		
		for(CourseReg cr : courseList) {
			
			// 画面に合わせて情報を作る
			stateList = MSI.makeSearchModel1(cr, stateList);
			dateTimeList = MSI.makeSearchModel2(cr, dateTimeList);
		}
		
		model.addAttribute("courseList",courseList);
		model.addAttribute("state",stateList);
		model.addAttribute("dateTime",dateTimeList);
		
		return "admin/course/list";
	}

	/**検索結果画面から検索画面へ*/
	@Override
	@RequestMapping(value = "/course/list/back", params  = "searchBtn", method = RequestMethod.POST)
	public String listSearch(@ModelAttribute("searchForm") SearchForm form) {
		return "admin/course/search";
	}

	/**講座削除画面*/
	@Override
	@RequestMapping(value = "/course/list/upd-or-del", params = "delBtn", method = RequestMethod.POST)
	public String listDel(@RequestParam("delBtn") String btn, Model model) {
		
		CourseReg domain = new CourseReg();
		DUForm duForm = new DUForm();
		
		domain.setCourseNo(btn);
		domain = DSI.courseFind(domain);
		
		duForm = MSI.makeDUPage(domain, duForm);
		model.addAttribute("duForm", duForm);

		return "admin/course/del";
	}

	/**講座修正画面*/
	@Override
	@RequestMapping(value = "/course/list/upd-or-del", params = "updBtn", method = RequestMethod.POST)
	public String listUpd(@RequestParam("updBtn") String btn,  Model model) {
		
		CourseReg domain = new CourseReg();
		DUForm duForm = new DUForm();
		
		domain.setCourseNo(btn);
		domain = DSI.courseFind(domain);
		
		duForm = MSI.makeDUPage(domain, duForm);
		model.addAttribute("duForm", duForm);
		
		return "admin/course/upd";
	}

	/**削除画面から講座リストに*/
	@Override
	@RequestMapping(value = "/course/del", params = "listBtn", method = RequestMethod.POST)
	public String delBack(@SessionAttribute("searchForm") SearchForm form, Model model) {
		
		List<CourseReg> courseList = DSI.selectCourseList(form); // 画面で使用するためのリスト
		List<String> stateList = new ArrayList<>();
		List<String> dateTimeList = new ArrayList<>();
		
		for(CourseReg cr : courseList) {
			
			// 画面に合わせて情報を作る
			stateList = MSI.makeSearchModel1(cr, stateList);
			dateTimeList = MSI.makeSearchModel2(cr, dateTimeList);
		}
		
		model.addAttribute("courseList",courseList);
		model.addAttribute("state",stateList);
		model.addAttribute("dateTime",dateTimeList);
		
		return "admin/course/list";
	}

	/**削除終了画面*/
	@Override
	@RequestMapping(value = "/course/del", params = "endBtn", method = RequestMethod.POST)
	public String delEnd(@ModelAttribute("duForm") DUForm form) {
		CourseReg courseReg = new CourseReg();
		BeanUtils.copyProperties(form, courseReg);
		DSI.courseDel(courseReg);
		return "admin/course/delEnd";
	}

	/**削除終了画面からメニューへ*/
	@Override
	@RequestMapping(value = "/course/del/end", params = "menuBtn")
	public String delMenu(HttpSession session) {
		session.removeAttribute("searchForm");
		return "admin/menu";
	}

	/**削除終了画面から講座検索画面へ*/
	@Override
	@RequestMapping(value = "/course/del/end", params = "searchBtn", method = RequestMethod.POST)
	public String delSearch(@SessionAttribute("searchForm") SearchForm form) {
		return "admin/course/search";
	}

	/**削除終了画面から講座リスト画面へ*/
	@Override
	@RequestMapping(value = "/course/del/end", params = "listBtn", method = RequestMethod.POST)
	public String delList(@SessionAttribute("searchForm") SearchForm form, Model model) {
		
		List<CourseReg> courseList = DSI.selectCourseList(form); // 画面で使用するためのリスト
		List<String> stateList = new ArrayList<>();
		List<String> dateTimeList = new ArrayList<>();
		
		for(CourseReg cr : courseList) {
			
			// 画面に合わせて情報を作る
			stateList = MSI.makeSearchModel1(cr, stateList);
			dateTimeList = MSI.makeSearchModel2(cr, dateTimeList);
		}
		
		model.addAttribute("courseList",courseList);
		model.addAttribute("state",stateList);
		model.addAttribute("dateTime",dateTimeList);
		
		return "admin/course/list";
	}

	/**削除終了画面でログアウト*/
	@Override
	@RequestMapping(value = "/course/del/end", params = "logoutBtn")
	public String delLogout(SessionStatus sess) {
		sess.setComplete();
		return "redirect:/course/del/end?logout";
	}

	/**削除終了画面でログアウトしてログイン画面へ*/
	@Override
	@RequestMapping(value = "/course/del/end", params = "logout")
	public String delLogoutEnd() {
		return "user/login";
	}

	/**講座修正画面から講座リストに*/
	@Override
	@RequestMapping(value = "/course/upd", params = "listBtn", method = RequestMethod.POST)
	public String updBack(@SessionAttribute("searchForm") SearchForm form, Model model) {
		
		List<CourseReg> courseList = DSI.selectCourseList(form); // 画面で使用するためのリスト
		List<String> stateList = new ArrayList<>();
		List<String> dateTimeList = new ArrayList<>();
		
		for(CourseReg cr : courseList) {
			
			// 画面に合わせて情報を作る
			stateList = MSI.makeSearchModel1(cr, stateList);
			dateTimeList = MSI.makeSearchModel2(cr, dateTimeList);
		}
		
		model.addAttribute("courseList",courseList);
		model.addAttribute("state",stateList);
		model.addAttribute("dateTime",dateTimeList);
		
		return "admin/course/list";
	}

	/**講座修正画面から修正確認画面に*/
	@Override
	@RequestMapping(value = "/course/upd", params = "confBtn", method = RequestMethod.POST)
	public String updConf(@Validated @ModelAttribute("duForm") DUForm form, 
			BindingResult result) {
		
		// 修正に合わせてフォーム完成
		form = MSI.makeForm(form);
		// エラーチェック
		result = ESI.courseUpdateCheck(form, result);
		
		if(result.hasErrors()) {
			return "admin/course/upd"; // エラーがあれば次の画面に行かない
		}
		return "admin/course/updConf";
	}

	/**修正確認画面から講座修正画面に*/
	@Override
	@RequestMapping(value = "/course/upd/conf", params = "backBtn", method = RequestMethod.POST)
	public String updConfBack(@ModelAttribute("duForm") DUForm form) {
		return "admin/course/upd";
	}

	/**修正確認画面から修正終了画面に*/
	@Override
	@RequestMapping(value = "/course/upd/conf", params = "endBtn", method = RequestMethod.POST)
	public String updEnd(@ModelAttribute("duForm") DUForm form) {
		CourseReg courseReg = new CourseReg();
		BeanUtils.copyProperties(form, courseReg);
		DSI.courseUdp(courseReg); // データベースを修正
		return "admin/course/updEnd";
	}

	/**修正終了画面からメニューへ*/
	@Override
	@RequestMapping(value = "/course/upd/end", params = "menuBtn")
	public String updMenu(HttpSession session) {
		session.removeAttribute("searchForm");
		return "admin/menu";
	}

	/**修正終了画面から講座検索画面へ*/
	@Override
	@RequestMapping(value = "/course/upd/end", params = "searchBtn", method = RequestMethod.POST)
	public String updSearch(@SessionAttribute("searchForm") SearchForm form) {
		return "admin/course/search";
	}

	/**修正終了画面から講座のリスト画面へ*/
	@Override
	@RequestMapping(value = "/course/upd/end", params = "listBtn", method = RequestMethod.POST)
	public String updList(@SessionAttribute("searchForm") SearchForm form, Model model) {
		
		List<CourseReg> courseList = DSI.selectCourseList(form); // 画面で使用するためのリスト
		List<String> stateList = new ArrayList<>();
		List<String> dateTimeList = new ArrayList<>();
		
		for(CourseReg cr : courseList) {
			
			// 画面に合わせて情報を作る
			stateList = MSI.makeSearchModel1(cr, stateList);
			dateTimeList = MSI.makeSearchModel2(cr, dateTimeList);
		}
		
		model.addAttribute("courseList",courseList);
		model.addAttribute("state",stateList);
		model.addAttribute("dateTime",dateTimeList);
		
		return "admin/course/list";
	}

	/**削除終了画面でログアウト*/
	@Override
	@RequestMapping(value = "/course/upd/end", params = "logoutBtn")
	public String updLogout(SessionStatus sess) {
		sess.setComplete();
		return "redirect:/course/upd/end?logout";
	}

	/**削除終了画面でログアウトしてログイン画面へ*/
	@Override
	@RequestMapping(value = "/course/upd/end", params = "logout")
	public String updLogoutEnd() {
		return "user/login";
	}

}
