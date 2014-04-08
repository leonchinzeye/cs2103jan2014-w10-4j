package application;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TaskCard implements Cloneable {
	
	private static final int MAX_PRIORITY = 3;
	private String name;
	/*
	 * task types: T and FT
	 * event types: E
	 */
	private String type;
	private int priority = 0;
	private Calendar current = new GregorianCalendar();
	private Calendar startDay = new GregorianCalendar();
	private Calendar endDay = new GregorianCalendar();
	private SimpleDateFormat dateString = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private String taskString;
	private boolean isExpired;
	private boolean isOngoing;
	
	public TaskCard(){
	}
	
	public TaskCard(String name, String type, int priority, Calendar start, Calendar end, boolean expired, boolean ongoing){
		this.name = name;
		this.type = type;
		this.priority = priority;
		this.startDay = start;
		this.endDay = end;
		this.isExpired = expired;
		this.isOngoing = ongoing;
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
	
	public boolean getIsExpired() {
		if (current.after(endDay)) {
			isExpired = true;
		}
		return isExpired;
	}
	
	public void setIsExpired(boolean expired) {
		this.isExpired = expired;
	}
	
	public boolean getIsOngoing() {
		if (!type.contains("T") && current.after(startDay) && current.before(endDay)) {
			isOngoing = true;
		}
		return isOngoing;
	}
	
	public void setIsOngoing(boolean ongoing) {
		this.isOngoing = ongoing;
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
	
	@Override
	protected Object clone() {
		try {
			return super.clone();
		} catch(CloneNotSupportedException e) {
			return null;
		}
	}
}

