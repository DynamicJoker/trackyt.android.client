package trackyt.android.client.models;


public class Time {
    private int seconds;
    private int minutes;
    private int hours;
    
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
	
	@Override
	public String toString() {
		return (getHours() + ":" + getMinutes() + ":" + getSeconds());
	}
}
