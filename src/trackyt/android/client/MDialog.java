package trackyt.android.client;

import trackyt.android.client.controller.TimeController;
import trackyt.android.client.models.Task;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MDialog extends Dialog {
	
	Task task;
	TimeController timeController;
	
	TextView startTask;
	TextView stopTask;
	TextView deleteTask;

	public MDialog(TimeController timeController, Context context) {
		super(context);
		this.timeController = timeController;
		Log.d("Dev", "MDialog instance instantiated");
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
				timeController.startTask(task);
				dismiss();
			}
		});
		
		stopTask.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				timeController.stopTask(task);
				dismiss();
			}
		});
		
		deleteTask.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				timeController.deleteTask(task);
				dismiss();
			}
		});
	}
	
	public void setTask(Task task) {
		this.task = task;
	}
}
