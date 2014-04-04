package application;

import javafx.beans.property.SimpleStringProperty;

public class TaskDataUI {

	private SimpleStringProperty ID;    
	private SimpleStringProperty Priority;
	private SimpleStringProperty Name;
	private SimpleStringProperty endDate;
	private SimpleStringProperty endTime;
	
	public TaskDataUI() {
		this.ID = new SimpleStringProperty();
		this.Priority = new SimpleStringProperty();
		this.Name = new SimpleStringProperty();
		this.endDate = new SimpleStringProperty();
		this.endTime = new SimpleStringProperty();
	}

	public String getID() {
		return ID.get();
	}
	
	public void setID(String id) {
		ID.set(id);
	}
	
	public String getPriority() {
		return Priority.get();
	}
	
	public void setPriority(String p) {
		Priority.set(p);
	}
	
	public String getName() {
		return Name.get();
	}
	
	public void setName(String n) {
		Name.set(n);
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
		return Priority;
	}
	
	public SimpleStringProperty nameProperty() {
		return Name;
	}
	
	public SimpleStringProperty endDateProperty() {
		return endDate;
	}
	
	public SimpleStringProperty endTimeProperty() {
		return endTime;
	}
}
