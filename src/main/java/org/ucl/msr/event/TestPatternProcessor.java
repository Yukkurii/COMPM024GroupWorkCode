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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Instances of this class search the data set for edit events, recording those
 * that are made to test files and those that are made to non-test files.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class TestPatternProcessor implements EventProcessor
{
    private Map<String, Map<ZonedDateTime, Integer>> testEdits;
    private Map<String, Map<ZonedDateTime, Integer>> mainEdits;
    private Map<String, Collection<String>> sessions;

    public TestPatternProcessor()
    {
        testEdits = new ConcurrentHashMap<>();
        mainEdits = new ConcurrentHashMap<>();
        sessions = new ConcurrentHashMap<>();
    }

    public Map<String, Map<ZonedDateTime, Integer>> getMainEdits()
    {
        return mainEdits;
    }

    public Map<String, Map<ZonedDateTime, Integer>> getTestEdits()
    {
        return testEdits;
    }

    public Map<String, Collection<String>> getSessions()
    {
        return sessions;
    }

    public void printCsv()
    {
        Map<String, Map<ZonedDateTime, Integer>> profileMainEdits = convertSessionsToProfiles(mainEdits);
        Map<String, Map<ZonedDateTime, Integer>> profileTestEdits = convertSessionsToProfiles(testEdits);
        outputInCsvFormat("main", profileMainEdits);
        outputInCsvFormat("test", profileTestEdits);
    }

    private Map<String, Map<ZonedDateTime, Integer>> convertSessionsToProfiles(Map<String, Map<ZonedDateTime, Integer>> edits)
    {
        Map<String, Map<ZonedDateTime, Integer>> result = new HashMap<>();
        for (Entry<String, Collection<String>> session: sessions.entrySet())
        {
            Map<ZonedDateTime, Integer> sessionEdits = new HashMap<>();
            for (String alias: session.getValue())
            {
                Map<ZonedDateTime, Integer> aliasEdits = edits.get(alias);
                if (aliasEdits != null)
                {
                    for (Entry<ZonedDateTime, Integer> aliasEdit : aliasEdits.entrySet())
                    {
                        Integer sessionEdit = sessionEdits.get(aliasEdit.getKey());
                        sessionEdit = sessionEdit == null ? aliasEdit.getValue() : sessionEdit + aliasEdit.getValue();
                        sessionEdits.put(aliasEdit.getKey(), sessionEdit);
                    }
                }
            }
            result.put(session.getKey(), sessionEdits);
        }
        return result;
    }

    private void outputInCsvFormat(String id, Map<String, Map<ZonedDateTime, Integer>> edits)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (Entry<String, Map<ZonedDateTime, Integer>> session: edits.entrySet())
        {
            for (Entry<ZonedDateTime, Integer> edit: session.getValue().entrySet())
            {
                String date = edit.getKey().format(formatter);
                System.out.println(id + "," + session.getKey() + "," + date + "," + edit.getValue());
            }
        }
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
        sessions = sessions == null ? new ConcurrentHashMap<>() : sessions;
        incrementEditsForDay(event, sessions);
        edits.put(event.IDESessionUUID, sessions);
    }

    private void incrementEditsForDay(EditEvent event, Map<ZonedDateTime, Integer> sessions)
    {
        ZonedDateTime day = getDay(event);
        Integer edits = sessions.get(day);
        edits = edits == null ? 1 : edits + 1;
        sessions.put(day, edits);
    }

    boolean isTestFile(EditEvent editEvent)
    {
        String fileName = editEvent.ActiveWindow.getCaption();
        return fileName.endsWith("Test.cs") || fileName.endsWith("Tests.cs");
    }

    private ZonedDateTime getDay(EditEvent editEvent)
    {
        ZonedDateTime eventTime = editEvent.TriggeredAt;
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
