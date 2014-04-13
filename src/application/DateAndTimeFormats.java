//@author A0097304E
package application;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateAndTimeFormats {

	private SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
	
	private SimpleDateFormat[] properDateFormats = {
		new SimpleDateFormat("dd/MM/yyyy"),
		new SimpleDateFormat("dd-MM-yyyy"),
		new SimpleDateFormat("dd MMM yyyy")
	};
	
	private SimpleDateFormat[] lazyYearFormats = {
		new SimpleDateFormat("dd/MM/yy"),
		new SimpleDateFormat("dd-MM-yy"),
		new SimpleDateFormat("dd MMM yy")
	};
	
	private SimpleDateFormat[] lazyDateFormats = {
		new SimpleDateFormat("dd/MM yyyy"),
		new SimpleDateFormat("dd-MM yyyy"),
		new SimpleDateFormat("dd MMM yyyy"),
		new SimpleDateFormat("ddMMM yyyy"),
		new SimpleDateFormat("dd/MMM yyyy")
	};
	
	private SimpleDateFormat[] complete24HrFormat = {
		new SimpleDateFormat("HHmm"),
		new SimpleDateFormat("HH:mm"),
		new SimpleDateFormat("HH.mm")
	};
	
	private SimpleDateFormat[] complete12HrFormat = {
		new SimpleDateFormat("hh:mma"),
		new SimpleDateFormat("hh.mma"),
	};
	
	private SimpleDateFormat[] hourOnly12HrFormat = {
		new SimpleDateFormat("hha"),
	};

	public DateAndTimeFormats() {
		
	}
	
	/**
	 * takes in a string and checks if it is a valid 12 hour
	 * time format with hours only
	 * Eg 2pm
	 * @return
	 * returns a date object if the string is valid, else
	 * returns a null object
	 */
	@SuppressWarnings("deprecation")
	public Date isHourOnly(String input) {
		for(int i = 0; i < hourOnly12HrFormat.length; i++) {
			SimpleDateFormat formatter = hourOnly12HrFormat[i];
			formatter.setLenient(false);
			
			try {
				ParsePosition pos = new ParsePosition(0);
				Date userDate = formatter.parse(input, pos);
				
				if(pos.getIndex() < input.length()) {
					throw new ParseException(input, pos.getIndex());
				}
				
				Calendar enteredDate = GregorianCalendar.getInstance();
				enteredDate.set(Calendar.HOUR_OF_DAY, userDate.getHours());
				enteredDate.set(Calendar.MINUTE, 0);
				enteredDate.set(Calendar.SECOND, 0);
				enteredDate.set(Calendar.MILLISECOND, 0);
				
				return enteredDate.getTime();
			} catch(ParseException e) {
				continue;
			}
		}
		
		return null;
	}
	
	/**
	 * takes in a string and checks if it is a valid 12 hour
	 * time format 
	 * Eg 2.30pm
	 * @return
	 * returns a date object if the string is valid, else
	 * returns a null object
	 */
	@SuppressWarnings("deprecation")
	public Date isComplete12Hr(String input) {
		for(int i = 0; i < complete12HrFormat.length; i++) {
			SimpleDateFormat formatter = complete12HrFormat[i];
			formatter.setLenient(false);
			
			try {
				ParsePosition pos = new ParsePosition(0);
				Date userDate = formatter.parse(input, pos);
				
				if(pos.getIndex() < input.length()) {
					throw new ParseException(input, pos.getIndex());
				}
				
				Calendar enteredDate = GregorianCalendar.getInstance();
				enteredDate.set(Calendar.HOUR_OF_DAY, userDate.getHours());
				enteredDate.set(Calendar.MINUTE, userDate.getMinutes());
				enteredDate.set(Calendar.SECOND, 0);
				enteredDate.set(Calendar.MILLISECOND, 0);
				
				return enteredDate.getTime();
			} catch(ParseException e) {
				continue;
			}
		}
		
		return null;
	}
	
	/**
	 * takes in a string and checks if it is a valid 24 hour
	 * time format 
	 * Eg 16:35
	 * @return
	 * returns a date object if the string is valid, else
	 * returns a null object
	 */
	@SuppressWarnings("deprecation")
	public Date isComplete24Hr(String input) {
		for(int i = 0; i < complete24HrFormat.length; i++) {
			SimpleDateFormat formatter = complete24HrFormat[i];
			formatter.setLenient(false);
			
			try {
				ParsePosition pos = new ParsePosition(0);
				Date userDate = formatter.parse(input, pos);
				
				if(pos.getIndex() < input.length()) {
					throw new ParseException(input, pos.getIndex());
				}
				
				Calendar enteredDate = GregorianCalendar.getInstance();
				enteredDate.set(Calendar.HOUR_OF_DAY, userDate.getHours());
				enteredDate.set(Calendar.MINUTE, userDate.getMinutes());
				enteredDate.set(Calendar.SECOND, 0);
				enteredDate.set(Calendar.MILLISECOND, 0);
				
				return enteredDate.getTime();
			} catch(ParseException e) {
				continue;
			}
		}
		
		return null;
	}
	
	/**
	 * takes in a string and checks if it is a valid date
	 * format without the year entered
	 * Eg 23 May
	 * @return
	 * returns a date object if the string is valid, else
	 * returns a null object
	 */
	public Date isLazyDate(String input) {
		Date today = new Date();
		
		String currentYear = yearFormat.format(today);
		String userInput = input + " " + currentYear;
		
		for(int i = 0; i < lazyDateFormats.length; i++) {
			SimpleDateFormat formatter = lazyDateFormats[i];
			formatter.setLenient(false);

			try {
				ParsePosition pos = new ParsePosition(0);
				Date userDate = formatter.parse(userInput, pos);
				
				if(pos.getIndex() < input.length()) {
					throw new ParseException(userInput, pos.getIndex());
				}
				
				return userDate;
			} catch(ParseException e) {
				continue;
			}
		}
		
		return null;
	}

	/**
	 * takes in a string and checks if it is a valid date
	 * format with the year entered as 2 digits
	 * Eg 23/6/14
	 * @return
	 * returns a date object if the string is valid, else
	 * returns a null object
	 */
	public Date isLazyYearDate(String input) {
		for(int i = 0; i < lazyYearFormats.length; i++) {
			SimpleDateFormat formatter = lazyYearFormats[i];
			formatter.setLenient(false);
			
			Date startOf21stCentury = new GregorianCalendar(2001, 1, 1).getTime();
			formatter.set2DigitYearStart(startOf21stCentury);
			
			try {
				ParsePosition pos = new ParsePosition(0);
				Date userDate = formatter.parse(input, pos);
				
				if(pos.getIndex() < input.length()) {
					throw new ParseException(input, pos.getIndex());
				}
				
				return userDate;
			} catch(ParseException e) {
				continue;
			}
		}
		
		return null;
	}
	
	/**
	 * takes in a string and checks if it is a valid date
	 * format
	 * Eg 23/6/2014
	 * @return
	 * returns a date object if the string is valid, else
	 * returns a null object
	 */
	public Date isProperDate(String input){
		for(int i = 0; i < properDateFormats.length; i++) {
			SimpleDateFormat formatter = properDateFormats[i];
			formatter.setLenient(false);
			
			try {
				ParsePosition pos = new ParsePosition(0);
				Date userDate = formatter.parse(input, pos);
				
				if(pos.getIndex() < input.length()) {
					throw new ParseException(input, pos.getIndex());
				}
				
				return userDate;
			} catch(ParseException e){
				continue;
			}
		}
		
		return null;
	}
}
