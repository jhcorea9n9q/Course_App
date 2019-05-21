package com.example.persistence;

import com.example.domain.CourseReg;

/**講座登録マッパー*/
public interface CourseRegMapper {

	public Integer selectOne(CourseReg courseReg);
	
	public void insert(CourseReg courseReg);
	
}
