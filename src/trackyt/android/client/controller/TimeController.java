package trackyt.android.client.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import trackyt.android.client.TrackytApiAdapter;
import trackyt.android.client.activities.TasksScreen;
import trackyt.android.client.exceptions.NotAuthenticatedException;
import trackyt.android.client.models.ApiToken;
import trackyt.android.client.models.Task;
import android.os.Handler;

public class TimeController {

	private TasksScreen tasksBoard; 
	private Map<Integer, Task> tasksToUpdate;
	private TrackytApiAdapter mTrackytAdapter;
	private ApiToken token;

	public TimeController(TasksScreen tasksBoard,
			TrackytApiAdapter mTrackytAdapter, ApiToken token) {

		tasksToUpdate = new HashMap<Integer, Task>();
		this.tasksBoard = tasksBoard;
		this.mTrackytAdapter = mTrackytAdapter;
		this.token = token;
	}
	
	public TimeController() {
	}

	public ApiToken getToken() {
		if (token == null) {
			throw new NullPointerException("Token is null and can't be returned");
		}
		
		return token;
	}

	public void authenticate(final String email, final String password) throws NotAuthenticatedException {
		token = mTrackytAdapter.authenticate(email, password);
	}

	public void addNewTask(final String description) throws Exception {
		if (description.equals("")) {
			throw new IllegalArgumentException("description can't be empty");
		}

		mTrackytAdapter.addTask(token, description);
	}

	public void startAll() throws Exception {
		mTrackytAdapter.startAll(token);
		addAllTaskInQueue();
	}

	public void stopAll() throws Exception {
		mTrackytAdapter.stopAll(token);
		clearQueue();
	}

	public void startTask(final Task task) throws Exception {
		if (task == null) {
			throw new IllegalArgumentException();
		}

		mTrackytAdapter.startTask(token, task.getId());
		addTaskInQueue(task);
	}

	public void stopTask(final Task task) throws Exception {
		if (task == null) {
			throw new IllegalArgumentException();
		}

		mTrackytAdapter.stopTask(token, task.getId());
		removeTaskFromQueue(task);
	}

	public void deleteTask(final Task task) throws Exception {
		mTrackytAdapter.deleteTask(token, task.getId());
		tasksBoard.getTaskList().remove(task);
		tasksBoard.updateUI();
		removeTaskFromQueue(task);
	}

	public void updateTime(int value) {
		Collection<Task> collection = tasksToUpdate.values();
		Iterator<Task> iterator = collection.iterator();

		while (iterator.hasNext()) {
			Task task = (Task) iterator.next();
			task.setSpent(task.getSpent() + value);
			task.parseTime();
		}
	}

	private void addTaskInQueue(Task task) {
		tasksToUpdate.put(new Integer(task.getId()), task);
	}

	private void removeTaskFromQueue(Task task) {
		tasksToUpdate.remove(new Integer(task.getId()));
	}

	private void clearQueue() {
		tasksToUpdate.clear();
	}

	private void addAllTaskInQueue() {
		Collection<Task> collection = tasksBoard.getTaskList();
		Iterator<Task> iterator = collection.iterator();

		clearQueue();

		while (iterator.hasNext()) {
			Task task = (Task) iterator.next();
			addTaskInQueue(task);
		}
	}

	public void updateUI() {
		tasksBoard.updateUI();
	}
	
	public List<Task> loadTasks() throws Exception {
//		ArrayList<Task> tmp = new ArrayList<Task>(mTrackytAdapter.getAllTasks(token));
		return mTrackytAdapter.getAllTasks(token);
	}

	public void runCount() {
		final Handler mHandler = new Handler();
		Thread mThread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
						updateTime(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mHandler.post(new Runnable() {

						public void run() {
							updateUI();
						}
					});
				}
			}
		});
		mThread.start();
	}
}
