//@author A0100720E
import javafx.beans.property.SimpleStringProperty;
/**
 * The object that will represent a task for the UI to be able to read
 * @author Atul
 *
 */
public class TaskDataUI {

	private SimpleStringProperty ID;    
	private SimpleStringProperty priority;
	private SimpleStringProperty name;
	private SimpleStringProperty endDate;
	private SimpleStringProperty endTime;
	private boolean isExpired;
	
	public TaskDataUI() {
		this.ID = new SimpleStringProperty();
		this.priority = new SimpleStringProperty();
		this.name = new SimpleStringProperty();
		this.endDate = new SimpleStringProperty();
		this.endTime = new SimpleStringProperty();
		this.isExpired = false;
	}

	public String getID() {
		return ID.get();
	}
	
	public void setID(String id) {
		ID.set(id);
	}
	
	public String getPriority() {
		return priority.get();
	}
	
	public void setPriority(String p) {
		priority.set(p);
	}
	
	public String getName() {
		return name.get();
	}
	
	public void setName(String n) {
		name.set(n);
	}
	
	public void setIsExpired(boolean expired) {
		isExpired = expired;
	}
	
	public String getEndDate() {
		return endDate.get();
	}
	
	public void setEndDate(String endD) {
		endDate.set(endD);
	}
	
	public String getEndTime() {
		return endTime.get();
	}
	
	public void setEndTime(String endT) {
		endTime.set(endT);
	}
	
	public SimpleStringProperty idProperty() {
		return ID;
	}
	
	public SimpleStringProperty priorityProperty() {
		return priority;
	}
	
	public SimpleStringProperty nameProperty() {
		return name;
	}
	
	public SimpleStringProperty endDateProperty() {
		return endDate;
	}
	
	public SimpleStringProperty endTimeProperty() {
		return endTime;
	}
	
	public boolean getIsExpired() {
		return isExpired;
	}
}
