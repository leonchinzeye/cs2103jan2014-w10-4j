package application;

import java.util.Calendar;
import java.util.GregorianCalendar;

//@author A0097304E
public class TaskCard implements Cloneable {
	
	private String name;
	private String type;
	private int priority = 0;
	private Calendar startDay = new GregorianCalendar();
	private Calendar endDay = new GregorianCalendar();

	public TaskCard(){
	}
	
	public TaskCard(String name, String type, int priority, Calendar start, Calendar end, boolean expired, boolean ongoing){
		this.name = name;
		this.type = type;
		this.priority = priority;
		this.startDay = start;
		this.endDay = end;
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
	
	@Override
	protected Object clone() {
		try {
			return super.clone();
		} catch(CloneNotSupportedException e) {
			return null;
		}
	}
}

