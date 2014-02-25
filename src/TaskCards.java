
public class TaskCards {
	//should have the following TaskName, TaskType, TaskStartTime, TaskEndTime, TaskDate
	private String TaskName;
	private String TaskType;
	private int TaskDate;
	private int TaskStartTime = 0;
	private int TaskEndTime = 0;
	private int taskPriority = 0;
	
	
	
	public int getTaskPriority() {
		return taskPriority;
	}

	public void setTaskPriority(int taskPriority) {
		this.taskPriority = taskPriority;
	}

	public String getTaskName() {
		return TaskName;
	}

	public void setTaskName(String taskName) {
		TaskName = taskName;
	}

	public String getTaskType() {
		return TaskType;
	}

	public void setTaskType(String taskType) {
		TaskType = taskType;
	}

	public int getTaskDate() {
		return TaskDate;
	}

	public void setTaskDate(int taskDate) {
		TaskDate = taskDate;
	}

	public int getTaskStartTime() {
		return TaskStartTime;
	}

	public void setTaskStartTime(int taskStartTime) {
		TaskStartTime = taskStartTime;
	}

	public int getTaskEndTime() {
		return TaskEndTime;
	}

	public void setTaskEndTime(int taskEndTime) {
		TaskEndTime = taskEndTime;
	}

	
	public TaskCards(){
		
	}
	
	
}

