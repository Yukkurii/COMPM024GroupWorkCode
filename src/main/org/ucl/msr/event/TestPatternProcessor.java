/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr.event;

import cc.kave.commons.model.events.IDEEvent;
import cc.kave.commons.model.events.userprofiles.UserProfileEvent;
import cc.kave.commons.model.events.visualstudio.EditEvent;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TestPatternProcessor implements EventProcessor
{
    private Map<String, Map<ZonedDateTime, Integer>> testEdits;
    private Map<String, Map<ZonedDateTime, Integer>> mainEdits;
    private Map<String, Collection<String>> sessions;

    public TestPatternProcessor()
    {
        testEdits = new ConcurrentHashMap();
        mainEdits = new ConcurrentHashMap();
        sessions = new ConcurrentHashMap();
    }

    @Override
    public void process(IDEEvent event)
    {
        if (event instanceof EditEvent)
        {
            EditEvent editEvent = (EditEvent)event;
            incrementEdits(editEvent);
        }
        else if (event instanceof UserProfileEvent)
        {
            UserProfileEvent profileEvent = (UserProfileEvent)event;
            associateSession(profileEvent);
        }
    }

    private void incrementEdits(EditEvent event)
    {
        if (isTestFile(event)){
            incrementEdits(event, testEdits);
        }
        else {
            incrementEdits(event, mainEdits);
        }
    }

    private void incrementEdits(EditEvent event, Map<String, Map<ZonedDateTime, Integer>> edits)
    {
        Map<ZonedDateTime, Integer> sessions = edits.get(event.IDESessionUUID);
        sessions = sessions == null ? new ConcurrentHashMap() : sessions;
        incrementEditsForDay(event, sessions);
        testEdits.put(event.IDESessionUUID, sessions);
    }

    private void incrementEditsForDay(EditEvent event, Map<ZonedDateTime, Integer> sessions)
    {
        ZonedDateTime day = getDay(event);
        Integer edits = sessions.get(day);
        edits = edits == null ? 1 : edits + 1;
        sessions.put(day, edits);
    }

    private boolean isTestFile(EditEvent editEvent)
    {
        String fileName = editEvent.ActiveWindow.getCaption();
        boolean result = fileName.endsWith("Test.cs");
        if (!result && fileName.contains("Test")){
            System.out.println("Unrecorded test file: " + fileName);
        }
        return result;
    }

    private ZonedDateTime getDay(EditEvent editEvent)
    {
        ZonedDateTime eventTime = editEvent.getTriggeredAt();
        return eventTime.truncatedTo(ChronoUnit.DAYS);
    }

    private void associateSession(UserProfileEvent event)
    {
        Collection<String> sessionList = sessions.get(event.ProfileId);
        sessionList = sessionList == null ? Collections.synchronizedList(new ArrayList<String>()) : sessionList;
        sessionList.add(event.IDESessionUUID);
        sessions.put(event.ProfileId, sessionList);
    }
}
