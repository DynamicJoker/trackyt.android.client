package trackyt.android.client.ui.dialog;

import trackyt.android.client.R;
import trackyt.android.client.controller.TimeController;
import trackyt.android.client.models.Task;
import trackyt.android.client.ui.activities.TasksBoard;
import trackyt.android.client.ui.activities.TasksScreen;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class ADialog {
	AlertDialog.Builder builder;
	AlertDialog alert;
	TimeController timeController;
	Task task;
	TasksScreen screen;
	
	Context mContext;
	
	public ADialog(Context context, TimeController timeController, TasksScreen screen) {
		this.screen = screen;
		mContext = context;
		this.timeController = timeController;
		init();
	}
	
	public void init() {
		builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Task Action");
		builder.setItems(R.array.tasks_board_dialog_items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	switch (item){
		    	case 0:
		    		new StartTask().execute();
		    		break;
		    	case 1:
		    		new StopTask().execute();
		    		break;
		    	case 2:
		    		new DeleteTask().execute();
		    		break;
		    	case 3:
		    		new DoneTask().execute();
		    		break;
		    	}
		    }
		});
		alert = builder.create();
	}
	
	public void setTask(Task task) {
		this.task = task;
	}
	
	public void show() {
		alert.show();
	}
	
	private class StartTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			publishProgress();
			try {
				timeController.startTask(task);
				return true;
			} catch (Exception e) {
				Log.d("Dev", "Exception in StartTask()");
				e.printStackTrace();
				return false;
			}
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
			Toast.makeText(mContext, "Starting the task...", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				Toast.makeText(mContext, "Task was started on server", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mContext, "Task wasn't started, try again", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private class StopTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			publishProgress();
			try {
				timeController.stopTask(task);
				return true; 
			} catch (Exception e) {
				return false;
			}
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
			Toast.makeText(mContext, "Stoping the task...", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				Toast.makeText(mContext, "Task was stopped on server", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mContext, "Task wasn't stopped, try again", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private class DeleteTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			publishProgress();
			try {
				screen.freezeViews();
				timeController.deleteTask(task, TasksBoard.taskList);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
			Toast.makeText(mContext, "Deleting the task...", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				Toast.makeText(mContext, "Task was deleted on server", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mContext, "Task wasn't deleted, try again", Toast.LENGTH_SHORT).show();
			}
			
			screen.updateUI();
			screen.unfreezeViews();
		}
	}
	
	private class DoneTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			publishProgress();
			try {
				screen.freezeViews();
				timeController.doneTask(task);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
			Toast.makeText(mContext, "Marking task as done...", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				Toast.makeText(mContext, "Task was marked as done on server", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mContext, "Task wasn't marked as done, try again", Toast.LENGTH_SHORT).show();
			}
			
			screen.updateUI();
			screen.unfreezeViews();
		}
	}
}
