package io.gong;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {
    public static List<CalendarEvent> parseCalendarCSV(String filePath) {
        List<CalendarEvent> events = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while((line = br.readLine()) != null) {
                //Split the line into fields (person, subject, start, end)
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                //Check if this line is valid (should have at least 4 columns)
                if(parts.length < 4) continue;

                String person = parts[0].replace("\"", "").trim();
                String subject = parts[1].replace("\"", "").trim();
                LocalTime start = LocalTime.parse(parts[2].replace("\"", "").trim());
                LocalTime end = LocalTime.parse(parts[3].replace("\"", "").trim());

                //Use the CalendarEvent "blueprint" to create a new event object with these details
                events.add(new CalendarEvent(person, subject, start, end));
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV: " + e.getMessage());
        }
        //After reading the whole file, return the complete list of event objects
        return events;
    }
}
