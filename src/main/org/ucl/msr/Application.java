/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr;

import org.ucl.msr.event.EventIterator;
import org.ucl.msr.event.TestPatternProcessor;
import org.ucl.msr.zip.ZipArchive;
import org.ucl.msr.zip.ZipFile;

import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

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
    public static void main(String[] arguments)
    {
        try
        {
            TestPatternProcessor processor = new TestPatternProcessor();
            ApplicationParameters parameters = new ApplicationParameters(arguments);
            ZipArchive archive = new ZipFile(parameters.getDataPath());
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
            EventIterator iterator = new EventIterator(archive, processor, executor);
            executor.submit(iterator);
        }
        catch (Exception exception)
        {
            System.out.println(exception.getMessage());
        }
    }
}
