package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.domain.CourseReg;
import com.example.domain.CourseUser;
import com.example.web.AdminForm;
import com.example.web.DUForm;
import com.example.web.SearchForm;
import com.example.web.UserForm;

/**エラーチェック用のサービスクラス*/
@Service
public class ErrorCheckService implements ErrorCheckServiceInterface {
	
	@Autowired
	private DBAccessServiceInterface DSI;

	/**講座登録のエラーチェックメソッド*/
	@Override
	public BindingResult resultCheck(CourseReg courseReg,
			@Validated @ModelAttribute("adminForm") AdminForm form,
			BindingResult result) {
		
		// 重複チェック
		if(DSI.courseCheck(courseReg)) {
			result.rejectValue("courseNo", "errors.same.courseNo");
		}
		
		// 講座開催日チェック
		if(form.getYear().equals("")) {
			result.rejectValue("year","errors.notEmpty.theDate");
		}else if(form.getMonth().equals("")) {
			result.rejectValue("month","errors.notEmpty.theDate");
		}else if(form.getDay().equals("")) {
			result.rejectValue("day","errors.notEmpty.theDate");
		}
		
		// 開始時刻チェック
		String start = "";
		if(form.getStartHour().equals("")) {
			result.rejectValue("startHour","errors.notEmpty.startTime");
		}else if(form.getStartMin().equals("")) {
			result.rejectValue("startMin","errors.notEmpty.startTime");
		}else {
			start = form.getStartHour() + form.getStartMin();
		}
		
		// 終了時刻チェック
		String end = "";
		if(form.getEndHour().equals("")) {
			result.rejectValue("endHour","errors.notEmpty.endTime");
		}else if(form.getEndMin().equals("")) {
			result.rejectValue("endMin","errors.notEmpty.endTime");
		}else {
			end = form.getEndHour() + form.getEndMin();
		}
		
		// 時間の矛盾チェック
		if(!start.equals("") && !end.equals("")) { // あらかじめ準備した2つの変数を整数型にパースして、比較する
			if (Integer.parseInt(start) >= Integer.parseInt(end)) {
				result.rejectValue("startHour", "errors.paradox.time");
			}
		}
		
		return result;
	}
	
	/**ログイン時のエラーチェックメソッド*/
	@Override
	public BindingResult loginCheck(CourseUser userData,
			@Validated @ModelAttribute("userForm") UserForm form, 
			BindingResult result) {

		// データベースにアカウントがなければエラー
		if(!form.getUserId().equals("") && !form.getPasswd().equals("")) {
			if(userData==null) {
				result.reject("errors.fail.login");
			}
		}

		return result;
	}

	/**ユーザ登録のエラーチェックメソッド*/
	@Override
	public BindingResult userErrorCheck(CourseUser domain, 
			@Validated @ModelAttribute("userForm") UserForm form, 
			BindingResult result) {
		
		// パスワード確認チェック
		if(!form.getPasswd().equals(form.getPasswdCheck())) {
			result.rejectValue("passwdCheck", "errors.fail.passwdCheck");
		}
		
		// 重複チェック
		if(DSI.userIdCheck(domain)) {
			result.rejectValue("userId", "errors.same.userId");
		}
		
		return result;
	}
	
	/**講座修正のエラーチェックメソッド*/
	@Override
	public BindingResult courseUpdateCheck(@Validated
			@ModelAttribute("duForm") DUForm form, BindingResult result) {
		
		// 講座開催日チェック
		if(form.getYear().equals("")) {
			result.rejectValue("year","errors.notEmpty.theDate");
		}else if(form.getMonth().equals("")) {
			result.rejectValue("month","errors.notEmpty.theDate");
		}else if(form.getDay().equals("")) {
			result.rejectValue("day","errors.notEmpty.theDate");
		}
		
		// 開始時刻チェック
		String start = "";
		if(form.getStartHour().equals("")) {
			result.rejectValue("startHour","errors.notEmpty.startTime");
		}else if(form.getStartMin().equals("")) {
			result.rejectValue("startMin","errors.notEmpty.startTime");
		}else {
			start = form.getStartHour() + form.getStartMin();
		}
		
		// 終了時刻チェック
		String end = "";
		if(form.getEndHour().equals("")) {
			result.rejectValue("endHour","errors.notEmpty.endTime");
		}else if(form.getEndMin().equals("")) {
			result.rejectValue("endMin","errors.notEmpty.endTime");
		}else {
			end = form.getEndHour() + form.getEndMin();
		}
		
		// 時間の矛盾チェック
		if(!start.equals("") && !end.equals("")) { // あらかじめ準備した2つの変数を整数型にパースして、比較する
			if (Integer.parseInt(start) >= Integer.parseInt(end)) {
				result.rejectValue("startHour", "errors.paradox.time");
			}
		}
		
		return result;
	}

}
