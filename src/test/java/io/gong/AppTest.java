package io.gong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import java.time.LocalTime;
import java.time.Duration;
import java.util.*;

/**
 * Unit test for Gong calendar scheduler
 */
public class AppTest {

    @Test
    public void testFindAvailableSlots() {
        //Set up test events
        List<CalendarEvent> eventsList = Arrays.asList(
            new CalendarEvent("Alice", "Yoga", LocalTime.of(8, 0), LocalTime.of(9, 0)),
            new CalendarEvent("Jack", "Sales call", LocalTime.of(12, 0), LocalTime.of(13, 0)),
            new CalendarEvent("Bob", "Lunch", LocalTime.of(13,0), LocalTime.of(14, 0))
        );

        MeetingScheduler scheduler = new MeetingScheduler();
        List<String> personList = Arrays.asList("Alice", "Jack", "Bob");
        Duration eventDuration = Duration.ofMinutes(60);

        List<LocalTime> availableSlots = scheduler.findAvailableSlots(personList, eventDuration, eventsList);

        System.out.println("Slots " +availableSlots);
        System.out.println("Slot count " +availableSlots.size());

        //At least one slot expected at the beginning of the day. 
        assertTrue(availableSlots.contains(LocalTime.of(7, 0)));
        //Meeting slot during Alice's.
        assertFalse(availableSlots.contains(LocalTime.of(8, 0)));
        //Meeting slot during Jack's.
        assertFalse(availableSlots.contains(LocalTime.of(12, 0)));
        //Meeting slot during Bob's.
        assertFalse(availableSlots.contains(LocalTime.of(13, 0)));
        assertTrue(availableSlots.contains(LocalTime.of(18, 0))); //last possible slot for 1 hour.
        //Check that the list has correct number of slots.
        assertEquals(9, availableSlots.size());
    }
}
