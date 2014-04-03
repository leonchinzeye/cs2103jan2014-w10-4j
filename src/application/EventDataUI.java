package application;

import javafx.beans.property.SimpleStringProperty;

public class EventDataUI {

	private SimpleStringProperty ID; 
	private SimpleStringProperty Priority;
	private SimpleStringProperty Name;
	private SimpleStringProperty startDate;
	private SimpleStringProperty startTime;
	private SimpleStringProperty endDate;
	private SimpleStringProperty endTime;
	private boolean isExpired;
	private boolean isOngoing;
	
	public EventDataUI() {
		this.ID = new SimpleStringProperty();
		this.Priority = new SimpleStringProperty();
		this.Name = new SimpleStringProperty();
		this.startDate = new SimpleStringProperty();
		this.startTime = new SimpleStringProperty();
		this.endDate = new SimpleStringProperty();
		this.endTime = new SimpleStringProperty();
		this.isExpired = true;
		this.isOngoing = false;
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
	
	public String getStartDate() {
		return startDate.get();
	}
	
	public void setStartDate(String startD) {
		startDate.set(startD);
	}
	
	public String getStartTime() {
		return startTime.get();
	}
	
	public void setStartTime(String startT) {
		startTime.set(startT);
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
	
	public boolean getIsExpired() {
		return isExpired;
	}
	
	public void setIsExpired(boolean isEx) {
		isExpired = isEx;
	}
	
	public boolean getIsOngoing() {
		return isOngoing;
	}
	
	public void setIsOngoing(boolean isOn) {
		isOngoing = isOn;
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
	
	public SimpleStringProperty startDateProperty() {
		return startDate;
	}
	
	public SimpleStringProperty startTimeProperty() {
		return startTime;
	}
	
	public SimpleStringProperty endDateProperty() {
		return endDate;
	}
	
	public SimpleStringProperty endTimeProperty() {
		return endTime;
	}
}