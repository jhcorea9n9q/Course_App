package com.example.persistence;

import java.util.List;

import com.example.domain.CourseReg;
import com.example.web.SearchForm;

public interface CourseSearchMapper {
	
	public List<CourseReg> selectList(SearchForm form);
	
	public CourseReg selectOne(CourseReg domain);
	
	public void courseDel(CourseReg courseReg);
	
	public void courseUdp(CourseReg courseReg);

}
