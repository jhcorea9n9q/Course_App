package com.example.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.CourseRegApplication;
import com.example.domain.CourseReg;
import com.example.web.AdminForm;
import com.example.web.DUForm;
import com.example.web.UserForm;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CourseRegApplication.class)
public class MakeModelServiceTest {
	
	@Autowired
	private MakeModelService TEST_UNIT;
	
	@Test
	void test1MakeForm() {
		AdminForm form = new AdminForm();
		form = createAdminForm(
				"0001", "JAVA基礎講座01", 
				"2019", "06", "11", 
				"13", "00", 
				"15", "30", 
				25);
		
		AdminForm expected = new AdminForm();
		expected.setTheDate("2019-06-11");
		expected.setStartTime("13:00");
		expected.setEndTime("15:30");
		
		AdminForm actual = TEST_UNIT.makeForm(form);
		
		assertThat(actual.getTheDate(), is(expected.getTheDate()));
		assertThat(actual.getStartTime(), is(expected.getStartTime()));
		assertThat(actual.getEndTime(), is(expected.getEndTime()));
	}
	
	@Test
	void test2MakeForm() {
		DUForm form = new DUForm();
		form.setYear("2020");
		form.setMonth("12");
		form.setDay("12");
		form.setStartHour("12");
		form.setStartMin("12");
		form.setEndHour("18");
		form.setEndMin("15");
		
		DUForm expected = new DUForm();
		expected.setTheDate("2020-12-12");
		expected.setStartTime("12:12");
		expected.setEndTime("18:15");
		
		DUForm actual = TEST_UNIT.makeForm(form);
		
		assertThat(actual.getTheDate(), is(expected.getTheDate()));
		assertThat(actual.getStartTime(), is(expected.getStartTime()));
		assertThat(actual.getEndTime(), is(expected.getEndTime()));
	}
	
	@Test
	void test1MakeSearchModel1() {
		CourseReg domain = new CourseReg();
		List<String> actual = new ArrayList<String>();
		String expected = "終了";
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(cal.getTime());
	
		domain = createCourseRegDomain(
				"0001", "JAVA基礎講座01", date, "10:00", "18:00", 25);
		
		actual = TEST_UNIT.makeSearchModel1(domain, actual);
		
		assertThat(actual, hasItem(expected));
	}
	
	@Test
	void test2MakeSearchModel1() {
		CourseReg domain = new CourseReg();
		List<String> actual = new ArrayList<String>();
		String expected = "開催中";
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(cal.getTime());
		
		domain = createCourseRegDomain(
				"0002", "JAVA基礎講座02", date, "10:00", "18:00", 20);
		
		actual = TEST_UNIT.makeSearchModel1(domain, actual);
		
		assertThat(actual, hasItem(expected));
	}
	
	@Test
	void test3MakeSearchModel1() {
		CourseReg domain = new CourseReg();
		List<String> actual = new ArrayList<String>();
		String expected = "開催予定";
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(cal.getTime());
		
		domain = createCourseRegDomain(
				"0003", "JAVA基礎講座03", date, "10:00", "18:00", 32);
		
		actual = TEST_UNIT.makeSearchModel1(domain, actual);
		
		assertThat(actual, hasItem(expected));
	}
	
	@Test
	void testMakeSearchModel2() {
		CourseReg domain = new CourseReg();
		List<String> actual = new ArrayList<String>();
		String expected = "2019年6月15日 (土) 12:30-16:30";
		
		domain = createCourseRegDomain(
				"0004", "JAVA基礎講座04", 
				"2019-06-15", "12:30", "16:30", 22);
		
		actual = TEST_UNIT.makeSearchModel2(domain, actual);
		
		assertThat(actual, hasItem(expected));
	}
	
	@Test
	void test1MakeDUPage() {
		CourseReg domain = new CourseReg();
		DUForm form = new DUForm();
		
		domain = createCourseRegDomain(
				"0005", "JAVA基礎講座05", 
				"2019-10-15", "11:30", "16:00", 20);
		
		DUForm expected = new DUForm();
		expected.setYear("2019");
		expected.setMonth("10");
		expected.setDay("15");
		expected.setStartHour("11");
		expected.setStartMin("30");
		expected.setEndHour("16");
		expected.setEndMin("00");
		
		DUForm actual = TEST_UNIT.makeDUPage(domain, form);
		
		assertThat(actual.getYear(), is(expected.getYear()));
		assertThat(actual.getMonth(), is(expected.getMonth()));
		assertThat(actual.getDay(), is(expected.getDay()));
		assertThat(actual.getStartHour(), is(expected.getStartHour()));
		assertThat(actual.getStartMin(), is(expected.getStartMin()));
		assertThat(actual.getEndHour(), is(expected.getEndHour()));
		assertThat(actual.getEndMin(), is(expected.getEndMin()));
		
	}
	
	@Test
	void test2MakeDUPage() {
		CourseReg domain = new CourseReg();
		DUForm form = new DUForm();
		
		domain = createCourseRegDomain(
				"0006", "JAVA基礎講座06", 
				"2022-01-05", "11:09", "16:09", 20);
		
		DUForm expected = new DUForm();
		expected.setYear("2022");
		expected.setMonth("1");
		expected.setDay("5");
		expected.setStartHour("11");
		expected.setStartMin("09");
		expected.setEndHour("16");
		expected.setEndMin("09");
		
		DUForm actual = TEST_UNIT.makeDUPage(domain, form);
		
		assertThat(actual.getYear(), is(expected.getYear()));
		assertThat(actual.getMonth(), is(expected.getMonth()));
		assertThat(actual.getDay(), is(expected.getDay()));
		assertThat(actual.getStartHour(), is(expected.getStartHour()));
		assertThat(actual.getStartMin(), is(expected.getStartMin()));
		assertThat(actual.getEndHour(), is(expected.getEndHour()));
		assertThat(actual.getEndMin(), is(expected.getEndMin()));
	}
	
	public AdminForm createAdminForm(
			String courseNo, 
			String courseName, 
			String year,
			String month,
			String day,
			String startHour,
			String startMin,
			String endHour,
			String endMin,
			Integer capacity) {
		AdminForm form = new AdminForm();
		form.setCourseNo(courseNo);
		form.setCourseName(courseName);
		form.setYear(year);
		form.setMonth(month);
		form.setDay(day);
		form.setStartHour(startHour);
		form.setStartMin(startMin);
		form.setEndHour(endHour);
		form.setEndMin(endMin);
		form.setCapacity(capacity);
		return form;
	}
	
	public UserForm createUserForm(
			String userId, String passwd) {
		UserForm form = new UserForm();
		form.setUserId(userId);
		form.setPasswd(passwd);
		return form;
	}
	
	public CourseReg createCourseRegDomain(
			String courseNo, 
			String courseName,
			String theDate,
			String startTime,
			String endTime,
			Integer capacity) {
		CourseReg domain = new CourseReg();
		domain.setCourseNo(courseNo);
		domain.setCourseName(courseName);
		domain.setTheDate(theDate);
		domain.setStartTime(startTime);
		domain.setEndTime(endTime);
		domain.setCapacity(capacity);
		return domain;
	}
	
}
