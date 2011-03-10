package trackyt.android.client.activities;

import trackyt.android.client.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ADialog extends AlertDialog {
	AlertDialog.Builder builder;
	AlertDialog alert;
	
	Context mContext;
	
	public ADialog(Context context) {
		super(context);
		mContext = context;
		init();
	}
	
	public void init() {
		builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Task");
		builder.setItems(R.array.select_dialog_items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    }
		});
		alert = builder.create();
	}
	
	public void show() {
		alert.show();
	}
}
