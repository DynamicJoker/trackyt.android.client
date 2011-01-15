package trackyt.android.client.models;

import trackyt.android.client.utils.MyConfig;
import android.util.Log;

public class Time {
    private int seconds;
    private int minutes;
    private int hours;
    
    public Time() {
    	if (MyConfig.DEBUG) Log.d("Dev", "Time object created");
    }
    
	public int getSeconds() {
		return seconds;
	}
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	public int getMinutes() {
		return minutes;
	}
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	public int getHours() {
		return hours;
	}
	public void setHours(int hours) {
		this.hours = hours;
	}
}
