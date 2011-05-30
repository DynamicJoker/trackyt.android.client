package trackyt.android.client.ui.phone;

import java.util.List;

import trackyt.android.client.models.Task;


public interface TasksScreen {
	
	void updateUI();
	List<Task> getTaskList();
	void freezeViews();
	void unfreezeViews();

}
