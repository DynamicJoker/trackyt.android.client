package trackyt.android.client;

import java.util.ArrayList;

import trackyt.android.client.models.Task;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TasksBoard extends Activity {
	ArrayList<Task> taskList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tasks_board);
		
		taskList = new ArrayList<Task>();
		ListView listView = (ListView) findViewById(R.id.list_view);
		
		listView.setAdapter(new MyAdapter(this, R.id.list_view, taskList));
		
		generateTestData(); // To be removed
		
	}
	
	/* TO BE REMOVED */
	private void generateTestData() {
		for (int i = 0; i < 15; i++) {
			Task t = new Task();
			t.setDescription("Description: " + i);
			taskList.add(t);
		}
	}

	private class MyAdapter extends ArrayAdapter<Task> {
    	private ArrayList<Task> taskList;
    	
    	private LayoutInflater mInflater;
    	
    	public MyAdapter(Context context, int resource, ArrayList<Task> list) {
    		super(context, resource, list);
    		this.taskList = list;
    		
    		/*Getting inflater from the received context*/ 
    		mInflater = LayoutInflater.from(context);
    	}

    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		View v = convertView;
    		
    		if (v == null) {
    			v = mInflater.inflate(R.layout.list_item, null);
    		}
    		
    		/* Take an instance of your Object from oList List*/
    		Task task = taskList.get(position);
    		
    		/* Setup views from your layout using data in Object*/
    		if (task != null) {
    			TextView tv = (TextView) v.findViewById(R.id.task_text_view);
    			
    			if (tv != null) {
    				tv.setText(task.getDescription());
    			}
    		}
    		
    		return v;
    	}
    }
}
