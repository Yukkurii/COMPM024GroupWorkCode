/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr.event;

import cc.kave.commons.model.events.visualstudio.EditEvent;
import cc.kave.commons.model.naming.idecomponents.IDocumentName;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.ucl.msr.data.EventData;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class TestPatternProcessorTest
{
    @Test
    public void isTestFileTest()
    {
        ZonedDateTime time = createTime(2017, Month.OCTOBER, 1, 2, 3);
        EditEvent testEvent = createEditEvent("1", "FooTest.cs", time);
        EditEvent nonTestEvent = createEditEvent("1", "Foo.cs", time);

        EventData eventData = new EventData();
        TestPatternProcessor processor = new TestPatternProcessor(eventData);
        Assert.assertEquals(true, processor.isTestFile(testEvent));
        Assert.assertEquals(false, processor.isTestFile(nonTestEvent));
    }

    @Test
    public void testProcess()
    {
        ZonedDateTime day1A = createTime(2017, Month.OCTOBER, 1, 2, 3);
        ZonedDateTime day1B = createTime(2017, Month.OCTOBER, 1, 7, 8);
        ZonedDateTime day2 = createTime(2017, Month.OCTOBER, 9, 0, 1);

        EditEvent event1 = createEditEvent("Alpha", "FooTest.cs", day1A);
        EditEvent event2 = createEditEvent("Alpha", "FooTest.cs", day1B);
        EditEvent event3 = createEditEvent("Alpha", "FooTest.cs", day2);
        EditEvent event4 = createEditEvent("Alpha", "Foo.cs", day1A);
        EditEvent event5 = createEditEvent("Beta", "Foo.cs", day1A);
        EditEvent event6 = createEditEvent("Beta", "FooTest.cs", day1B);

        EventData eventData = new EventData();
        TestPatternProcessor processor = new TestPatternProcessor(eventData);
        processor.process(event1);
        processor.process(event2);
        processor.process(event3);
        processor.process(event4);
        processor.process(event5);
        processor.process(event6);


        ZonedDateTime day1Key = day1A.truncatedTo(ChronoUnit.DAYS);
        ZonedDateTime day2Key = day2.truncatedTo(ChronoUnit.DAYS);

        Map<String, Map<ZonedDateTime, Integer>> testEdits = eventData.getEditData().getTestEdits();
        Assert.assertEquals(2, testEdits.get("Alpha").size());
        Assert.assertEquals(1, testEdits.get("Beta").size());

        Assert.assertEquals((Integer)2, testEdits.get("Alpha").get(day1Key));
        Assert.assertEquals((Integer)1, testEdits.get("Alpha").get(day2Key));
        Assert.assertEquals((Integer)1, testEdits.get("Beta").get(day1Key));
    }

    private ZonedDateTime createTime(int year, Month month, int dayOfMonth, int hour, int minute)
    {
        LocalDateTime localTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute);
        return localTime.atZone(ZoneId.of("Europe/London"));
    }

    private EditEvent createEditEvent(String session, String fileName, ZonedDateTime time)
    {
        IDocumentName documentName = Mockito.mock(IDocumentName.class);
        Mockito.when(documentName.getFileName()).thenReturn(fileName);

        EditEvent result = Mockito.mock(EditEvent.class);
        result.ActiveDocument = documentName;
        result.IDESessionUUID = session;
        result.TriggeredAt = time;
        result.NumberOfChanges = 1;

        return result;
    }
}
