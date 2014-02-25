
public class TaskCards {
	//should have the following TaskName, TaskType, TaskStartTime, TaskEndTime, TaskDate
	private String taskName;
	private String taskType;
	private int taskDate;
	private int taskStartTime = 0;
	private int taskEndTime = 0;
	private int taskPriority = 0;
	
	
	
	public int getTaskPriority() {
		return taskPriority;
	}

	public void setTaskPriority(int taskPriority) {
		this.taskPriority = taskPriority;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public int getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(int taskDate) {
		this.taskDate = taskDate;
	}

	public int getTaskStartTime() {
		return taskStartTime;
	}

	public void setTaskStartTime(int taskStartTime) {
		this.taskStartTime = taskStartTime;
	}

	public int getTaskEndTime() {
		return taskEndTime;
	}

	public void setTaskEndTime(int taskEndTime) {
		this.taskEndTime = taskEndTime;
	}

	
	public TaskCards(){
		
	}
	
	
}

