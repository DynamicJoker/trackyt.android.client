package trackyt.android.client.activities;

import trackyt.android.client.R;
import trackyt.android.client.R.id;
import trackyt.android.client.R.layout;
import trackyt.android.client.controller.TimeController;
import trackyt.android.client.models.Task;
import trackyt.android.client.reponses.GetAllTasksResponse;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MDialog extends Dialog {
	
	Task task;
	TimeController timeController;
	
	TextView startTask;
	TextView stopTask;
	TextView deleteTask;
	
	Context mContext;

	public MDialog(TimeController timeController, Context context) {
		super(context);
		mContext = context;
		this.timeController = timeController;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mdialog_layout);
		setTitle("Task");
		
		startTask = (TextView) findViewById(R.id.start_task);
		stopTask = (TextView) findViewById(R.id.stop_task);
		deleteTask = (TextView) findViewById(R.id.delete_task);
		
		startTask.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new StartTask().execute();
			}
		});
		
		stopTask.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new StopTask().execute();
			}
		});
		
		deleteTask.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new DeleteTask().execute();
			}
		});
	}
	
	public void setTask(Task task) {
		this.task = task;
	}
	
	private class StartTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			dismiss();
			publishProgress();
			try {
				timeController.startTask(task);
				return true;
			} catch (Exception e) {
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
			dismiss();
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
			dismiss();
			publishProgress();
			try {
				timeController.deleteTask(task);
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
		}
	}
}
