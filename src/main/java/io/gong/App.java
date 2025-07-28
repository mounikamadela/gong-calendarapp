package io.gong;

import java.time.Duration;
import java.util.*;
import java.time.LocalTime;

/**
 * This is the App entry point
 */
public class App {
    public static void main(String[] args) {
        String csvFilePath = "src/main/resources/io/gong/calendar.csv";

        List<CalendarEvent> eventsList = CsvParser.parseCalendarCSV(csvFilePath);

        //Create MeetingScheduler
        MeetingScheduler scheduler = new MeetingScheduler();

        //Prepare test input
        List<String> personList = Arrays.asList("Alice", "Jack", "Bob");
        Duration meetingDuration = Duration.ofMinutes(60);

        //Call the logic
        List<LocalTime> availableSlots = scheduler.findAvailableSlots(personList, meetingDuration, eventsList);

        //Print the results
        System.out.println("Available meeting slots for Alice, Jack, Bob (1 hour)");
        for(LocalTime slot : availableSlots) {
            System.out.println("Available slot: " +slot);
        }
        System.out.println(availableSlots.size());
    }
}
