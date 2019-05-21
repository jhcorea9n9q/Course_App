package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.CourseReg;
import com.example.domain.CourseUser;
import com.example.persistence.CourseRegMapper;
import com.example.persistence.CourseSearchMapper;
import com.example.persistence.UserMapper;
import com.example.web.SearchForm;

/**データベース用のサービスクラス*/
@Service
public class DBAccessService implements DBAccessServiceInterface {
	
	@Autowired
	private CourseRegMapper courseRegMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private CourseSearchMapper courseSearchMapper;

	/**講座登録重複チェックメソッド*/
	@Override
	public boolean courseCheck(CourseReg courseReg) {
		if(courseRegMapper.selectOne(courseReg) > 0) { // 重複があった場合、trueになる
			return true;
		}
		return false;
	}

	/**講座データをデータベースへ入力するメソッド*/
	@Override
	@Transactional
	public void insertCourseReg(CourseReg courseReg) {
		courseRegMapper.insert(courseReg);
	}

	/**ユーザID重複チェックメソッド*/
	@Override
	public boolean userIdCheck(CourseUser domain) {
		if(userMapper.selectOne(domain) > 0) {
			return true;
		}
		return false;
	}

	/**ユーザデータをデータベースへ入力するメソッド*/
	@Override
	@Transactional
	public void insertUserData(CourseUser domain) {
		userMapper.userInsert(domain);
	}
	
	/**ログイン用メソッド*/
	@Override
	public CourseUser accountCheck(CourseUser domain) {
		return userMapper.accountCheck(domain);
	}

	/**講座検索メソッド*/
	@Override
	public List<CourseReg> selectCourseList(SearchForm form) {
		if(form.getMonth().length()==1) {
			form.setMonth("0"+form.getMonth());
		}
		if(form.getDay().length()==1) {
			form.setDay("0"+form.getDay());
		}
		return courseSearchMapper.selectList(form);
	}

	/**講座削除・修正画面を作るためのメソッド*/
	@Override
	public CourseReg courseFind(CourseReg domain) {
		return courseSearchMapper.selectOne(domain);
	}

	/**講座削除メソッド*/
	@Override
	@Transactional
	public void courseDel(CourseReg courseReg) {
		courseSearchMapper.courseDel(courseReg);
	}

	/**講座修正メソッド*/
	@Override
	public void courseUdp(CourseReg courseReg) {
		courseSearchMapper.courseUdp(courseReg);
	}
	
}
