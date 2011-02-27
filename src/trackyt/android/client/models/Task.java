package trackyt.android.client.models;

import trackyt.android.client.utils.TimeParser;

public class Task {
	private int id;
	private String description;
	private int status;
	private String createdDate;
	private String startedDate;
	private String stoppedDate;
	private int spent;
	private Time time; 

	public Task() {
	}

	public Task(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getStartedDate() {
		return startedDate;
	}

	public void setStartedDate(String startedDate) {
		this.startedDate = startedDate;
	}

	public String getStoppedDate() {
		return stoppedDate;
	}

	public void setStoppedDate(String stoppedDate) {
		this.stoppedDate = stoppedDate;
	}

	public int getSpent() {
		return spent;
	}

	public void setSpent(int spent) {
		this.spent = spent;
	}
	
	public void parseTime() {
		time = TimeParser.convertToTime(spent);
	}
	
	public String showTime() {
		return time.toString();
	}
	
}