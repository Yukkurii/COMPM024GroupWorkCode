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
    private String query;

    public ApplicationParameters(String[] arguments)
    {
        CommandLine commandLine = getCommandLine(arguments);
        path = commandLine.getOptionValue("data");
        query = commandLine.getOptionValue("query");
    }

    public String getDataPath()
    {
        return path;
    }

    public String getQuery()
    {
        return query;
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
        Option input = new Option("d", "data", true, "the path of the data zip");
        input.setRequired(true);

        Options options = new Options();
        options.addOption(input);

        return options;
    }
}

