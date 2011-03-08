package trackyt.android.client.activities;

import java.util.List;

import trackyt.android.client.models.Task;


public interface TasksScreen {
	
	void updateUI();
	void dismissLoadTaskDialog();
	List<Task> getTaskList();

}
