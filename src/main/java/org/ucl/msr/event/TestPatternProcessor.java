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
import cc.kave.commons.model.events.visualstudio.EditEvent;
import org.ucl.msr.data.EditData;
import org.ucl.msr.data.EventData;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
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
    private EditData editData;

    public TestPatternProcessor(EventData eventData)
    {
        this.editData = eventData.getEditData();
    }

    @Override
    public void process(IDEEvent event)
    {
        if (event instanceof EditEvent)
        {
            EditEvent editEvent = (EditEvent)event;
            incrementEdits(editEvent);
        }
    }

    private void incrementEdits(EditEvent event)
    {
        if (isSourceFile(event))
        {
            if (isTestFile(event))
            {
                incrementEdits(event, editData.getTestEdits());
            } else
                {
                incrementEdits(event, editData.getMainEdits());
            }
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
        edits = edits == null ? 1 : edits + event.NumberOfChanges;
        sessions.put(day, edits);
    }

    boolean isSourceFile(EditEvent editEvent)
    {
        return editEvent.ActiveDocument != null;
    }

    boolean isTestFile(EditEvent editEvent)
    {
        String fileName = editEvent.ActiveDocument.getFileName();
        return fileName.endsWith("Test.cs") || fileName.endsWith("Tests.cs");
    }

    private ZonedDateTime getDay(EditEvent editEvent)
    {
        ZonedDateTime eventTime = editEvent.TriggeredAt;
        return eventTime.truncatedTo(ChronoUnit.DAYS);
    }
}
