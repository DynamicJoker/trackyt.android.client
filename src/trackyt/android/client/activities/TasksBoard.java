package trackyt.android.client.activities;

import java.util.ArrayList;
import java.util.List;

import trackyt.android.client.R;
import trackyt.android.client.TrackytApiAdapterFactory;
import trackyt.android.client.controller.TimeController;
import trackyt.android.client.models.ApiToken;
import trackyt.android.client.models.Task;
import trackyt.android.client.utils.RequestMaker;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TasksBoard extends Activity implements TasksScreen {
	ArrayList<Task> taskList; // TODO: change to Map
	RequestMaker requestMaker;

	MyAdapter mAdapter;

	ListView listView;
	Button okButton;
	EditText editText;

	MDialog itemPressDialog;
	ProgressDialog pDialogGetTasks;

	TimeController timeController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tasks_board);

		Bundle extras = getIntent().getExtras();
		String token = (String) extras.get("token");

		timeController = new TimeController(this,
				TrackytApiAdapterFactory.createV11Adapter(), new ApiToken(token));
		itemPressDialog = new MDialog(timeController, this);

		initializeControls();
		try {
			taskList = timeController.loadTasks();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"Something wrong happened, try again",
					Toast.LENGTH_SHORT);
			e.printStackTrace();
		}
		
		Log.d("Dev", "Task list: " + taskList.toString());
		Log.d("Dev", "Tasks are in TasksBoard activity");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("Dev", "about to setup List View");
		
		setupListView();
		updateUI();
		timeController.runCount();

		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Task task = (Task) listView.getItemAtPosition(position);
				itemPressDialog.setTask(task);
				itemPressDialog.show();
				return false;
			}
		});
	}

	public void initTaskList(List<Task> list) {
		Log.d("Dev", "Task list initialized");
		this.taskList = (ArrayList<Task>) list;
		for (Task t : taskList) {
			t.parseTime(); // TODO: Remove this somewhere
		}
	}

	@Override
	public List<Task> getTaskList() {
		return taskList;
	}

	@Override
	public void updateUI() {
		mAdapter.notifyDataSetChanged();
	}

	public void onClickOKButton(View view) {

		String taskDescription = editText.getText().toString();
		final Task task = new Task(taskDescription);
		task.parseTime();
		taskList.add(task);
		try {
			timeController.addNewTask(task.getDescription());
			timeController.loadTasks();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"Something wrong happened, try again",
					Toast.LENGTH_SHORT);
			e.printStackTrace();
		}
		editText.setText("");
	}

	@Override
	public void showLoadTaskDialog() {
		pDialogGetTasks = pDialogGetTasks.show(this, "Getting tasks",
				"Loading your tasks, please wait", true, false);
	}

	@Override
	public void dismissLoadTaskDialog() {
		if (pDialogGetTasks != null)
			pDialogGetTasks.dismiss();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.taskboard_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.start_all:
			try {
				timeController.startAll();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"Something wrong happened, try again",
						Toast.LENGTH_SHORT);
				e.printStackTrace();
			}
			return true;
		case R.id.stop_all:
			try {
				timeController.stopAll();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"Something wrong happened, try again",
						Toast.LENGTH_SHORT);
				e.printStackTrace();
			}
			return true;
		case R.id.menu_refresh:
			try {
				timeController.loadTasks();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"Something wrong happened, try again",
						Toast.LENGTH_SHORT);
				e.printStackTrace();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class MyAdapter extends ArrayAdapter<Task> {
		private LayoutInflater mInflater;

		public MyAdapter(Context context, int resource, ArrayList<Task> list) {
			super(context, resource, list);
			/* Getting inflater from the received context */
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;

			if (v == null) {
				v = mInflater.inflate(R.layout.list_item, null);
			}

			/* Take an instance of your Object from taskList */
			Task task = taskList.get(position);

			/* Setup views from your layout using data in Object */
			if (task != null) {
				TextView tvDescription = (TextView) v
						.findViewById(R.id.task_text_view);
				TextView tvTime = (TextView) v
						.findViewById(R.id.time_text_view);

				if (tvDescription != null) {
					tvDescription.setText(task.getDescription());
				}

				if (tvTime != null) {
					tvTime.setText(task.showTime());
				}
			}

			return v;
		}
	}

	private void setupListView() {
		mAdapter = new MyAdapter(this, R.id.list_view, taskList);
		listView.setAdapter(mAdapter);
		listView.setCacheColorHint(Color.WHITE);
	}

	private void initializeControls() {
		okButton = (Button) findViewById(R.id.ok_button);
		editText = (EditText) findViewById(R.id.edit_text);
		listView = (ListView) findViewById(R.id.list_view);
	}

}
