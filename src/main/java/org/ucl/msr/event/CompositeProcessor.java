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

import java.util.Arrays;
import java.util.Collection;

/**
 * Instances of this class implement the {@link EventProcessor} interface,
 * delegating process calls to a given collection of delegates.
 *
 * @author Blair Butterworth
 * @author Chenghui Fan
 */
public class CompositeProcessor implements EventProcessor
{
    private Collection<EventProcessor> delegates;

    public CompositeProcessor(Collection<EventProcessor> delegates)
    {
        this.delegates = delegates;
    }

    public CompositeProcessor(EventProcessor ... delegates)
    {
        this(Arrays.asList(delegates));
    }

    @Override
    public void process(IDEEvent event)
    {
        for (EventProcessor delegate: delegates)
        {
            delegate.process(event);
        }
    }
}
