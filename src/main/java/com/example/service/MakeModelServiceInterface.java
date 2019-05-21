package com.example.service;

import java.util.List;

import com.example.domain.CourseReg;
import com.example.web.AdminForm;
import com.example.web.DUForm;

public interface MakeModelServiceInterface {
	
	public AdminForm makeForm(AdminForm form);
	
	public DUForm makeForm(DUForm form);
	
	public List<String> makeSearchModel1(CourseReg cr, List<String> stateList);
	
	public List<String> makeSearchModel2(CourseReg cr, List<String> dateTimeList);
	
	public DUForm makeDUPage(CourseReg domain, DUForm form);

}
