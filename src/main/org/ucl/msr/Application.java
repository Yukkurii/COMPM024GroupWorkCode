/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr;

import java.nio.file.Paths;

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

            EventIterator iterator = new EventIterator(Paths.get(parameters.getDataPath()), processor);
            Thread thread = new Thread(iterator);
            thread.start();
        }
        catch (Exception exception)
        {
            System.out.println(exception.getMessage());
        }
    }
}
