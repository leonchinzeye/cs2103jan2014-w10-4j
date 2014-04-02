package application;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TaskCard {
	
	private static final int MAX_PRIORITY = 3;
	private String name;
	/*
	 * task types: T and FT
	 * event types: E
	 */
	private String type;
	private int priority = 0;
	private Calendar startDay = new GregorianCalendar();
	private Calendar endDay = new GregorianCalendar();
	private SimpleDateFormat dateString = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private String taskString;
	
	public TaskCard(){
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public Calendar getStartDay() {
		return startDay;
	}
	
	public void setStartDay(Calendar startDay) {
		this.startDay = startDay;
	}
	
	public Calendar getEndDay() {
		return endDay;
	}
	
	public void setEndDay(Calendar end) {
		this.endDay = end;
	}
	
	public String getTaskString() {
		taskString = "";
		for (int i = 0; i < MAX_PRIORITY; i++) {
			if (priority > i) {
				taskString += "*";
			} else {
				taskString += " ";
			}
		}
		if(type.equals("FT") && type.equals("T")){
			taskString += name + ", " + dateString.format(startDay.getTime()); 
		} else {
			taskString += name + ", " + dateString.format(startDay.getTime()) + " - " + dateString.format(endDay.getTime());
		}
		
		
		return taskString;
	}
}

