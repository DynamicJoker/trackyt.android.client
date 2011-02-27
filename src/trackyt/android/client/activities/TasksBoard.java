package trackyt.android.client.activities;

import java.util.ArrayList;
import java.util.List;

import trackyt.android.client.R;
import trackyt.android.client.controller.TimeController;
import trackyt.android.client.models.Task;
import trackyt.android.client.reponses.AuthResponse;
import trackyt.android.client.utils.RequestMaker;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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

public class TasksBoard extends Activity {
	ArrayList<Task> taskList; // TODO: change to Map
	AuthResponse auth;
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
		
		// TODO: No need to pass data as extras, pass auth obj to
		// requestMaker just after authentication
		Bundle extras = getIntent().getExtras(); 
		auth = (AuthResponse) extras.getSerializable("auth");
		
		requestMaker = RequestMaker.getInstance();
		requestMaker.initAuth(auth);
		// ---------------------------------------------------
		timeController = new TimeController(this);
		itemPressDialog = new MDialog(timeController, this);
		
		initializeControls();
		
		timeController.loadTasks();
		delay();
		setupListView();
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
		this.taskList = (ArrayList<Task>)list;
		for (Task t : taskList) {
			t.parseTime(); // TODO: Remove this somewhere
		}
	}

	public List<Task> getTaskList() {
		return taskList;
	}



	public void updateUI() {
		mAdapter.notifyDataSetChanged();
	}



	public void onClickOKButton(View view) { 
		
		String taskDescription = editText.getText().toString();
		final Task task = new Task(taskDescription);
		task.parseTime();
		taskList.add(task);
		timeController.addNewTask(task);
		timeController.loadTasks();
		editText.setText("");
	}
	

	public void showLoadTaskDialog() {
		pDialogGetTasks = pDialogGetTasks.show(this, "Getting tasks", 
				"Loading your tasks, please wait", true, false);
	}



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
	    	timeController.startAll();
	        return true;
	    case R.id.stop_all:
	    	timeController.stopAll();
	        return true;
	    case R.id.menu_refresh:
	    	timeController.loadTasks();
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	private class MyAdapter extends ArrayAdapter<Task> {
		private LayoutInflater mInflater;
		
		public MyAdapter(Context context, int resource, ArrayList<Task> list) {
			super(context, resource, list);
			/*Getting inflater from the received context*/ 
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
			
			/* Setup views from your layout using data in Object*/
			if (task != null) {
				TextView tvDescription = (TextView) v.findViewById(R.id.task_text_view);
				TextView tvTime = (TextView) v.findViewById(R.id.time_text_view);
				
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

	private void delay() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
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
