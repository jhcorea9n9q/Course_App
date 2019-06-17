package com.example.service;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.dbunit.Assertion;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.CourseRegApplication;
import com.example.domain.CourseReg;
import com.example.domain.CourseUser;
import com.example.web.SearchForm;

/**データベース用のサービスクラス*/
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CourseRegApplication.class)
public class DBAccessServiceTest {
	
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
	};
	
	public void dbSetting(String tfPath, String backUpTableName) throws Exception {
		final String testDataFilePath = tfPath;
		fIn = null;
		fOut = null;
		
		QueryDataSet OriginDataSet = new QueryDataSet(con);
		OriginDataSet.addTable(backUpTableName);
		
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
		
	}
	
	@Test
	void test1CourseCheck() throws Exception {
		dbSetting("src/test/java/com/example/service/courseTableTestData.xml", "course");
		
		CourseReg testDomain = new CourseReg();
		testDomain.setCourseNo("0001");
		
		boolean actual = TEST_UNIT.courseCheck(testDomain);
		assertThat(actual, is(true));
	}
	
	@Test
	void test2CourseCheck() throws Exception {
		dbSetting("src/test/java/com/example/service/courseTableTestData.xml", "course");
		
		CourseReg testDomain = new CourseReg();
		testDomain.setCourseNo("1000");
		
		boolean actual = TEST_UNIT.courseCheck(testDomain);
		assertThat(actual, is(false));
	}
	

	@Test
	void sqlErrorTest1InsertCourseReg() throws Exception {
		dbSetting("src/test/java/com/example/service/courseTableTestData.xml", "course");
		try {
			TEST_UNIT.insertCourseReg(new CourseReg());
			fail();
		} catch(Exception e) {
			assertThat(e instanceof DataIntegrityViolationException, is(true));
		}
	}
	
	@Test
	void sqlErrorTest2InsertCourseReg() throws Exception {
		dbSetting("src/test/java/com/example/service/courseTableTestData.xml", "course");
		try {
			CourseReg domain = createCourseRegDomain("00012", "不正講座", "2012-12-12", "12:12", "12:13", 12);
			TEST_UNIT.insertCourseReg(domain);
			fail();
		} catch(Exception e) {
			assertThat(e instanceof DataIntegrityViolationException, is(true));
		}
	}
	
	@Test
	void testInsertCourseReg() throws Exception {
		dbSetting("src/test/java/com/example/service/courseTableTestData.xml", "course");
	
		final String expectedDataFile = "src/test/java/com/example/service/courseTableInsertTestExpectedData.xml";
		CourseReg domain = createCourseRegDomain("0004", "JSP", "2020-02-05", "10:00", "15:00", 30);		
		fIn = null;
		
		try {
			TEST_UNIT.insertCourseReg(domain);
			
			fIn = new FileInputStream(expectedDataFile);
			
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			ITable actual = con.createDataSet().getTable("course");
			actual = DefaultColumnFilter.excludedColumnsTable(actual, new String[] {"upd_date"});
			
			Assertion.assertEquals(expected, actual);
				
		} catch(Exception e) {
			fail();
		} finally {
			if(fIn != null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void test1UserIdCheck() throws Exception {
		dbSetting("src/test/java/com/example/service/courseUserTableTestData.xml", "course_user");
		
		CourseUser testDomain = new CourseUser();
		testDomain.setUserId("user1");
		
		boolean actual = TEST_UNIT.userIdCheck(testDomain);
		assertThat(actual, is(true));
	}
	
	@Test
	void test2UserIdCheck() throws Exception {
		dbSetting("src/test/java/com/example/service/courseUserTableTestData.xml", "course_user");
		
		CourseUser testDomain = new CourseUser();
		testDomain.setUserId("user2");
		
		boolean actual = TEST_UNIT.userIdCheck(testDomain);
		assertThat(actual, is(false));
	}
	
	@Test
	void sqlErrorTest1InsertUserData() throws Exception {
		dbSetting("src/test/java/com/example/service/courseUserTableTestData.xml", "course_user");
		try {
			TEST_UNIT.insertUserData(new CourseUser());
			fail();
		} catch(Exception e) {
			assertThat(e instanceof DataIntegrityViolationException, is(true));
		}
	}
	
	@Test
	void sqlErrorTest2InsertUserData() throws Exception {
		dbSetting("src/test/java/com/example/service/courseUserTableTestData.xml", "course_user");
		try {
			CourseUser domain = createCourseUserDomain("tooLongId", "5555");
			TEST_UNIT.insertUserData(domain);
			fail();
		} catch(Exception e) {
			assertThat(e instanceof DataIntegrityViolationException, is(true));
		}
	}
	
	@Test
	void testInsertUserData() throws Exception {
		dbSetting("src/test/java/com/example/service/courseUserTableTestData.xml", "course_user");
	
		final String expectedDataFile = "src/test/java/com/example/service/courseUserTableInsertTestExpectedData.xml";
		CourseUser domain = createCourseUserDomain("insert", "insert");
		fIn = null;
		
		try {
			TEST_UNIT.insertUserData(domain);
			
			fIn = new FileInputStream(expectedDataFile);
			
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course_user");
			
			ITable actual = con.createDataSet().getTable("course_user");
			actual = DefaultColumnFilter.excludedColumnsTable(actual, new String[] {"id","upd_date"});
			
			Assertion.assertEquals(expected, actual);
				
		} catch(Exception e) {
			fail();
		} finally {
			if(fIn != null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void test1AccountCheck() throws Exception {
		dbSetting("src/test/java/com/example/service/courseUserTableTestData.xml", "course_user");
		
		CourseUser domain = createCourseUserDomain("user1", "2222");
		
		CourseUser actual = TEST_UNIT.accountCheck(domain);
		assertThat(actual, is(nullValue()));
	}
	
	@Test
	void test2AccountCheck() throws Exception {
		dbSetting("src/test/java/com/example/service/courseUserTableTestData.xml", "course_user");
		
		final String expectedDataFile = "src/test/java/com/example/service/courseUserTableLoginTestExpectedData.xml";
		CourseUser domain = createCourseUserDomain("user1", "1111");
		fIn = null;
		
		try {
			CourseUser actual = TEST_UNIT.accountCheck(domain);
			
			fIn = new FileInputStream(expectedDataFile);
			
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course_user");
			
			assertThat(actual.getUserId(), is(expected.getValue(0, "user_id")));
			assertThat(actual.getPasswd(), is(expected.getValue(0, "passwd")));
				
		} catch(Exception e) {
			fail();
		} finally {
			if(fIn != null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void testCourseFind() throws Exception{
		dbSetting("src/test/java/com/example/service/courseTableTestData.xml","course");
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableFindTestExpectedData.xml";
		CourseReg domain = new CourseReg();
		domain.setCourseNo("0001");
		fIn = null;
		
		try {
			CourseReg actual = TEST_UNIT.courseFind(domain);
			
			fIn = new FileInputStream(expectedDataFile);
			
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			assertThat(actual.getCourseNo(), is(expected.getValue(0, "course_no")));
			assertThat(actual.getCourseName(), is(expected.getValue(0, "course_name")));
			assertThat(actual.getTheDate(), is(expected.getValue(0, "the_date")));
			assertThat(actual.getStartTime(), is(expected.getValue(0, "start_time")));
			assertThat(actual.getEndTime(), is(expected.getValue(0, "end_time")));
			assertThat(actual.getCapacity(), is( Integer.parseInt(expected.getValue(0, "capacity").toString()) ));
				
		} catch(Exception e) {
			fail();
		} finally {
			if(fIn != null) {
				fIn.close();
			}
		}	
	}
	 
	@Test
	void testCourseDel() throws Exception{
		dbSetting("src/test/java/com/example/service/courseTableTestData.xml","course");
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableDeleteTestExpectedData.xml";
		CourseReg domain = new CourseReg();
		domain.setCourseNo("0002");
		
		fIn = null;
		try {
			TEST_UNIT.courseDel(domain);
			
			fIn = new FileInputStream(expectedDataFile);
			
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			ITable actual = con.createDataSet().getTable("course");
			actual = DefaultColumnFilter.excludedColumnsTable(actual, new String[] {"upd_date"});
			
			Assertion.assertEquals(expected, actual);
				
		} catch(Exception e) {
			fail();
		} finally {
			if(fIn != null) {
				fIn.close();
			}
		}
	}
	
	@Test
	void testCourseUdp() throws Exception{
		dbSetting("src/test/java/com/example/service/courseTableTestData.xml","course");
		
		final String expectedDataFile = "src/test/java/com/example/service/courseTableUpdateTestExpectedData.xml";
		CourseReg domain = createCourseRegDomain("0002", "新JAVA基礎", "2019-07-07", "12:00", "15:30", 45);	
		
		fIn = null;
		try {
			TEST_UNIT.courseUdp(domain);
			
			fIn = new FileInputStream(expectedDataFile);
			
			IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(fIn);
			ITable expected = expectedDataSet.getTable("course");
			
			ITable actual = con.createDataSet().getTable("course");
			actual = DefaultColumnFilter.excludedColumnsTable(actual, new String[] {"upd_date"});
			
			Assertion.assertEquals(expected, actual);
				
		} catch(Exception e) {
			fail();
		} finally {
			if(fIn != null) {
				fIn.close();
			}
		}
	}
	
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
	
	public CourseUser createCourseUserDomain(String userId, String passwd) {
		CourseUser domain = new CourseUser();
		domain.setUserId(userId);
		domain.setPasswd(passwd);
		return domain;
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
