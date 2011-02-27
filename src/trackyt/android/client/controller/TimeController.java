package trackyt.android.client.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import trackyt.android.client.TrackytApiAdapter;
import trackyt.android.client.activities.TasksBoard;
import trackyt.android.client.activities.TasksScreen;
import trackyt.android.client.exceptions.NotAuthenticatedException;
import trackyt.android.client.models.ApiToken;
import trackyt.android.client.models.Task;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class TimeController {

	private TasksScreen tasksBoard; 
	private Map<Integer, Task> tasksToUpdate;
	private TrackytApiAdapter mTrackytAdapter;
	private ApiToken token;

	public TimeController(TasksScreen tasksBoard,
			TrackytApiAdapter mTrackytAdapter) {

		tasksToUpdate = new HashMap<Integer, Task>();
		this.tasksBoard = tasksBoard;
		this.mTrackytAdapter = mTrackytAdapter;
	}

	public void authenticate(final String email, final String password,
			final Context context) {
		final Handler h1 = new Handler();
		final Handler h2 = new Handler();

		Thread mThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					token = mTrackytAdapter.authenticate(email, password);
					h1.post(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(context,
									"You have been authenticated",
									Toast.LENGTH_SHORT).show();
						}
					});
				} catch (NotAuthenticatedException e) {
					h2.post(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(context,
									"You have not been authenticated",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		});
		mThread.start();
	}

	public Task addNewTask(final String description, final Context context) {
		if (description.equals("")) {
			throw new IllegalArgumentException("description can't be empty");
		}

		final Handler h1 = new Handler();
		final Handler h2 = new Handler();

		Thread mThread = new Thread(new Runnable() {
			public void run() {
				if (requestMaker.addTask(task)) {
					h1.post(new Runnable() {

						public void run() {
							Toast.makeText(tasksBoard.getApplicationContext(),
									"Task has been created", Toast.LENGTH_SHORT)
									.show();
							updateUI();
						}

					});
				} else {
					h2.post(new Runnable() {

						public void run() {
							Toast.makeText(tasksBoard.getApplicationContext(),
									"Task has not been created, try again",
									Toast.LENGTH_SHORT).show();
						}

					});
				}
			}
		});
		mThread.start();
	}

	public void startAll() {
		final Handler h1 = new Handler();
		final Handler h2 = new Handler();

		Thread mThread = new Thread(new Runnable() {
			public void run() {
				if (requestMaker.startAllTasks()) {

					addAllTaskInQueue();

					h1.post(new Runnable() {

						public void run() {
							Toast.makeText(tasksBoard.getApplicationContext(),
									"All task started", Toast.LENGTH_SHORT)
									.show();
						}

					});
				} else {
					h2.post(new Runnable() {

						public void run() {
							Toast.makeText(tasksBoard.getApplicationContext(),
									"Task has not been started, try again",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			}

		});
		mThread.start();
	}

	public void stopAll() {
		final Handler h1 = new Handler();
		final Handler h2 = new Handler();

		Thread mThread = new Thread(new Runnable() {
			public void run() {
				if (requestMaker.stopAllTasks()) {

					clearQueue();

					h1.post(new Runnable() {

						public void run() {
							Toast.makeText(tasksBoard.getApplicationContext(),
									"All task stopped", Toast.LENGTH_SHORT)
									.show();
						}

					});
				} else {
					h2.post(new Runnable() {

						public void run() {
							Toast.makeText(tasksBoard.getApplicationContext(),
									"Task has not been stopped, try again",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			}

		});
		mThread.start();
	}

	public void startTask(final Task task) {
		if (task == null) {
			throw new IllegalArgumentException();
		}

		final Handler h1 = new Handler();
		final Handler h2 = new Handler();

		Thread mThread = new Thread(new Runnable() {
			public void run() {
				if (requestMaker.startTask(task)) {

					addTaskInQueue(task);

					h1.post(new Runnable() {

						public void run() {
							Toast.makeText(tasksBoard.getApplicationContext(),
									"Task has been started", Toast.LENGTH_SHORT)
									.show();
						}

					});
				} else {
					h2.post(new Runnable() {

						public void run() {
							Toast.makeText(tasksBoard.getApplicationContext(),
									"Task has not been started, try again",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		});
		mThread.start();
	}

	public void stopTask(final Task task) {
		if (task == null) {
			throw new IllegalArgumentException();
		}

		final Handler h1 = new Handler();
		final Handler h2 = new Handler();

		Thread mThread = new Thread(new Runnable() {
			public void run() {
				if (requestMaker.stopTask(task)) {

					removeTaskFromQueue(task);

					h1.post(new Runnable() {

						public void run() {
							Toast.makeText(tasksBoard.getApplicationContext(),
									"Task has been stopped", Toast.LENGTH_SHORT)
									.show();
						}

					});
				} else {
					h2.post(new Runnable() {

						public void run() {
							Toast.makeText(tasksBoard.getApplicationContext(),
									"Task has not been stopped, try again",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		});
		mThread.start();
	}

	public void deleteTask(final Task task) {
		final Handler h1 = new Handler();
		final Handler h2 = new Handler();

		Thread mThread = new Thread(new Runnable() {
			public void run() {
				if (requestMaker.deleteTask(task)) {

					removeTaskFromQueue(task);
					// TODO: Remove task from TasksBoard.tasksList (TBI when Map
					// is done)

					h1.post(new Runnable() {

						public void run() {
							Toast.makeText(tasksBoard.getApplicationContext(),
									"Task has been deleted", Toast.LENGTH_SHORT)
									.show();
							updateUI();
						}

					});
				} else {
					h2.post(new Runnable() {

						public void run() {
							Toast.makeText(tasksBoard.getApplicationContext(),
									"Task has not been deleted, try again",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		});
		mThread.start();

	}

	public void updateUI() {
		tasksBoard.updateUI();
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

	private void createNewThread(final Task task, final Runnable handlerObject) {
		final Handler mHandler = new Handler();

		Thread mThread = new Thread(new Runnable() {
			public void run() {
				requestMaker.addTask(task);
				if (handlerObject != null) {
					mHandler.post(handlerObject);
				}
			}
		});
		mThread.start();
	}

	public void loadTasks() {
		final Handler h1 = new Handler();
		final Handler h2 = new Handler();

		Thread t = new Thread(new Runnable() {

			public void run() {
				h1.post(new Runnable() {
					public void run() {
						tasksBoard.showLoadTaskDialog();
					}
				});

				tasksBoard.initTaskList(requestMaker.getTasks());

				h2.post(new Runnable() {

					public void run() {
						tasksBoard.dismissLoadTaskDialog();
					}

				});
			}

		});
		t.start();
	}

	public void runCount() {
		final Handler mHandler = new Handler();
		Thread mThread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					updateTime(1);
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
