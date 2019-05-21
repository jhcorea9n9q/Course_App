package com.example.persistence;

import com.example.domain.CourseUser;

public interface UserMapper {
	
	public CourseUser accountCheck(CourseUser domain);
	
	public Integer selectOne(CourseUser domain);
	
	public void userInsert(CourseUser domain);

}
