package trackyt.android.client.reponses;

import java.util.ArrayList;
import java.util.List;

import trackyt.android.client.models.Task;

public class GetAllTasksResponse extends BaseResponse {
	public GetAllTasksData data;

	public List<Task> getTasksList() {
		return data.getTasksList();
	}
}

class GetAllTasksData {
	
	List<Task> tasksList = new ArrayList<Task>();
	
	public GetAllTasksData() {
		
	}
	
	public List<Task> getTasksList() {
		return tasksList;
	}
}