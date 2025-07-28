package io.gong;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.*;

public class MeetingScheduler {

    private static final LocalTime DAY_START = LocalTime.of(7, 0);
    private static final LocalTime DAY_END = LocalTime.of(19, 0);

    public List<LocalTime> findAvailableSlots(List<String> personList, Duration eventDuration,
            List<CalendarEvent> eventsList) {

        // Group each person's event and sort by start time.
        Map<String, List<CalendarEvent>> eventsByPerson = new HashMap<>();
        for (String person : personList) {
            List<CalendarEvent> personEvents = new ArrayList<>();
            for (CalendarEvent event : eventsList) {
                if (person.equalsIgnoreCase(event.getPersonName())) {
                    personEvents.add(event);
                }
            }
            // Sort events for this person by their start time.
            personEvents.sort(Comparator.comparing(CalendarEvent::getStartTime));
            eventsByPerson.put(person, personEvents);
        }

        // Compute each person's free intervals within the workday.
        Map<String, List<Interval>> freeIntervalsByPerson = new HashMap<>();
        for (String person : personList) {
            List<Interval> freeIntervals = getFreeIntervals(eventsByPerson.get(person));
            freeIntervalsByPerson.put(person, freeIntervals);
        }

        // Find all intervals when everyone is free.
        List<Interval> commonFreeIntervals = intersectAllFreeIntervals(
                new ArrayList<>(freeIntervalsByPerson.values()));

        System.out.println("Common free intervals: " + commonFreeIntervals);

        // For each shared interval, Find Every Valid Meeting Start Time.
        List<LocalTime> availableSlots = new ArrayList<>();
        for (Interval interval : commonFreeIntervals) {
            LocalTime slot = interval.start;
            // This loop checks every 15 minute start time within a free interval and adds
            // it to list if meeting
            // would finish before free time ends. It stops as soon as a meeting would go
            // past the interval.
            while (!slot.plus(eventDuration).isAfter(interval.end)) {
                availableSlots.add(slot);
                slot = slot.plus(eventDuration);
            }
        }
        return availableSlots;
    }

    /**
     * Helper class to represent a time interval (Start and End)
     */
    private static class Interval {
        final LocalTime start;
        final LocalTime end;

        Interval(LocalTime start, LocalTime end) {
            this.start = start;
            this.end = end;
        }
        @Override
        public String toString() {
            return "[" + start + "-" + end + "]";
        }
    }

    private List<Interval> getFreeIntervals(List<CalendarEvent> eventsList) {
        if (eventsList == null)
            return new ArrayList<>(); // Defensive check for null input.
        List<Interval> freeIntervals = new ArrayList<>();
        // End time of last busy event we checked, at begining we set this to
        // DAY_START(07:00)
        LocalTime prevEnd = DAY_START;
        for (CalendarEvent event : eventsList) {
            if (event.getStartTime().isAfter(prevEnd)) {
                freeIntervals.add(new Interval(prevEnd, event.getStartTime()));
            }
            prevEnd = event.getEndTime().isAfter(prevEnd) ? event.getEndTime() : prevEnd;
        }

        if (prevEnd.isBefore(DAY_END)) {
            freeIntervals.add(new Interval(prevEnd, DAY_END));
        }
        return freeIntervals;
    }

    // this is the logic that finds time where everyone is free at the same time
    private List<Interval> intersectAllFreeIntervals(List<List<Interval>> freeIntervalsByPerson) {
        if (freeIntervalsByPerson.isEmpty()) {
            return Collections.emptyList(); // returning an empty list, no way to schedule meeting
        }
        List<Interval> intersectionIntervals = freeIntervalsByPerson.get(0);
        for (int i = 1; i < freeIntervalsByPerson.size(); i++) {
            intersectionIntervals = intersectTwo(intersectionIntervals, freeIntervalsByPerson.get(i));
            if (intersectionIntervals.isEmpty()) {
                break;
            }
        }
        return intersectionIntervals;
    }

    // To schedule a meeting with two people, you need to find blocks of time when
    // both are free
    private List<Interval> intersectTwo(List<Interval> a, List<Interval> b) {
        List<Interval> result = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < a.size() && j < b.size()) {
            LocalTime maxStart = a.get(i).start.isAfter(b.get(j).start) ? a.get(i).start : b.get(j).start;
            LocalTime minEnd = a.get(i).end.isBefore(b.get(j).end) ? a.get(i).end : b.get(j).end;

            if (maxStart.isBefore(minEnd) || maxStart.equals(minEnd)) {
                result.add(new Interval(maxStart, minEnd));
            }
            // Move to next interval.
            if (a.get(i).end.isBefore(b.get(j).end)) {
                i++;
            } else {
                j++;
            }
        }
        return result;
    }
}
