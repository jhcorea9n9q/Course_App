package com.example.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.domain.CourseReg;
import com.example.web.AdminForm;
import com.example.web.DUForm;

/**画面上のFormやデータベースから持ってきたデータを、使用できるように完成するサービスクラス*/
@Service
public class MakeModelService implements MakeModelServiceInterface {

	/**講座登録formの中身を完成して返すメソッド*/
	@Override
	public AdminForm makeForm(@ModelAttribute("adminForm") AdminForm form) {
		form.setTheDate(form.getYear() + "-" + form.getMonth() + "-" + form.getDay()); // 年月日をyyyy-mm-ddの形式で格納
		form.setStartTime(form.getStartHour() + ":" + form.getStartMin()); // 時間をhh:MMの形式で格納
		form.setEndTime(form.getEndHour() + ":" + form.getEndMin()); // 時間をhh:MMの形式で格納
		return form;
	}
	
	/**講座修正formの中身を完成して返すメソッド*/
	@Override
	public DUForm makeForm(@ModelAttribute("duForm") DUForm form) {
		form.setTheDate(form.getYear() + "-" + form.getMonth() + "-" + form.getDay()); // 年月日をyyyy-mm-ddの形式で格納
		form.setStartTime(form.getStartHour() + ":" + form.getStartMin()); // 時間をhh:MMの形式で格納
		form.setEndTime(form.getEndHour() + ":" + form.getEndMin()); // 時間をhh:MMの形式で格納
		return form;
	}

	/**講座検索結果を表示するための中身を完成して返すメソッド１*/
	@Override
	public List<String> makeSearchModel1(CourseReg cr, List<String> stateList) {
		
		// Calendarを使って状態を決めるためにデータを分解
		String[] dates = cr.getTheDate().split("-");
		int year = Integer.parseInt(dates[0]);
		int month = Integer.parseInt(dates[1]);
		int day = Integer.parseInt(dates[2]);
		
		String[] startTime = cr.getStartTime().split(":");
		int startHour = Integer.parseInt(startTime[0]);
		int startMin = Integer.parseInt(startTime[1]);
		
		String[] endTime = cr.getEndTime().split(":");
		int endHour = Integer.parseInt(endTime[0]);
		int endMin = Integer.parseInt(endTime[1]);

		// 開始時刻
		Calendar startCal = Calendar.getInstance();
		startCal.set(year, month-1, day, startHour, startMin);
		
		// 終了時刻
		Calendar endCal = Calendar.getInstance();
		endCal.set(year, month-1, day, endHour, endMin);
		
		// 現在の日時
		Calendar nowCal = Calendar.getInstance();
		
		// 今日の日時と比べて、講座の状態を完成
		if(endCal.before(nowCal)) {				// 現在の日時が講座の終了時間より後の場合、「終了」と表記
			stateList.add("終了");
		}else if(startCal.after(nowCal)) {	// 現在の日時が講座の開始時間より前の場合、「開催予定」と表記
			stateList.add("開催予定");
		}else {														//	どちらでもない場合には「開催中」となる
			stateList.add("開催中");
		}
		
		return stateList;
	}
	
	/**講座検索結果を表示するための中身を完成して返すメソッド２*/
	@Override
	public List<String> makeSearchModel2(CourseReg cr, List<String> dateTimeList) {
		
		// Calendarを使って状態を決めるためにデータを分解
		String[] dates = cr.getTheDate().split("-");
		int year = Integer.parseInt(dates[0]);
		int month = Integer.parseInt(dates[1]);
		int day = Integer.parseInt(dates[2]);
		
		String[] startTime = cr.getStartTime().split(":");
		int startHour = Integer.parseInt(startTime[0]);
		int startMin = Integer.parseInt(startTime[1]);
		
		String[] endTime = cr.getEndTime().split(":");
		int endHour = Integer.parseInt(endTime[0]);
		int endMin = Integer.parseInt(endTime[1]);

		// 開始時刻
		Calendar startCal = Calendar.getInstance();
		startCal.set(year, month-1, day, startHour, startMin);
		
		// 終了時刻
		Calendar endCal = Calendar.getInstance();
		endCal.set(year, month-1, day, endHour, endMin);
		
		// 講座の曜日名
		int week = startCal.get(Calendar.DAY_OF_WEEK);
		String w = "";

		switch (week) {
        case Calendar.SUNDAY:
            w = "日";
            break;
        case Calendar.MONDAY:
            w = "月";
            break;
        case Calendar.TUESDAY:
            w = "火";
            break;
        case Calendar.WEDNESDAY:
           w = "水";
            break;
        case Calendar.THURSDAY:
            w = "木";
            break;
        case Calendar.FRIDAY:
            w = "金";
            break;
        case Calendar.SATURDAY:
            w = "土";
            break;
		}
		
		// 画面に表示するために日時情報を
		// YYYY年MM月DD日(week)開始時:開始分-終了時:終了分の形で完成
		dateTimeList.add(year+"年"+month+"月"+day+"日 ("+w+") "
				+ cr.getStartTime() + "-" + cr.getEndTime());
		
		return dateTimeList;
	}

	/**講座修正及び削除用のフォームを完成して返すメソッド*/
	@Override
	public DUForm makeDUPage(CourseReg domain, DUForm form) {
		
		form.setCourseNo(domain.getCourseNo());
		form.setCourseName(domain.getCourseName());
		
		String theDate = domain.getTheDate();
		form.setTheDate(theDate);
		
		String startTime = domain.getStartTime();
		form.setStartTime(startTime);
		
		String endTime = domain.getEndTime();
		form.setEndTime(endTime);
		
		form.setCapacity(domain.getCapacity());
		
		String[] theDates = theDate.split("-");
		form.setYear(theDates[0]);
		
		String m = theDates[1];
		if(m.startsWith("0")) {
			form.setMonth(m.substring(1));
		}else {
			form.setMonth(m);
		}
		
		String d =theDates[2];
		if(d.startsWith("0")) {
			form.setDay(d.substring(1));
		}else {
			form.setDay(d);
		}
		
		String[] startTimes = startTime.split(":");
		form.setStartHour(startTimes[0]);
		form.setStartMin(startTimes[1]);
		
		String[] endTimes = endTime.split(":");
		form.setEndHour(endTimes[0]);
		form.setEndMin(endTimes[1]);

		return form;
	}

}
