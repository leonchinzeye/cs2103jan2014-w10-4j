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
	private SimpleStringProperty Frequency;
	
	public EventDataUI() {
		this.ID = new SimpleStringProperty();
		this.Priority = new SimpleStringProperty();
		this.Name = new SimpleStringProperty();
		this.startDate = new SimpleStringProperty();
		this.startTime = new SimpleStringProperty();
		this.endDate = new SimpleStringProperty();
		this.endTime = new SimpleStringProperty();
		this.Frequency = new SimpleStringProperty();
	}
	
	private EventDataUI(String id, String p, String n, String startD, String startT, String endD, String endT, String freq) {
		this.ID = new SimpleStringProperty(id);
		this.Priority = new SimpleStringProperty(p);
		this.Name = new SimpleStringProperty(n);
		this.startDate = new SimpleStringProperty(startD);
		this.startTime = new SimpleStringProperty(startT);
		this.endDate = new SimpleStringProperty(endD);
		this.endTime = new SimpleStringProperty(endT);
		this.Frequency = new SimpleStringProperty(freq);
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
	
	public String getFrequency() {
		return Frequency.get();
	}
	
	public void setFrequency(String f) {
		Frequency.set(f);
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
	
	public SimpleStringProperty frequencyProperty() {
		return Frequency;
	}
}