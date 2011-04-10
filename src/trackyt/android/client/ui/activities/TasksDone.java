package trackyt.android.client.ui.activities;

import java.util.List;

import trackyt.android.client.R;
import trackyt.android.client.controller.TimeController;
import trackyt.android.client.models.Task;
import trackyt.android.client.ui.dialog.ADialog;
import trackyt.android.client.utils.RequestMaker;
import android.app.ActivityGroup;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TasksDone extends ActivityGroup {
	public static String token;
	public static TimeController timeController;
	public static RequestMaker requestMaker;
	
	private List<Task> taskList;
	private MyAdapter mAdapter;
	private ListView listView;
	private ProgressDialog progressDialog;
	private ADialog alert;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tasks_done);
		
		token = TasksBoard.token;
		timeController = TasksBoard.timeController;
		requestMaker = TasksBoard.requestMaker;
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
				taskList = timeController.loadTasks();
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
				Toast.makeText(getApplicationContext(),
						"Something wrong happened, try again",
						Toast.LENGTH_SHORT).show();
				return;
			}

			setupListView();
			updateUI();

			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					Task task = (Task) listView.getItemAtPosition(position);
					alert.setTask(task);
					alert.show();
				}
			});

			progressDialog = null;
		}
	}
	
	private void setupListView() {
		mAdapter = new MyAdapter(this, R.id.list_view, taskList);
		listView.setAdapter(mAdapter);
		listView.setCacheColorHint(Color.WHITE);
	}
	
	public void updateUI() {
		if (mAdapter != null)
			mAdapter.notifyDataSetChanged();
	}
}
