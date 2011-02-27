package trackyt.android.client.reponses;

import trackyt.android.client.models.Task;

public class StartTaskResponse extends BaseResponse{
	StartTaskResponseData data;
	
	public Task getTask() {
		return data.getTask();
	}
}

class StartTaskResponseData {
	Task task;
	
	public Task getTask() {
		return task;
	}
}
