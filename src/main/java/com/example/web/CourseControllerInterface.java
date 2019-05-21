package com.example.web;

import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.SessionStatus;

public interface CourseControllerInterface {
	
	public UserForm setUserForm();
	
	public SearchForm setSearchForm();
	
	public DUForm setDUForm();
	
	public String searchMenu();
	
	public String searchList(SearchForm form, BindingResult result, Model model);
	
	public String listSearch(SearchForm form);
	
	public String listDel(String btn, Model model);
	
	public String listUpd(String btn, Model model);
	
	public String delBack(SearchForm form, Model model);
	
	public String delEnd(DUForm form);
	
	public String delMenu(HttpSession session);
	
	public String delSearch(SearchForm form);
	
	public String delList(SearchForm form, Model model);
	
	public String delLogout(SessionStatus sess);
	
	public String delLogoutEnd();
	
	public String updBack(SearchForm form, Model model);

	public String updConf(DUForm form, BindingResult result);
	
	public String updConfBack(DUForm form);
	
	public String updEnd(DUForm form);
	
	public String updMenu(HttpSession session);
	
	public String updSearch(SearchForm form);
	
	public String updList(SearchForm form, Model model);
	
	public String updLogout(SessionStatus sess);
	
	public String updLogoutEnd();

}
