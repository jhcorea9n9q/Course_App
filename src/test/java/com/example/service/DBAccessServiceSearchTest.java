package com.example.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.CourseRegApplication;
import com.example.domain.CourseReg;
import com.example.web.SearchForm;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CourseRegApplication.class)
public class DBAccessServiceSearchTest {
	
	@Autowired
	private DBAccessService TEST_UNIT;
	
	private Connection connection;
	private IDatabaseConnection con;
	private FileInputStream fIn;
	private FileOutputStream fOut;
	private File bkup;
	
	@BeforeAll
	public static void beforeTest() throws Exception {
		System.out.println("テスト開始。");
		Class.forName("org.mariadb.jdbc.Driver");
	}
	
	@BeforeEach
	void setting() throws Exception{
		connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/coursereg", "root", "");
		con = new DatabaseConnection(connection);
		
		final String testDataFilePath = "src/test/java/com/example/service/courseTableSearchTestData.xml";
		fIn = null;
		fOut = null;
		
		QueryDataSet OriginDataSet = new QueryDataSet(con);
		OriginDataSet.addTable("course");
		
		bkup = File.createTempFile("backup", ".xml");
		
		try {
			fOut = new FileOutputStream(bkup);
			FlatXmlDataSet.write(OriginDataSet, fOut);
			
			fIn = new FileInputStream(testDataFilePath);
			IDataSet dataSet = new FlatXmlDataSetBuilder().build(fIn);
			DatabaseOperation.CLEAN_INSERT.execute(con, dataSet);			
		} finally {
			if(fOut!=null) {
				fOut.close();
			}
			if(fIn!=null) {
				fIn.close();
			}
		}
	};
	
	@AfterEach
	void dbBack() throws Exception {
		fIn = null;
		try {
			fIn = new FileInputStream(bkup);
			
			IDataSet dataSet = new FlatXmlDataSetBuilder().build(fIn);
			DatabaseOperation.CLEAN_INSERT.execute(con, dataSet);
			
			bkup.deleteOnExit();
			
		} finally {
			if(fIn!=null) {
				fIn.close();
			}
			if(con!=null) {
				con.close();
			}
			if(connection!=null) {
				connection.close();
			}
		}
	}
	
	@AfterAll
	public static void AfterTest() {
		System.out.println("テスト終了。");
	}
	
