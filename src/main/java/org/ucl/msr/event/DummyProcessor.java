/*
 * UCL MSR Processor (c) by Blair Butterworth and Chenghui Fan
 *
 * This work is licensed under the Creative Commons Attribution 4.0
 * International License. To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by/4.0/
 */

package org.ucl.msr.event;

import cc.kave.commons.model.events.IDEEvent;

/**
 * Instances of this class are used a placeholder {@link EventProcessor}.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class DummyProcessor implements EventProcessor
{
    @Override
    public void process(IDEEvent event)
    {
        System.out.println(event);
    }
}
