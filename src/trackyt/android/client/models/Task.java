package trackyt.android.client.models;

import org.json.JSONObject;

public class Task {
	private int id;
	private String description;
	private boolean status; 
	private int createdDate; 
	private int startedDate;
	private int stoppedDate;
	private int spent;
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String d) {
		this.description = d; 
	}
}