	@Test
	void test1SelectCourseList() throws Exception {
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableSearchTestData.xml";
		SearchForm form = createNewSearchForm();
				
		fIn = null;
		try {
			List<CourseReg> actual = TEST_UNIT.selectCourseList(form);
			
			fIn = new FileInputStream(expectedDataFile);
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			for (int i = 0; i < expected.getRowCount(); i++) {
				assertThat(actual.get(i).getCourseNo(), is(expected.getValue(i, "course_no")));
				assertThat(actual.get(i).getCourseName(), is(expected.getValue(i, "course_name")));
				assertThat(actual.get(i).getTheDate(), is(expected.getValue(i, "the_date")));
				assertThat(actual.get(i).getStartTime(), is(expected.getValue(i, "start_time")));
				assertThat(actual.get(i).getEndTime(), is(expected.getValue(i, "end_time")));
				assertThat(actual.get(i).getCapacity(), is( Integer.parseInt(expected.getValue(i, "capacity").toString()) ));
			}
		} catch (Exception e) {
			fail();
		} finally {
			if(fIn!=null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void test2SelectCourseList() throws Exception {
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableSearchTestExpectedData1.xml";
		SearchForm form = createNewSearchForm();
		form.setCourseNo("1");
				
		fIn = null;
		try {
			List<CourseReg> actual = TEST_UNIT.selectCourseList(form);
			
			fIn = new FileInputStream(expectedDataFile);
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			for (int i = 0; i < expected.getRowCount(); i++) {
				assertThat(actual.get(i).getCourseNo(), is(expected.getValue(i, "course_no")));
				assertThat(actual.get(i).getCourseName(), is(expected.getValue(i, "course_name")));
				assertThat(actual.get(i).getTheDate(), is(expected.getValue(i, "the_date")));
				assertThat(actual.get(i).getStartTime(), is(expected.getValue(i, "start_time")));
				assertThat(actual.get(i).getEndTime(), is(expected.getValue(i, "end_time")));
				assertThat(actual.get(i).getCapacity(), is( Integer.parseInt(expected.getValue(i, "capacity").toString()) ));
			}
		} catch (Exception e) {
			fail();
		} finally {
			if(fIn!=null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void test3SelectCourseList() throws Exception {
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableSearchTestExpectedData2.xml";
		SearchForm form = createNewSearchForm();
		form.setCourseNo("0009");
				
		fIn = null;
		try {
			List<CourseReg> actual = TEST_UNIT.selectCourseList(form);
			
			fIn = new FileInputStream(expectedDataFile);
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			for (int i = 0; i < expected.getRowCount(); i++) {
				assertThat(actual.get(i).getCourseNo(), is(expected.getValue(i, "course_no")));
				assertThat(actual.get(i).getCourseName(), is(expected.getValue(i, "course_name")));
				assertThat(actual.get(i).getTheDate(), is(expected.getValue(i, "the_date")));
				assertThat(actual.get(i).getStartTime(), is(expected.getValue(i, "start_time")));
				assertThat(actual.get(i).getEndTime(), is(expected.getValue(i, "end_time")));
				assertThat(actual.get(i).getCapacity(), is( Integer.parseInt(expected.getValue(i, "capacity").toString()) ));
			}
		} catch (Exception e) {
			fail();
		} finally {
			if(fIn!=null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void test4SelectCourseList() throws Exception {
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableSearchTestExpectedData3.xml";
		SearchForm form = createNewSearchForm();
		form.setCourseName("JAVA");
				
		fIn = null;
		try {
			List<CourseReg> actual = TEST_UNIT.selectCourseList(form);
			
			fIn = new FileInputStream(expectedDataFile);
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			for (int i = 0; i < expected.getRowCount(); i++) {
				assertThat(actual.get(i).getCourseNo(), is(expected.getValue(i, "course_no")));
				assertThat(actual.get(i).getCourseName(), is(expected.getValue(i, "course_name")));
				assertThat(actual.get(i).getTheDate(), is(expected.getValue(i, "the_date")));
				assertThat(actual.get(i).getStartTime(), is(expected.getValue(i, "start_time")));
				assertThat(actual.get(i).getEndTime(), is(expected.getValue(i, "end_time")));
				assertThat(actual.get(i).getCapacity(), is( Integer.parseInt(expected.getValue(i, "capacity").toString()) ));
			}
		} catch (Exception e) {
			fail();
		} finally {
			if(fIn!=null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void test5SelectCourseList() throws Exception {
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableSearchTestExpectedData4.xml";
		SearchForm form = createNewSearchForm();
		form.setCourseName("PHP講座");
				
		fIn = null;
		try {
			List<CourseReg> actual = TEST_UNIT.selectCourseList(form);
			
			fIn = new FileInputStream(expectedDataFile);
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			for (int i = 0; i < expected.getRowCount(); i++) {
				assertThat(actual.get(i).getCourseNo(), is(expected.getValue(i, "course_no")));
				assertThat(actual.get(i).getCourseName(), is(expected.getValue(i, "course_name")));
				assertThat(actual.get(i).getTheDate(), is(expected.getValue(i, "the_date")));
				assertThat(actual.get(i).getStartTime(), is(expected.getValue(i, "start_time")));
				assertThat(actual.get(i).getEndTime(), is(expected.getValue(i, "end_time")));
				assertThat(actual.get(i).getCapacity(), is( Integer.parseInt(expected.getValue(i, "capacity").toString()) ));
			}
		} catch (Exception e) {
			fail();
		} finally {
			if(fIn!=null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void test6SelectCourseList() throws Exception {
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableSearchTestExpectedData5.xml";
		SearchForm form = createNewSearchForm();
		form.setYear("2019");
				
		fIn = null;
		try {
			List<CourseReg> actual = TEST_UNIT.selectCourseList(form);
			
			fIn = new FileInputStream(expectedDataFile);
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			for (int i = 0; i < expected.getRowCount(); i++) {
				assertThat(actual.get(i).getCourseNo(), is(expected.getValue(i, "course_no")));
				assertThat(actual.get(i).getCourseName(), is(expected.getValue(i, "course_name")));
				assertThat(actual.get(i).getTheDate(), is(expected.getValue(i, "the_date")));
				assertThat(actual.get(i).getStartTime(), is(expected.getValue(i, "start_time")));
				assertThat(actual.get(i).getEndTime(), is(expected.getValue(i, "end_time")));
				assertThat(actual.get(i).getCapacity(), is( Integer.parseInt(expected.getValue(i, "capacity").toString()) ));
			}
		} catch (Exception e) {
			fail();
		} finally {
			if(fIn!=null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void test7SelectCourseList() throws Exception {
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableSearchTestExpectedData6.xml";
		SearchForm form = createNewSearchForm();
		form.setMonth("7");
				
		fIn = null;
		try {
			List<CourseReg> actual = TEST_UNIT.selectCourseList(form);
			
			fIn = new FileInputStream(expectedDataFile);
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			for (int i = 0; i < expected.getRowCount(); i++) {
				assertThat(actual.get(i).getCourseNo(), is(expected.getValue(i, "course_no")));
				assertThat(actual.get(i).getCourseName(), is(expected.getValue(i, "course_name")));
				assertThat(actual.get(i).getTheDate(), is(expected.getValue(i, "the_date")));
				assertThat(actual.get(i).getStartTime(), is(expected.getValue(i, "start_time")));
				assertThat(actual.get(i).getEndTime(), is(expected.getValue(i, "end_time")));
				assertThat(actual.get(i).getCapacity(), is( Integer.parseInt(expected.getValue(i, "capacity").toString()) ));
			}
		} catch (Exception e) {
			fail();
		} finally {
			if(fIn!=null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void test8SelectCourseList() throws Exception {
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableSearchTestExpectedData7.xml";
		SearchForm form = createNewSearchForm();
		form.setDay("9");
				
		fIn = null;
		try {
			List<CourseReg> actual = TEST_UNIT.selectCourseList(form);
			
			fIn = new FileInputStream(expectedDataFile);
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			for (int i = 0; i < expected.getRowCount(); i++) {
				assertThat(actual.get(i).getCourseNo(), is(expected.getValue(i, "course_no")));
				assertThat(actual.get(i).getCourseName(), is(expected.getValue(i, "course_name")));
				assertThat(actual.get(i).getTheDate(), is(expected.getValue(i, "the_date")));
				assertThat(actual.get(i).getStartTime(), is(expected.getValue(i, "start_time")));
				assertThat(actual.get(i).getEndTime(), is(expected.getValue(i, "end_time")));
				assertThat(actual.get(i).getCapacity(), is( Integer.parseInt(expected.getValue(i, "capacity").toString()) ));
			}
		} catch (Exception e) {
			fail();
		} finally {
			if(fIn!=null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void test9SelectCourseList() throws Exception {
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableSearchTestExpectedData8.xml";
		SearchForm form = createNewSearchForm();
		form.setStartHour("10");
				
		fIn = null;
		try {
			List<CourseReg> actual = TEST_UNIT.selectCourseList(form);
			
			fIn = new FileInputStream(expectedDataFile);
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			for (int i = 0; i < expected.getRowCount(); i++) {
				assertThat(actual.get(i).getCourseNo(), is(expected.getValue(i, "course_no")));
				assertThat(actual.get(i).getCourseName(), is(expected.getValue(i, "course_name")));
				assertThat(actual.get(i).getTheDate(), is(expected.getValue(i, "the_date")));
				assertThat(actual.get(i).getStartTime(), is(expected.getValue(i, "start_time")));
				assertThat(actual.get(i).getEndTime(), is(expected.getValue(i, "end_time")));
				assertThat(actual.get(i).getCapacity(), is( Integer.parseInt(expected.getValue(i, "capacity").toString()) ));
			}
		} catch (Exception e) {
			fail();
		} finally {
			if(fIn!=null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void test10SelectCourseList() throws Exception {
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableSearchTestExpectedData9.xml";
		SearchForm form = createNewSearchForm();
		form.setStartMin("30");
				
		fIn = null;
		try {
			List<CourseReg> actual = TEST_UNIT.selectCourseList(form);
			
			fIn = new FileInputStream(expectedDataFile);
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			for (int i = 0; i < expected.getRowCount(); i++) {
				assertThat(actual.get(i).getCourseNo(), is(expected.getValue(i, "course_no")));
				assertThat(actual.get(i).getCourseName(), is(expected.getValue(i, "course_name")));
				assertThat(actual.get(i).getTheDate(), is(expected.getValue(i, "the_date")));
				assertThat(actual.get(i).getStartTime(), is(expected.getValue(i, "start_time")));
				assertThat(actual.get(i).getEndTime(), is(expected.getValue(i, "end_time")));
				assertThat(actual.get(i).getCapacity(), is( Integer.parseInt(expected.getValue(i, "capacity").toString()) ));
			}
		} catch (Exception e) {
			fail();
		} finally {
			if(fIn!=null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void test11SelectCourseList() throws Exception {
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableSearchTestExpectedData10.xml";
		SearchForm form = createNewSearchForm();
		form.setEndHour("18");
				
		fIn = null;
		try {
			List<CourseReg> actual = TEST_UNIT.selectCourseList(form);
			
			fIn = new FileInputStream(expectedDataFile);
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			for (int i = 0; i < expected.getRowCount(); i++) {
				assertThat(actual.get(i).getCourseNo(), is(expected.getValue(i, "course_no")));
				assertThat(actual.get(i).getCourseName(), is(expected.getValue(i, "course_name")));
				assertThat(actual.get(i).getTheDate(), is(expected.getValue(i, "the_date")));
				assertThat(actual.get(i).getStartTime(), is(expected.getValue(i, "start_time")));
				assertThat(actual.get(i).getEndTime(), is(expected.getValue(i, "end_time")));
				assertThat(actual.get(i).getCapacity(), is( Integer.parseInt(expected.getValue(i, "capacity").toString()) ));
			}
		} catch (Exception e) {
			fail();
		} finally {
			if(fIn!=null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void test12SelectCourseList() throws Exception {
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableSearchTestExpectedData11.xml";
		SearchForm form = createNewSearchForm();
		form.setEndMin("30");
				
		fIn = null;
		try {
			List<CourseReg> actual = TEST_UNIT.selectCourseList(form);
			
			fIn = new FileInputStream(expectedDataFile);
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			for (int i = 0; i < expected.getRowCount(); i++) {
				assertThat(actual.get(i).getCourseNo(), is(expected.getValue(i, "course_no")));
				assertThat(actual.get(i).getCourseName(), is(expected.getValue(i, "course_name")));
				assertThat(actual.get(i).getTheDate(), is(expected.getValue(i, "the_date")));
				assertThat(actual.get(i).getStartTime(), is(expected.getValue(i, "start_time")));
				assertThat(actual.get(i).getEndTime(), is(expected.getValue(i, "end_time")));
				assertThat(actual.get(i).getCapacity(), is( Integer.parseInt(expected.getValue(i, "capacity").toString()) ));
			}
		} catch (Exception e) {
			fail();
		} finally {
			if(fIn!=null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void test13SelectCourseList() throws Exception {
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableSearchTestExpectedData12.xml";
		SearchForm form = createNewSearchForm();
		form.setCapacityMin(30);
				
		fIn = null;
		try {
			List<CourseReg> actual = TEST_UNIT.selectCourseList(form);
			
			fIn = new FileInputStream(expectedDataFile);
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			for (int i = 0; i < expected.getRowCount(); i++) {
				assertThat(actual.get(i).getCourseNo(), is(expected.getValue(i, "course_no")));
				assertThat(actual.get(i).getCourseName(), is(expected.getValue(i, "course_name")));
				assertThat(actual.get(i).getTheDate(), is(expected.getValue(i, "the_date")));
				assertThat(actual.get(i).getStartTime(), is(expected.getValue(i, "start_time")));
				assertThat(actual.get(i).getEndTime(), is(expected.getValue(i, "end_time")));
				assertThat(actual.get(i).getCapacity(), is( Integer.parseInt(expected.getValue(i, "capacity").toString()) ));
			}
		} catch (Exception e) {
			fail();
		} finally {
			if(fIn!=null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void test14SelectCourseList() throws Exception {
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableSearchTestExpectedData13.xml";
		SearchForm form = createNewSearchForm();
		form.setCapacityMin(40);
				
		fIn = null;
		try {
			List<CourseReg> actual = TEST_UNIT.selectCourseList(form);
			
			fIn = new FileInputStream(expectedDataFile);
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			for (int i = 0; i < expected.getRowCount(); i++) {
				assertThat(actual.get(i).getCourseNo(), is(expected.getValue(i, "course_no")));
				assertThat(actual.get(i).getCourseName(), is(expected.getValue(i, "course_name")));
				assertThat(actual.get(i).getTheDate(), is(expected.getValue(i, "the_date")));
				assertThat(actual.get(i).getStartTime(), is(expected.getValue(i, "start_time")));
				assertThat(actual.get(i).getEndTime(), is(expected.getValue(i, "end_time")));
				assertThat(actual.get(i).getCapacity(), is( Integer.parseInt(expected.getValue(i, "capacity").toString()) ));
			}
		} catch (Exception e) {
			fail();
		} finally {
			if(fIn!=null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void test15SelectCourseList() throws Exception {
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableSearchTestExpectedData14.xml";
		SearchForm form = createNewSearchForm();
		form.setCapacityMax(26);
				
		fIn = null;
		try {
			List<CourseReg> actual = TEST_UNIT.selectCourseList(form);
			
			fIn = new FileInputStream(expectedDataFile);
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			for (int i = 0; i < expected.getRowCount(); i++) {
				assertThat(actual.get(i).getCourseNo(), is(expected.getValue(i, "course_no")));
				assertThat(actual.get(i).getCourseName(), is(expected.getValue(i, "course_name")));
				assertThat(actual.get(i).getTheDate(), is(expected.getValue(i, "the_date")));
				assertThat(actual.get(i).getStartTime(), is(expected.getValue(i, "start_time")));
				assertThat(actual.get(i).getEndTime(), is(expected.getValue(i, "end_time")));
				assertThat(actual.get(i).getCapacity(), is( Integer.parseInt(expected.getValue(i, "capacity").toString()) ));
			}
		} catch (Exception e) {
			fail();
		} finally {
			if(fIn!=null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void test16SelectCourseList() throws Exception {
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableSearchTestExpectedData15.xml";
		SearchForm form = createNewSearchForm();
		form.setCapacityMax(20);
				
		fIn = null;
		try {
			List<CourseReg> actual = TEST_UNIT.selectCourseList(form);
			
			fIn = new FileInputStream(expectedDataFile);
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			for (int i = 0; i < expected.getRowCount(); i++) {
				assertThat(actual.get(i).getCourseNo(), is(expected.getValue(i, "course_no")));
				assertThat(actual.get(i).getCourseName(), is(expected.getValue(i, "course_name")));
				assertThat(actual.get(i).getTheDate(), is(expected.getValue(i, "the_date")));
				assertThat(actual.get(i).getStartTime(), is(expected.getValue(i, "start_time")));
				assertThat(actual.get(i).getEndTime(), is(expected.getValue(i, "end_time")));
				assertThat(actual.get(i).getCapacity(), is( Integer.parseInt(expected.getValue(i, "capacity").toString()) ));
			}
		} catch (Exception e) {
			fail();
		} finally {
			if(fIn!=null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void test17SelectCourseList() throws Exception {
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableSearchTestExpectedData16.xml";
		SearchForm form = createNewSearchForm();
		form.setState("終了");
				
		fIn = null;
		try {
			List<CourseReg> actual = TEST_UNIT.selectCourseList(form);
			
			fIn = new FileInputStream(expectedDataFile);
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			for (int i = 0; i < expected.getRowCount(); i++) {
				assertThat(actual.get(i).getCourseNo(), is(expected.getValue(i, "course_no")));
				assertThat(actual.get(i).getCourseName(), is(expected.getValue(i, "course_name")));
				assertThat(actual.get(i).getTheDate(), is(expected.getValue(i, "the_date")));
				assertThat(actual.get(i).getStartTime(), is(expected.getValue(i, "start_time")));
				assertThat(actual.get(i).getEndTime(), is(expected.getValue(i, "end_time")));
				assertThat(actual.get(i).getCapacity(), is( Integer.parseInt(expected.getValue(i, "capacity").toString()) ));
			}
		} catch (Exception e) {
			fail();
		} finally {
			if(fIn!=null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void test18SelectCourseList() throws Exception {
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableSearchTestExpectedData17.xml";
		SearchForm form = createNewSearchForm();
		form.setState("開催中");
				
		fIn = null;
		try {
			List<CourseReg> actual = TEST_UNIT.selectCourseList(form);
			
			fIn = new FileInputStream(expectedDataFile);
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			for (int i = 0; i < expected.getRowCount(); i++) {
				assertThat(actual.get(i).getCourseNo(), is(expected.getValue(i, "course_no")));
				assertThat(actual.get(i).getCourseName(), is(expected.getValue(i, "course_name")));
				assertThat(actual.get(i).getTheDate(), is(expected.getValue(i, "the_date")));
				assertThat(actual.get(i).getStartTime(), is(expected.getValue(i, "start_time")));
				assertThat(actual.get(i).getEndTime(), is(expected.getValue(i, "end_time")));
				assertThat(actual.get(i).getCapacity(), is( Integer.parseInt(expected.getValue(i, "capacity").toString()) ));
			}
		} catch (Exception e) {
			fail();
		} finally {
			if(fIn!=null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void test19SelectCourseList() throws Exception {
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableSearchTestExpectedData18.xml";
		SearchForm form = createNewSearchForm();
		form.setState("開催予定");
				
		fIn = null;
		try {
			List<CourseReg> actual = TEST_UNIT.selectCourseList(form);
			
			fIn = new FileInputStream(expectedDataFile);
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			for (int i = 0; i < expected.getRowCount(); i++) {
				assertThat(actual.get(i).getCourseNo(), is(expected.getValue(i, "course_no")));
				assertThat(actual.get(i).getCourseName(), is(expected.getValue(i, "course_name")));
				assertThat(actual.get(i).getTheDate(), is(expected.getValue(i, "the_date")));
				assertThat(actual.get(i).getStartTime(), is(expected.getValue(i, "start_time")));
				assertThat(actual.get(i).getEndTime(), is(expected.getValue(i, "end_time")));
				assertThat(actual.get(i).getCapacity(), is( Integer.parseInt(expected.getValue(i, "capacity").toString()) ));
			}
		} catch (Exception e) {
			fail();
		} finally {
			if(fIn!=null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void test20SelectCourseList() throws Exception {
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableSearchTestData.xml";
		SearchForm form = createNewSearchForm();
		form.setState("条件無し");
				
		fIn = null;
		try {
			List<CourseReg> actual = TEST_UNIT.selectCourseList(form);
			
			fIn = new FileInputStream(expectedDataFile);
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			for (int i = 0; i < expected.getRowCount(); i++) {
				assertThat(actual.get(i).getCourseNo(), is(expected.getValue(i, "course_no")));
				assertThat(actual.get(i).getCourseName(), is(expected.getValue(i, "course_name")));
				assertThat(actual.get(i).getTheDate(), is(expected.getValue(i, "the_date")));
				assertThat(actual.get(i).getStartTime(), is(expected.getValue(i, "start_time")));
				assertThat(actual.get(i).getEndTime(), is(expected.getValue(i, "end_time")));
				assertThat(actual.get(i).getCapacity(), is( Integer.parseInt(expected.getValue(i, "capacity").toString()) ));
			}
		} catch (Exception e) {
			fail();
		} finally {
			if(fIn!=null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void test21SelectCourseList() throws Exception {
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableSearchTestExpectedData19.xml";
		SearchForm form = createSearchForm(
				"3", "js", 
				"2019", "6", "17", 
				"17", "00", 
				"18", "00", 
				15, 25, 
				"開催中");
				
		fIn = null;
		try {
			List<CourseReg> actual = TEST_UNIT.selectCourseList(form);
			
			fIn = new FileInputStream(expectedDataFile);
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			for (int i = 0; i < expected.getRowCount(); i++) {
				assertThat(actual.get(i).getCourseNo(), is(expected.getValue(i, "course_no")));
				assertThat(actual.get(i).getCourseName(), is(expected.getValue(i, "course_name")));
				assertThat(actual.get(i).getTheDate(), is(expected.getValue(i, "the_date")));
				assertThat(actual.get(i).getStartTime(), is(expected.getValue(i, "start_time")));
				assertThat(actual.get(i).getEndTime(), is(expected.getValue(i, "end_time")));
				assertThat(actual.get(i).getCapacity(), is( Integer.parseInt(expected.getValue(i, "capacity").toString()) ));
			}
		} catch (Exception e) {
			fail();
		} finally {
			if(fIn!=null) {
				fIn.close();
			}
		}
	}
	
	public SearchForm createNewSearchForm() {
		SearchForm form = new SearchForm();
		form.setCourseNo("");
		form.setCourseName("");
		form.setYear("");
		form.setMonth("");
		form.setDay("");
		form.setStartHour("");
		form.setStartMin("");
		form.setEndHour("");
		form.setEndMin("");
		return form;
	}
	
	public SearchForm createSearchForm(
			String courseNo, 
			String courseName,
			String year,
			String month,
			String day,
			String startHour,
			String startMin,
			String endHour,
			String endMin,
			Integer capacityMin,
			Integer capacityMax,
			String state) {
		SearchForm form = new SearchForm();
		form.setCourseNo(courseNo);
		form.setCourseName(courseName);
		form.setYear(year);
		form.setMonth(month);
		form.setDay(day);
		form.setStartHour(startHour);
		form.setStartMin(startMin);
		form.setEndHour(endHour);
		form.setEndMin(endMin);
		form.setCapacityMin(capacityMin);
		form.setCapacityMax(capacityMax);
		form.setState(state);
		
		return form;
	}

}
