//@author A0097304E
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TaskCard implements Cloneable {
	
	private String name;
	private String type;
	private int priority = 0;
	private Calendar startDay = new GregorianCalendar();
	private Calendar endDay = new GregorianCalendar();

	public TaskCard(){
	}
	
	/**
	 * constructor for taskcard
	 */
	public TaskCard(String name, String type, int priority, Calendar start, Calendar end){
		this.name = name;
		this.type = type;
		this.priority = priority;
		this.startDay = start;
		this.endDay = end;
	}

	/**
	 * accesser for the task/event name
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * setter for the task/event name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * accessor for the task/event type
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * setter for the task/event type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * accessor for the task/event priority
	 * @return
	 */
	public int getPriority() {
		return priority;
	}
	
	/**
	 * setter for the task/event priority
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	/**
	 * accessor for the task/event starting time
	 * @return
	 */
	public Calendar getStartDay() {
		return startDay;
	}
	
	/**
	 * setter for the task/event starting time
	 */
	public void setStartDay(Calendar startDay) {
		this.startDay = startDay;
	}
	
	/**
	 * accessor for the task/event ending time
	 * @return
	 */
	public Calendar getEndDay() {
		return endDay;
	}
	
	/**
	 * setter for the task/event ending time
	 */
	public void setEndDay(Calendar end) {
		this.endDay = end;
	}
	
	/**
	 * method to clone the taskcard which ovverides the object class
	 * clone
	 */
	@Override
	protected Object clone() {
		try {
			return super.clone();
		} catch(CloneNotSupportedException e) {
			return null;
		}
	}
}

