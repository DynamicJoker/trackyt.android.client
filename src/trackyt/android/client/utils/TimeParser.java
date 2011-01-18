package trackyt.android.client.utils;

import trackyt.android.client.models.Time;
public class TimeParser {
	
    private static final int ONE_MINUTE = 60;
    private static final int ONE_HOUR = 3600;
    
    public static Time convertToTime(int receivedTime){
    	Time time = new Time();
    	
    	time.setHours(receivedTime / ONE_HOUR);
    	if ((receivedTime %= ONE_HOUR) != 0){
    		time.setMinutes(receivedTime / ONE_MINUTE);
    		if ((receivedTime %= ONE_MINUTE) != 0) {
    			time.setSeconds(receivedTime);
    		}
    	}
    	
    	return time;
    }
    
}
	