/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr;

import org.apache.commons.cli.*;

/**
 * Instances of this class represent the applications command line. This class
 * should be created by providing the arguments given to the Java process.
 * Methods exist to query the different parameters contained in the command
 * line.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class ApplicationParameters
{
    private String path;
    private long threadMax;
    private long eventMax;

    public ApplicationParameters(String[] arguments)
    {
        try
        {
            CommandLine commandLine = getCommandLine(arguments);
            path = commandLine.getOptionValue("data");
            threadMax = (Long)commandLine.getParsedOptionValue("thread-max");
            eventMax = (Long)commandLine.getParsedOptionValue("event-max");
        }
        catch (ParseException exception)
        {
            throw new IllegalArgumentException(exception);
        }
    }

    public String getDataPath()
    {
        return path;
    }

    public int getThreadMax()
    {
        return (int)threadMax;
    }

    public long getEventMax()
    {
        return eventMax;
    }

    private CommandLine getCommandLine(String[] arguments)
    {
        try
        {
            Options options = getOptions();
            CommandLineParser parser = new DefaultParser();
            return parser.parse(options, arguments);
        }
        catch (ParseException parseException)
        {
            throw new IllegalArgumentException(parseException);
        }
    }

    private Options getOptions()
    {
        Option dataOption = new Option("d", "data", true, "the path of the data zip");
        dataOption.setRequired(true);
        dataOption.setType(String.class);

        Option threadMaxOption = new Option("t", "thread-max", true, "the maximum number of threads to use");
        threadMaxOption.setRequired(false);
        threadMaxOption.setType(Number.class);

        Option eventMaxOption = new Option("e", "event-max", true, "the maximum number of events to process");
        eventMaxOption.setRequired(false);
        eventMaxOption.setType(Number.class);

        Options options = new Options();
        options.addOption(dataOption);
        options.addOption(eventMaxOption);
        options.addOption(threadMaxOption);

        return options;
    }
}

