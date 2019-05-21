package com.example.web;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.SessionStatus;

public interface UserControllerInterface {
	
	public UserForm setForm();
	
	public String login();
	
	public String tryLogin(UserForm form, BindingResult result);
	
	public String signin();
	
	public String signinBack(SessionStatus sess);
	
	public String backLogin();
	
	public String signinConf(UserForm form, BindingResult result);
	
	public String backToSignin(UserForm form);
	
	public String userInsert(UserForm form);
	
	public String endToCourseReg(UserForm form);
	
	public String endLogout(SessionStatus sess);
	
	public String end();

}
