package com.example.service;

import java.util.List;

import com.example.domain.CourseReg;
import com.example.domain.CourseUser;
import com.example.web.SearchForm;

public interface DBAccessServiceInterface {
	
	public boolean courseCheck(CourseReg courseReg);
	
	public void insertCourseReg(CourseReg courseReg);
	
	public boolean userIdCheck(CourseUser domain);
	
	public void insertUserData(CourseUser domain);
	
	public CourseUser accountCheck(CourseUser domain);
	
	public List<CourseReg> selectCourseList(SearchForm form);
	
	public CourseReg courseFind(CourseReg domain);
	
	public void courseDel(CourseReg courseReg);
	
	public void courseUdp(CourseReg courseReg);

}
