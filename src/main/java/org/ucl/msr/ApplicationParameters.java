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
    private static final String DATA_OPTION_LONG = "data";
    private static final String OUTPUT_OPTION_LONG = "output";
    private static final String EVENT_MAX_OPTION_LONG = "event-max";
    private static final String THREAD_MAX_OPTION_LONG = "thread-max";
    
    private CommandLine commandLine;

    public ApplicationParameters(String[] arguments)
    {
        commandLine = getCommandLine(arguments);
    }

    public String getDataPath()
    {
        return commandLine.getOptionValue(DATA_OPTION_LONG);
    }

    public String getOutputPath()
    {
        return commandLine.getOptionValue(OUTPUT_OPTION_LONG);
    }

    public long getEventMax()
    {
        return getLongOption(EVENT_MAX_OPTION_LONG);
    }

    public int getThreadMax()
    {
        return (int)getLongOption(THREAD_MAX_OPTION_LONG);
    }

    public boolean hasEventMax()
    {
        return commandLine.hasOption(EVENT_MAX_OPTION_LONG);
    }

    public boolean hasThreadMax()
    {
        return commandLine.hasOption(THREAD_MAX_OPTION_LONG);
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
        Option dataOption = new Option("d", DATA_OPTION_LONG, true, "the path of the data zip");
        dataOption.setRequired(true);
        dataOption.setType(String.class);

        Option outputOption = new Option("o", OUTPUT_OPTION_LONG, true, "the directory into which reports will be written");
        outputOption.setRequired(true);
        outputOption.setType(String.class);

        Option threadMaxOption = new Option("t", THREAD_MAX_OPTION_LONG, true, "the maximum number of threads to use");
        threadMaxOption.setRequired(false);
        threadMaxOption.setType(Number.class);

        Option eventMaxOption = new Option("e", EVENT_MAX_OPTION_LONG, true, "the maximum number of events to process");
        eventMaxOption.setRequired(false);
        eventMaxOption.setType(Number.class);

        Options options = new Options();
        options.addOption(dataOption);
        options.addOption(outputOption);
        options.addOption(eventMaxOption);
        options.addOption(threadMaxOption);

        return options;
    }

    private long getLongOption(String name)
    {
        try
        {
            return (Long)commandLine.getParsedOptionValue(name);
        }
        catch (Exception exception)
        {
            throw new IllegalArgumentException(exception);
        }
    }
}

