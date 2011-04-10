package trackyt.android.client.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import trackyt.android.client.TrackytApiAdapter;
import trackyt.android.client.exceptions.NotAuthenticatedException;
import trackyt.android.client.models.ApiToken;
import trackyt.android.client.models.Task;
import trackyt.android.client.ui.activities.TasksScreen;
import trackyt.android.client.utils.MyConfig;
import android.os.Handler;
import android.util.Log;

public class TimeController {
	private static final String TAG = "TimeController";

	private TasksScreen tasksBoard; 
	private Map<Integer, Task> tasksToUpdate;
	private TrackytApiAdapter mTrackytAdapter;
	private ApiToken token;
	
	protected boolean counterFlag = false;

	public TimeController(TasksScreen tasksBoard,
			TrackytApiAdapter mTrackytAdapter, ApiToken token) {
		
		if (MyConfig.DEBUG)
			Log.d(TAG, "Instance created.");
		
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
		if (MyConfig.DEBUG) Log.d(TAG, "authenticate()");
		token = mTrackytAdapter.authenticate(email, password);
	}

	public void addNewTask(final String description) throws Exception {
		if (MyConfig.DEBUG) Log.d(TAG, "addNewTask()");
		if (description.equals("")) {
			throw new IllegalArgumentException("description can't be empty");
		}

		mTrackytAdapter.addTask(token, description);
	}

	public void startAll() throws Exception {
		if (MyConfig.DEBUG) Log.d(TAG, "startAll()");
		mTrackytAdapter.startAll(token);
		addAllTaskInQueue();
	}

	public void stopAll() throws Exception {
		if (MyConfig.DEBUG) Log.d(TAG, "stopAll()");
		mTrackytAdapter.stopAll(token);
		clearQueue();
	}

	public void startTask(final Task task) throws Exception {
		if (MyConfig.DEBUG) Log.d(TAG, "startTask()");
		if (task == null) {
			throw new IllegalArgumentException();
		}
		Log.d("Dev", "TimeController startTask()");
		mTrackytAdapter.startTask(token, task.getId());
		addTaskInQueue(task);
	}

	public void stopTask(final Task task) throws Exception {
		if (MyConfig.DEBUG) Log.d(TAG, "stopTask()");
		if (task == null) {
			throw new IllegalArgumentException();
		}

		mTrackytAdapter.stopTask(token, task.getId());
		removeTaskFromQueue(task);
	}

	public void deleteTask(final Task task) throws Exception {
		if (MyConfig.DEBUG) Log.d(TAG, "deleteTask()");
		mTrackytAdapter.deleteTask(token, task.getId());
		tasksBoard.getTaskList().remove(task);
		removeTaskFromQueue(task);
	}
	
	public void doneTask(Task task) throws Exception {
		if (MyConfig.DEBUG) Log.d(TAG, "doneTask()");
		mTrackytAdapter.doneTask(token, task.getId());
		tasksBoard.getTaskList().remove(task);
		removeTaskFromQueue(task);
	}
	
	public List<Task> loadDoneTasks() throws Exception {
		if (MyConfig.DEBUG) Log.d(TAG, "loadDoneTask()");
		return mTrackytAdapter.getDoneTasks(token);
	}

	public void updateTime(int value) {
//		if (MyConfig.DEBUG) Log.d(TAG, "updateTime()");
		Collection<Task> collection = tasksToUpdate.values();
		Iterator<Task> iterator = collection.iterator();

		while (iterator.hasNext()) {
			Task task = (Task) iterator.next();
			task.setSpent(task.getSpent() + value);
			task.parseTime();
		}
	}

	private void addTaskInQueue(Task task) {
		if (MyConfig.DEBUG) Log.d(TAG, "addTaskInQueue()");
		tasksToUpdate.put(new Integer(task.getId()), task);
	}

	private void removeTaskFromQueue(Task task) {
		if (MyConfig.DEBUG) Log.d(TAG, "removeTaskFromQueue()");
		tasksToUpdate.remove(new Integer(task.getId()));
	}

	private void clearQueue() {
		if (MyConfig.DEBUG) Log.d(TAG, "clearQueue()");
		tasksToUpdate.clear();
	}

	private void addAllTaskInQueue() {
		if (MyConfig.DEBUG) Log.d(TAG, "addAllTasksInQueue()");
		Collection<Task> collection = tasksBoard.getTaskList();
		Iterator<Task> iterator = collection.iterator();

		clearQueue();

		while (iterator.hasNext()) {
			Task task = (Task) iterator.next();
			addTaskInQueue(task);
		}
	}

	public void updateUI() {
//		if (MyConfig.DEBUG) Log.d(TAG, "updateUI()");
		tasksBoard.updateUI();
	}
	
	public List<Task> loadTasks() throws Exception {
		if (MyConfig.DEBUG) Log.d(TAG, "loadTasks()");
		return mTrackytAdapter.getAllTasks(token);
	}

	public void runCount() {
		if (MyConfig.DEBUG) Log.d(TAG, "runCount()");
		counterFlag = true;
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
	
	public boolean getCounterFlag(){
		return counterFlag;
	}
}
