package trackyt.android.client.utils;

import trackyt.android.client.models.Time;
public class TimeParser {
	
    private final int ONE_MINUTE = 60;
    private final int ONE_HOUR = 3600;
    private int receivedTime; 
    private Time time; 
    
    public TimeParser(int taskTime) {
    	receivedTime = taskTime;
    	time = new Time();
    }

    public Time convertToTime(){
        if(calculateHours()) {
        	if(calculateMinutes()) {
        		calculateSeconds();
        	}
        }

        return time;
    }
    
    private boolean calculateHours() {
    	time.setHours(receivedTime / ONE_HOUR);
    	return ((receivedTime = receivedTime % ONE_HOUR) != 0);
    }
    
    private boolean calculateMinutes() {
    	time.setMinutes(receivedTime / ONE_MINUTE);
    	return ((receivedTime = receivedTime % ONE_MINUTE) != 0);
    }
    
    private void calculateSeconds() {
    	time.setSeconds(receivedTime);
    }
}
	