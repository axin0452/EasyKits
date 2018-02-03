package com.gmail.trentech.easykits.utils;

public class Resource {

	public final static String NAME = "@project.name@";
	public final static String ID = "@project.id@";
	public final static String VERSION = "@project.version@";
	public final static String DESCRIPTION = "@project.description@";
	public final static String AUTHOR = "@project.author@";
	public final static String URL = "@project.url@";

	public static String getReadableTime(long timeInSec) {
		long weeks = timeInSec / 604800;
		long wRemainder = timeInSec % 604800;
		long days = wRemainder / 86400;
		long dRemainder = wRemainder % 86400;
		long hours = dRemainder / 3600;
		long hRemainder = dRemainder % 3600;
		long minutes = hRemainder / 60;
		long seconds = hRemainder % 60;
		String time = null;	
		if(weeks > 0){
			String wks = " Weeks";
			if(weeks == 1){
				wks = " Week";
			}
			time = weeks + wks;
		}
		if(days > 0 || (days == 0 && weeks > 0)){
			String dys = " Days";
			if(days == 1){
				dys = " Day";
			}
			if(time != null){
				time = time + ", " + days + dys;
			}else{
				time = days + dys;
			}
		}		
		if((hours > 0) || (hours == 0 && days > 0)){
			String hrs = " Hours";
			if(hours == 1){
				hrs = " Hour";
			}
			if(time != null){
				time = time + ", " + hours + hrs;
			}else{
				time = hours + hrs;
			}		
		}
		if((minutes > 0) || (minutes == 0 && days > 0) || (minutes == 0 && hours > 0)){
			String min = " Minutes";
			if(minutes == 1){
				min = " Minute";
			}
			if(time != null){
				time = time + ", " + minutes + min;	
			}else{
				time = minutes + min;
			}			
		}
		if(seconds > 0){
			String sec = " Seconds";
			if(seconds == 1){
				sec = " Second";
			}
			if(time != null){
				time = time + " and " + seconds + sec;
			}else{
				time = seconds + sec;
			}			
		}
		return time;
	}
}
