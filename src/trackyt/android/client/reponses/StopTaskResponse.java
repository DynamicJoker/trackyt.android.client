package trackyt.android.client.reponses;

import trackyt.android.client.models.Task;

public class StopTaskResponse extends BaseResponse{
	StopTaskResponseData data;
	
	public Task getTask() {
		return data.getTask();
	}
}

class StopTaskResponseData {
	Task task;
	
	public Task getTask() {
		return task;
	}
}