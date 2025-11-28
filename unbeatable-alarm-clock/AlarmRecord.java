

 // Represents a single alarm record with set time, alarm time, dismissed time, and attempts.
 
public class AlarmRecord {
    private String setTime;
    private String alarmTime;
    private String dismissedTime;
    private int attempts;
    
    public AlarmRecord(String setTime, String alarmTime, String dismissedTime, int attempts) {
        this.setTime = setTime;
        this.alarmTime = alarmTime;
        this.dismissedTime = dismissedTime;
        this.attempts = attempts;
    }
    
    // Getters
    public String getSetTime() {
        return setTime;
    }
    
    public String getAlarmTime() {
        return alarmTime;
    }
    
    public String getDismissedTime() {
        return dismissedTime;
    }
    
    public int getAttempts() {
        return attempts;
    }
    
    @Override
    public String toString() {
        return String.format("Set: %s | Alarm: %s | Dismissed: %s | Attempts: %d", 
                           setTime, alarmTime, dismissedTime, attempts);
    }
}