import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TaskCard {
	
	private String name;
	private String type;
	private int priority = 0;
	private String frequency = "";
	private Calendar startDay = new GregorianCalendar();
	private Calendar endDay = new GregorianCalendar();
	private SimpleDateFormat dateString = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private String taskString;
	
	/*private int startDate;
	private int endDate;
	private int startMonth;
	private int endMonth;
	private int startYear;
	private int endYear;
	private int startTime = 0;
	private int endTime = 0;*/

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

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
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
		for (int i = 0; i < priority; i++) {
			taskString += "*";
		}
		taskString += name + ", " + dateString.format(startDay.getTime()) + " - " + dateString.format(endDay.getTime());
		if (!frequency.equals("N")) {
			taskString += " " + frequency;
		}
		return taskString;
	}
	
	/*public int getStartDate() {
	return startDate;
}

public void setStartDate(int startDate) {
	this.startDate = startDate;
}

public int getEndDate() {
	return endDate;
}

public void setEndDate(int endDate) {
	this.endDate = endDate;
}

public int getStartMonth() {
	return startMonth;
}

public void setStartMonth(int startMonth) {
	this.startMonth = startMonth;
}

public int getEndMonth() {
	return endMonth;
}

public void setEndMonth(int endMonth) {
	this.endMonth = endMonth;
}

public int getStartYear() {
	return startYear;
}

public void setStartYear(int startYear) {
	this.startYear = startYear;
}

public int getEndYear() {
	return endYear;
}

public void setEndYear(int endYear) {
	this.endYear = endYear;
}

public int getStartTime() {
	return startTime;
}

public void setStartTime(int startTime) {
	this.startTime = startTime;
}

public int getEndTime() {
	return endTime;
}

public void setEndTime(int endTime) {
	this.endTime = endTime;
}*/
}

