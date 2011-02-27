package trackyt.android.client.reponses;


public class DeleteTaskResponse extends BaseResponse {
	
	DeleteTaskResponseData data;
	
	public int getTaskId() {
		return data.getTaskId();
	}
	
}

class DeleteTaskResponseData {
	
	int id;
	
	public int getTaskId() {
		return id;
	}
}