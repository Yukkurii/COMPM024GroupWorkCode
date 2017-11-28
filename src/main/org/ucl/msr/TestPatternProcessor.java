/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr;

import cc.kave.commons.model.events.IDEEvent;

public class TestPatternProcessor implements EventProcessor
{
    @Override
    public void process(IDEEvent event)
    {
        System.out.println(event.toString());
    }
}
