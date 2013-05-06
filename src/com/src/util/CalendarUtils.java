package com.src.util;

import java.util.Calendar;

/**
 * Provides some handy calendar utility methods.
 * @author Kyle
 */
public class CalendarUtils {
	/**
	 * The days in each month.
	 */
	private static final int[] DAYS_IN_MONTHS = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	
	/**
	 * Given a calendar, this method extracts its fields and produces a
	 * convenient time-stamp string.
	 * @param c A calendar.
	 * @return Formats the calendar's fields into a human-readable String, in
	 * the format MM/DD/YYYY-HH:MM:SS.
	 */
	public static String convertToString(Calendar c) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%02d", c.get(Calendar.MONTH) + 1));
		sb.append('/');
		sb.append(String.format("%02d", c.get(Calendar.DAY_OF_MONTH)));
		sb.append('/');
		sb.append(String.format("%04d", c.get(Calendar.YEAR)));
		sb.append('-');
		sb.append(String.format("%02d", c.get(Calendar.HOUR_OF_DAY)));
		sb.append(':');
		sb.append(String.format("%02d", c.get(Calendar.MINUTE)));
		sb.append(':');
		sb.append(String.format("%02d", c.get(Calendar.SECOND)));
		return sb.toString();
	}
	
	/**
	 * @param c A calendar.
	 * @return True if the calendar's date and time is valid; false otherwise.
	 */
	public static boolean isValid(Calendar c) {
		return isValidTime(c.get(Calendar.HOUR), c.get(Calendar.MINUTE), c.get(Calendar.SECOND))
				&& isValidDate(c.get(Calendar.MONTH), c.get(Calendar.DATE), c.get(Calendar.YEAR));
	}
	
	/**
	 * Determines whether or not a given date is valid or not. For instance,
	 * February 29 is not valid in the year 1800.
	 * @param month The month of the year, where January is 1.
	 * @param date The day of the month. The first day of the month is 1.
	 * @param year The year. Must start from year 0.
	 * @return True if the date is valid; false otherwise. For instance,
	 */
	public static boolean isValidDate(int month, int date, int year) {
		return !(month < 1 || month > 12 || date < 1 || year < 0 || date > getDaysInMonth(month, year));
	}
	
	/**
	 * Determines whether or not a given time is valid or not.
	 * @param hour The hour of the day, in 24-hour format. Midnight is hour 0
	 * and 11PM is hour 23.
	 * @param minute The current minute, starting at 0.
	 * @param second The current second, starting at 0.
	 * @return True if the time is valid; false otherwise.
	 */
	public static boolean isValidTime(int hour, int minute, int second) {
		return !(hour < 0 || hour > 23 || minute < 0 || minute > 59 || second < 0 || second > 59);
	}
	
	/**
	 * Takes a String and creates a calendar out of it. Note that this method
	 * will still create a calendar if the input is properly formatted but the
	 * actual date and time are invalid.
	 * @param str The String, in the format MM/DD/YYYY-HH:MM:SS.
	 * @return A new Calendar object containing the specified information. If
	 * the given String is null, this method returns null.
	 * @throws IllegalArgumentException if the String is badly formatted.
	 */
	public static Calendar parseCalendar(String str) throws IllegalArgumentException {
		if(str == null)
			return null;
		if(str.length() != 19)
			throw new IllegalArgumentException("String must be in format MM/DD/YYYY-HH:MM:SS");
		
		Calendar c = Calendar.getInstance();
		try {
			c.set(Calendar.MONTH, Integer.parseInt(str.substring(0, 2)));
			c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(str.substring(3, 5)));
			c.set(Calendar.YEAR, Integer.parseInt(str.substring(6, 10)));
			c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(str.substring(11, 13)));
			c.set(Calendar.MINUTE, Integer.parseInt(str.substring(14, 16)));
			c.set(Calendar.SECOND, Integer.parseInt(str.substring(17, 19)));
			c.set(Calendar.MILLISECOND, 0);
		}
		catch(NumberFormatException e) {
			throw new IllegalArgumentException("String must be in format MM/DD/YYYY-HH:MM:SS");
		}
		return c;
	}
	
	/**
	 * @param month A month, where January is 1.
	 * @param year The year. Used to determine leap year.
	 * @return The number of days in the month.
	 */
	private static int getDaysInMonth(int month, int year) {
		return month == 2 ? (isLeapYear(year) ? 29 : 28) : DAYS_IN_MONTHS[month - 1];
	}
	
	/**
	 * @param year A year.
	 * @return True if the year is a leap year; false otherwise.
	 */
	private static boolean isLeapYear(int year) {
		return year % 4 == 0 ? (year % 100 == 0 ? (year % 400 == 0) : true) : false;
	}
}