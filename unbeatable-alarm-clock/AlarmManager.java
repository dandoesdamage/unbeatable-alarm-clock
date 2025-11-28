import java.time.LocalTime;
import java.util.LinkedList;

// Manages alarm logic, history, and validation.
public class AlarmManager {
    private LocalTime alarmTime;
    private boolean alarmSet = false;
    private boolean alarmRinging = false;
    private LinkedList<AlarmRecord> alarmHistory;
    
    public AlarmManager() {
        this.alarmHistory = new LinkedList<>();
    }
    
    // Sets an alarm for the specified time.
    // @return true if alarm was set successfully, false otherwise
    public boolean setAlarm(int hour, int minute) {
        if (!isValidTime(hour, minute)) {
            return false;
        }
        
        this.alarmTime = LocalTime.of(hour, minute, 0);
        this.alarmSet = true;
        return true;
    }
    
   // Validates time input (24-hour format).
    public boolean isValidTime(int hour, int minute) {
        return hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59;
    }
    
    // Checks if alarm should trigger at current time.
    public boolean shouldTriggerAlarm() {
        if (!alarmSet || alarmRinging) {
            return false;
        }
        
        LocalTime now = LocalTime.now();
        return now.getHour() == alarmTime.getHour() && 
               now.getMinute() == alarmTime.getMinute() && 
               now.getSecond() == 0;
    }
    
    // Adds a record to alarm history.
    public void addToHistory(AlarmRecord record) {
        alarmHistory.addFirst(record);
    }
    
    // Clears all alarm history.
    public void clearHistory() {
        alarmHistory.clear();
    }
    
    // Gets the alarm history.
    public LinkedList<AlarmRecord> getHistory() {
        return alarmHistory;
    }
    
    // Resets the alarm state.
    public void resetAlarm() {
        this.alarmSet = false;
        this.alarmRinging = false;
    }
    
    // Getters and setters
    public LocalTime getAlarmTime() {
        return alarmTime;
    }
    
    public boolean isAlarmSet() {
        return alarmSet;
    }
    
    public boolean isAlarmRinging() {
        return alarmRinging;
    }
    
    public void setAlarmRinging(boolean alarmRinging) {
        this.alarmRinging = alarmRinging;
    }
}