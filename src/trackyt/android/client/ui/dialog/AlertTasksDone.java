package trackyt.android.client.ui.dialog;

import trackyt.android.client.R;
import trackyt.android.client.controller.TimeController;
import trackyt.android.client.models.Task;
import trackyt.android.client.ui.activities.TasksDone;
import trackyt.android.client.ui.activities.TasksScreen;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AlertTasksDone {
	AlertDialog.Builder builder;
	AlertDialog alert;
	TimeController timeController;
	Task task;
	TasksScreen screen;
	
	Context mContext;
	
	public AlertTasksDone(Context context, TimeController timeController, TasksScreen screen) {
		this.screen = screen;
		mContext = context;
		this.timeController = timeController;
		init();
	}
	
	public void init() {
		builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Task Action");
		builder.setItems(R.array.tasks_done_dialog_items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	switch (item){
		    	case 0:
		    		new DeleteTask().execute();
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
	
	private class DeleteTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			publishProgress();
			try {
				timeController.deleteTask(task, TasksDone.taskList);
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
				screen.updateUI();
				Toast.makeText(mContext, "Task was deleted on server", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mContext, "Task wasn't deleted, try again", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
