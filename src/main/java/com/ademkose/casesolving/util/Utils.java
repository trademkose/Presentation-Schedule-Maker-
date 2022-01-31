package com.ademkose.casesolving.util;

import java.util.ArrayList;

import com.ademkose.casesolving.model.Presentation;

public class Utils {

	public static final int MAX_MINUTE = 240;
	public static final int BEFORE_LUNCH_MINUTE = 180;
	public static final int LUNCH_MINUTE = 60;
	public static final int MINUMUM_PRESENTATION_MINUTE = 5;
	public static final int MINUMUM_PRESENTATION_NAME_LENTGH = 5;
	public static final int START_TIME_AS_MINUTE = 540; // 540/60 = 09:00AM
	

	public static final String LUNCH_TIME = "12:00PM";
	public static final String LUNCH_NAME = "******   LUNCH   ******";
	public static final String EOD_NAME = "---------- END OF THE DAY ----------";
	public static final String NETWORKING_NAME = "****** Networking";
	
	public static String minuteToTimeAM_PM(int minute, String pm_or_am) {
		String time_press;
		int hours = ((int) (minute / 60));
		int min = (minute % 60);
		if (hours < 10) {
			time_press = "0" + hours;
		} else {
			time_press = Integer.toString(hours);
		}
		
		time_press = time_press + ":";

		if (min < 10) {
			time_press = time_press + "0" + min;
		} else {
			time_press = time_press +  Integer.toString(min);

		}

		return time_press+pm_or_am;

	}
	
	public static ArrayList<Presentation> setTime(ArrayList<Presentation> tracks_display_list) {
		int start_09_00 = Utils.START_TIME_AS_MINUTE;
		String pm_or_am = "AM";
		for (int i = 0; i < tracks_display_list.size(); i++) {
			Presentation temp_pres = tracks_display_list.get(i);
			if (temp_pres.getId() == 0) {
				pm_or_am = pm_or_am.equals("PM") ? "AM" : "PM";

			} else if (temp_pres.getId() == -1) //reset the day. set 09:00AM
			{
				start_09_00 = Utils.START_TIME_AS_MINUTE;
				continue;
			}
			if (temp_pres.getTime()==null)
			{
				temp_pres.setTime(Utils.minuteToTimeAM_PM(start_09_00, pm_or_am));
			}
			tracks_display_list.set(i, temp_pres);
			start_09_00 = start_09_00 + temp_pres.getMinute();
			if (temp_pres.getId() == 0) {
				start_09_00 = start_09_00 - (12 * 60);
			}
		}

		return tracks_display_list;
	}


}