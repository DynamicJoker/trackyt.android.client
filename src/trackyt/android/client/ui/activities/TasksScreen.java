package trackyt.android.client.ui.activities;

import java.util.List;

import trackyt.android.client.models.Task;


public interface TasksScreen {
	
	void updateUI();
	List<Task> getTaskList();

}
