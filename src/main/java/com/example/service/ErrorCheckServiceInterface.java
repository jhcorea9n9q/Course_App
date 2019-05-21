package com.example.service;

import org.springframework.validation.BindingResult;

import com.example.domain.CourseReg;
import com.example.domain.CourseUser;
import com.example.web.AdminForm;
import com.example.web.DUForm;
import com.example.web.SearchForm;
import com.example.web.UserForm;

public interface ErrorCheckServiceInterface {
	
	public BindingResult resultCheck(CourseReg courseReg, AdminForm form, BindingResult result);
	
	public BindingResult loginCheck(CourseUser userData, UserForm form, BindingResult result);
	
	public BindingResult userErrorCheck(CourseUser domain, UserForm form, BindingResult result);
	
	public BindingResult courseUpdateCheck(DUForm form, BindingResult result);
		
}
