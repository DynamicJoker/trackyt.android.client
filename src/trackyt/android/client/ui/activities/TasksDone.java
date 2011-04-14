package trackyt.android.client.ui.activities;

import java.util.List;

import trackyt.android.client.R;
import trackyt.android.client.controller.TimeController;
import trackyt.android.client.models.Task;
import trackyt.android.client.ui.dialog.AlertTasksDone;
import trackyt.android.client.utils.RequestMaker;
import android.app.ActivityGroup;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TasksDone extends ActivityGroup implements TasksScreen {
	private static final String TAG = "TasksDone";
	public static String token;
	public static TimeController timeController;
	public static RequestMaker requestMaker;
	public static List<Task> taskList;
	
	private MyAdapter mAdapter;
	private ListView listView;
	private ProgressDialog progressDialog;
	private AlertTasksDone alert;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tasks_done);
		
		token = TasksBoard.token;
		timeController = TasksBoard.timeController;
		requestMaker = TasksBoard.requestMaker;
		
		listView = (ListView) findViewById(R.id.list_view_done);
		
		new TasksLoader().execute();
		
		alert = new AlertTasksDone(this, timeController, this);
	}
	
	private class MyAdapter extends ArrayAdapter<Task> {
		private LayoutInflater mInflater;

		public MyAdapter(Context context, int resource, List<Task> list) {
			super(context, resource, list);
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			ViewHolder viewHolder;

			Task task = taskList.get(position);
			
			if (v == null) {
				v = mInflater.inflate(R.layout.list_item, null);
				viewHolder = new ViewHolder(v);
				v.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) v.getTag(); 
			}
			
			viewHolder.populateFrom(task);

			return v;
		}

	}

	static class ViewHolder {
		private TextView description;
		private TextView time;
		private View view;

		ViewHolder(View view) {
			this.view = view;
			description = (TextView) view.findViewById(R.id.task_text_view);
			time = (TextView) view.findViewById(R.id.time_text_view);
		}

		void populateFrom(Task task) {
			description.setText(task.getDescription());
			time.setText(task.showTime());
		}
	}
	
	private class TasksLoader extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {

			try {
				publishProgress();
				taskList = timeController.loadDoneTasks();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
			}

		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
			progressDialog = ProgressDialog.show(TasksDone.this, "",
					"Loading tasks. Please wait...");
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			if (!result) {
				Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				v.vibrate(50);
				Toast.makeText(getApplicationContext(),
						"Something wrong happened, try again",
						Toast.LENGTH_SHORT).show();
				return;
			}

			setupListView();
			updateUI();
			
			listView.setOnItemClickListener(mListener);

			progressDialog = null;
		}
	}
	
	private AdapterView.OnItemClickListener mListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			Task task = (Task) listView.getItemAtPosition(position);
			alert.setTask(task);
			alert.show();
		}
	};
	
	private void setupListView() {
		mAdapter = new MyAdapter(this, R.id.list_view, taskList);
		listView.setAdapter(mAdapter);
		listView.setCacheColorHint(Color.WHITE);
	}
	
	public void updateUI() {
		if (mAdapter != null)
			mAdapter.notifyDataSetChanged();
	}

	@Override
	public List<Task> getTaskList() {
		return taskList;
	}
	
	public void newTasksLoader() {
		new TasksLoader().execute();
	}
	
	@Override
	public void freezeViews() {
		listView.setOnItemClickListener(null);
		Log.d(TAG, "freezeViews()");
	}

	@Override
	public void unfreezeViews() {
		listView.setOnItemClickListener(mListener);
		Log.d(TAG, "unfreezeViews()");
	}
	
}
