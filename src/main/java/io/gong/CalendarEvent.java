package io.gong;
import java.time.LocalTime;

/**
 * CalendarEvent represents a single event in a person's calendar.
 * Each event has a person name, event subject, start time, and end time.
 * This class is immutable fields are set at creation and cannot be changed later.
 */
    
public class CalendarEvent {
    
    //The name of the person attending the event (e.g; Alice)
    private final String personName;

    //The subject of the event (e.g; "Yoga")
    private final String subjectName;

    //The start time of the event (e.g; "8;00")
    private final LocalTime startTime;

    //The end time of the event (e.g; "15:00")
    private final LocalTime endTime;

    /**Constructor to create fully initialized CalendarEvent object*/
    public CalendarEvent(String personName, String subjectName, LocalTime startTime, LocalTime endTime) {
        this.personName = personName;
        this.subjectName = subjectName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getPersonName() {
        return personName;
    }
    public String getSubjectName() {
        return subjectName;
    }
    public LocalTime getStartTime() {
        return startTime;
    }
    public LocalTime getEndTime() {
        return endTime;
    }   
}
