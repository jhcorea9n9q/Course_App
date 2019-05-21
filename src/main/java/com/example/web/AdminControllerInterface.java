package com.example.web;

import javax.servlet.http.HttpSession;

import org.springframework.validation.BindingResult;

public interface AdminControllerInterface {
	
	public AdminForm setAdminForm();
	
	public UserForm setUserForm();
	
	public SearchForm setSearchForm();
	
	public String menu();
	
	public String goToCourseUdp(UserForm form);
	
	public String input();
	
	public String inputBack(HttpSession session);
	
	public String backMenu();
	
	public String conf(AdminForm form, BindingResult result);
	
	public String confBack(AdminForm form);
	
	public String insert(AdminForm form, HttpSession session);
	
	public String endMenu();
	
	public String endInput();
	
	
}
