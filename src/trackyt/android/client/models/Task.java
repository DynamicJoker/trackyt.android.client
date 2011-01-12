package trackyt.android.client.models;

import org.json.JSONObject;

public class Task {
	private int id;
	private String description;
	private int status; 
	private int createdDate; 
	private int startedDate;
	private int stoppedDate;
	private int spent;
	
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

	public int getCreatedDate() {
		return createdDate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setCreatedDate(int createdDate) {
		this.createdDate = createdDate;
	}
	public int getStartedDate() {
		return startedDate;
	}
	public void setStartedDate(int startedDate) {
		this.startedDate = startedDate;
	}
	public int getStoppedDate() {
		return stoppedDate;
	}
	public void setStoppedDate(int stoppedDate) {
		this.stoppedDate = stoppedDate;
	}
	public int getSpent() {
		return spent;
	}
	public void setSpent(int spent) {
		this.spent = spent;
	}
	
//	public String getDescription() {
//		return description;
//	}
//	
//	public void setDescription(String d) {
//		this.description = d; 
//	}
	
}
