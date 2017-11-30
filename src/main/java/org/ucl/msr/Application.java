/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr;

import org.ucl.msr.event.*;
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
    private static TestPatternProcessor testProcessor;
    private static PerformanceProcessor performanceProcessor;

    public static void main(String[] arguments)
    {
        try
        {
            ApplicationParameters parameters = new ApplicationParameters(arguments);
            EventProcessor processor = getEventProcessor(parameters);
            ZipArchive archive = new ZipFile(parameters.getDataPath());

            ExecutorService executor = getExecutorService(parameters);
            EventIterator iterator = new EventIterator(archive, processor, executor);
            executor.invokeAll(Arrays.asList(iterator));

            testProcessor.printCsv();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private static EventProcessor getEventProcessor(ApplicationParameters parameters)
    {
        testProcessor = new TestPatternProcessor();
        performanceProcessor = new PerformanceProcessor();

        EventProcessor result = new CompositeProcessor(testProcessor, performanceProcessor);
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
