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
		
		StringBuilder stringBuilder = new StringBuilder();

		if(weeks > 0){
			stringBuilder.append(weeks);

			if(weeks == 1){
				stringBuilder.append(" Week ");
			} else {
				stringBuilder.append(" Weeks ");
			}
		}
		if(days > 0){
			stringBuilder.append(days);

			if(days == 1){
				stringBuilder.append(" Day ");
			} else {
				stringBuilder.append(" Days ");
			}
		}		
		if((hours > 0)){
			stringBuilder.append(hours);

			if(hours == 1){
				stringBuilder.append(" Hour ");
			} else {
				stringBuilder.append(" Hours ");
			}	
		}
		if(minutes > 0){
			stringBuilder.append(minutes);

			if(minutes == 1){
				stringBuilder.append(" Minute ");
			} else {
				stringBuilder.append(" Minutes ");
			}		
		}
		if(seconds > 0){
			stringBuilder.append(seconds);

			if(seconds == 1){
				stringBuilder.append(" Second ");
			} else {
				stringBuilder.append(" Seconds ");
			}		
		}
		return stringBuilder.toString();
	}
}
