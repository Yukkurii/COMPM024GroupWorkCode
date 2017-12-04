/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 *
 * Thanks to Mike Shauneu for the implementation of console progress bar.
 *
 *      https://stackoverflow.com/questions/4573123/java-updating-text-in-the-
 *      command-line-without-a-new-line
 */

package org.ucl.msr.event;

import cc.kave.commons.model.events.IDEEvent;

import java.util.Collections;
import java.util.concurrent.CancellationException;

/**
 * Instances of this class process events up to a specified limit.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class LimitedRunProcessor implements EventProcessor
{
    private long runMax;
    private long runCount;
    private EventProcessor delegate;

    public LimitedRunProcessor(EventProcessor delegate, long runMax)
    {
        this.runCount = 0;
        this.runMax = runMax;
        this.delegate = delegate;
    }

    @Override
    public void process(IDEEvent event)
    {
        if (runCount++ < runMax)
        {
//            printProgress(runCount, runMax);
            delegate.process(event);
        }
        else
        {
            System.out.println();
            throw new CancellationException();
        }
    }

    private void printProgress(long current, long total)
    {
        int percent = (int)(current * 100.0 / total);

        StringBuilder result = new StringBuilder(140);
        result.append('\r');
        result.append(String.join("", Collections.nCopies(percent == 0 ? 2 : 2 - (int) (Math.log10(percent)), " ")));
        result.append(String.format(" %d%% [", percent));
        result.append(String.join("", Collections.nCopies(percent, "=")));
        result.append('>');
        result.append(String.join("", Collections.nCopies(100 - percent, " ")));
        result.append(']');
        result.append(String.join("", Collections.nCopies((int) (Math.log10(total)) - (int) (Math.log10(current)), " ")));
        result.append(String.format(" %d/%d", current, total));

        System.out.print(result);
    }
}
