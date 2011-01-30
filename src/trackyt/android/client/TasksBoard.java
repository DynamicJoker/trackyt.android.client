package trackyt.android.client;

import java.util.ArrayList;

import trackyt.android.client.models.AuthResponse;
import trackyt.android.client.models.Task;
import trackyt.android.client.utils.RequestMaker;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TasksBoard extends Activity {
	ArrayList<Task> taskList;
	AuthResponse auth;
	RequestMaker httpManager;
	
	MyAdapter myAdapter;

	ListView listView;
	Button okButton;
	EditText editText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tasks_board);
		
		Bundle extras = getIntent().getExtras(); 
		auth = (AuthResponse) extras.getSerializable("auth");
		
		httpManager = RequestMaker.getInstance();
		httpManager.initAuth(auth);
		
		taskList = httpManager.getTasks();
		for (Task t : taskList) {
			t.parseTime();
		}

		okButton = (Button) findViewById(R.id.ok_button);
		editText = (EditText) findViewById(R.id.edit_text);
		listView = (ListView) findViewById(R.id.list_view);
		
		myAdapter = new MyAdapter(this, R.id.list_view, taskList);
		listView.setAdapter(myAdapter);
		listView.setCacheColorHint(Color.WHITE);
		
//		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//	
//			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//					long arg3) {
//				Task task = (Task) listView.getItemAtPosition(position);
//				httpManager.deleteTask(task);
//			}
//			
//		});
	}
	
	public void onClickOKButton(View view) {
		String taskDescription = editText.getText().toString();
		Task task = new Task(taskDescription);
		taskList.add(task);
		if (httpManager.createTask(task)) {
			Toast.makeText(this, "Task has been created", Toast.LENGTH_SHORT).show();
			myAdapter.notifyDataSetChanged();
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
}
