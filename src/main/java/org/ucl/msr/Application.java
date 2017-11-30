/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr;

import org.ucl.msr.data.EventData;
import org.ucl.msr.event.*;
import org.ucl.msr.report.EditReport;
import org.ucl.msr.zip.ZipArchive;
import org.ucl.msr.zip.ZipFile;

import java.util.Arrays;
import java.util.concurrent.*;

/**
 * Instances of this class contain the entry point to the system. When called,
 * the supplied arguments will be queried for the location of the data
 * collection, which will then be iterated.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class Application
{
    private static final int DEFAULT_THREAD_MAX = 4;

    public static void main(String[] arguments)
    {
        try
        {
            ApplicationParameters parameters = new ApplicationParameters(arguments);
            EventData eventData = new EventData();
            EventProcessor processor = getEventProcessor(parameters, eventData);
            ZipArchive archive = new ZipFile(parameters.getDataPath());

            ExecutorService executor = getExecutorService(parameters);
            EventIterator iterator = new EventIterator(archive, processor, executor);
            executor.invokeAll(Arrays.asList(iterator));

            EditReport editReport = new EditReport(eventData);
            editReport.writeToFile(parameters.getOutputPath(), "edits.csv");
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private static EventProcessor getEventProcessor(ApplicationParameters parameters, EventData eventData)
    {
        EventProcessor testProcessor = new TestPatternProcessor(eventData);
        EventProcessor profileProcessor = new ProfileProcessor(eventData);
        EventProcessor performanceProcessor = new PerformanceProcessor();

        EventProcessor result = new CompositeProcessor(testProcessor, profileProcessor, performanceProcessor);
        if (parameters.hasEventMax()){
            result = new LimitedRunProcessor(result, parameters.getEventMax());
        }
        return result;
    }

    private static ExecutorService getExecutorService(ApplicationParameters parameters)
    {
        int threadMax = parameters.hasThreadMax() ? parameters.getThreadMax() : DEFAULT_THREAD_MAX;
        return Executors.newFixedThreadPool(threadMax);
    }
}
