package trackyt.android.client;

import java.util.ArrayList;

import trackyt.android.client.models.AuthResponse;
import trackyt.android.client.models.Task;
import trackyt.android.client.utils.RequestMaker;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TasksBoard extends Activity {
	ArrayList<Task> taskList;
	AuthResponse auth;
	RequestMaker requestMaker;
	
	MyAdapter mAdapter;

	ListView listView;
	Button okButton;
	EditText editText;
	
	MDialog mDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tasks_board);
		
		Bundle extras = getIntent().getExtras(); 
		auth = (AuthResponse) extras.getSerializable("auth");
		
		requestMaker = RequestMaker.getInstance();
		requestMaker.initAuth(auth);
		
		taskList = requestMaker.getTasks();
		for (Task t : taskList) {
			t.parseTime();
		}

		okButton = (Button) findViewById(R.id.ok_button);
		editText = (EditText) findViewById(R.id.edit_text);
		listView = (ListView) findViewById(R.id.list_view);
		
		mAdapter = new MyAdapter(this, R.id.list_view, taskList);
		listView.setAdapter(mAdapter);
		listView.setCacheColorHint(Color.WHITE);
		
		mDialog = new MDialog(this);
		
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Task task = (Task) listView.getItemAtPosition(position);
				mDialog.setTask(task);
				mDialog.show();
				return false;
			}
		});
		
	}
	
	public void onClickOKButton(View view) {
		String taskDescription = editText.getText().toString();
		Task task = new Task(taskDescription);
		task.parseTime();
		taskList.add(task);
		if (requestMaker.addTask(task)) {
			Toast.makeText(this, "Task has been created", Toast.LENGTH_SHORT).show();
			mAdapter.notifyDataSetChanged();
		} else {
			Toast.makeText(this, "Task has not been created, try again", Toast.LENGTH_SHORT).show();
		}
		
		editText.setText("");
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
	
	@Override
	protected void onResume() {
		super.onResume();
		mAdapter.notifyDataSetChanged();
	}
}
